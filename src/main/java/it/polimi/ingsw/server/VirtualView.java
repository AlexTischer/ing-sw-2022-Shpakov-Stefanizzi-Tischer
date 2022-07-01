package it.polimi.ingsw.server;

import it.polimi.ingsw.exceptions.WrongActionException;
import it.polimi.ingsw.modelChange.ExceptionChange;
import it.polimi.ingsw.modelChange.ModelChange;
import it.polimi.ingsw.packets.Packet;
import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.utils.Observer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * <p>This class is a bridge between the {@link Connection} and the {@link Game} classes. Unlike the Connection, it's persistent even on client's disconnections and reconnections</p>
 * <ul> Contains:
 *     <li>{@link #game} </li>
 *     <li>{@link #player} is the specific {@link Player} of this VirtualView</li>
 *     <li>{@link #clientConnection} is the specific {@link Connection} of this VirtualView</li>
 * </ul>
 */
public class VirtualView implements Observer<ModelChange> {

    private Game game;
    private Player player;
    private Connection clientConnection;

    @Override
    public void update(ModelChange modelChange) {
        if(player.isActive() && clientConnection!=null){
            clientConnection.send(modelChange);
        }
    }

    /**
     * TODO sendPacket
     *
     * @param packet
     */
    public void sendPacket(Packet packet) {
        /*send message to client*/
        System.out.println("VirtualView says: I am in sendPacket(). I have received " + packet.getClass() + " from " + player.getName());

        //sends exception back to the client in case of error
        if (!player.getName().equals(game.getCurrentPlayer().getName())) {
            System.out.println(player.getName() + " is not current player");
            //if client tries to act while it's not his turn, then it is the wrong action
            clientConnection.send(new ExceptionChange(new WrongActionException("You are not current player!")));
        }
        //Send packet only if the game is on and not suspended
        else if(game.getGameBoard().isGameOn() && !game.isSuspended()) {
            try {
                game.usePacket(packet);
            } catch (RuntimeException e) {
                //any exception related to incorrect user action, sends an exception back to client
                clientConnection.send(new ExceptionChange(e));
                //exception must not be followed by endOfChanges because user still needs to make the correct move
            }
        }
        else if (game.isSuspended()){
            new Thread(()->{
                synchronized (this) {
                    while (game.isSuspended()) {
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    try {
                        game.usePacket(packet);
                    } catch (RuntimeException e) {
                        //any exception related to incorrect user action, sends an exception back to client
                        clientConnection.send(new ExceptionChange(e));
                        //exception must not be followed by endOfChanges because user still needs to make the correct move
                    }
                }
            }).start();
        }
    }

    public void sendStart(){
        if (isConnectionActive())
            clientConnection.sendStart();
    }

    public void attachGame(Game game){
        this.game=game;
    }

    public void attachConnection(Connection connection){
        this.clientConnection=connection;
    }

    public void attachPlayer(Player player){
        this.player=player;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isConnectionActive(){
        return clientConnection.isActive();
    }

    public String getClientName(){
        return clientConnection.getClientName();
    }


    /**
     *Changes status to Player:
     * <ul>
     *     <li>Calls {@link Game#proceed()} if {@link Game#isSuspended()}</li>
     *     <li>Calls {@link #notifyAll()} in order to wake a possible thread waiting on game</li>
     * </ul>
     * @param status is the boolean representing the status that is needed to be set to the {@link Player}
     */
    public void changePlayerStatus(boolean status) {
        //player can change status after the lock on game is released
        //which means that game waits for some player's action
        //commutation between player active and not active happens in pauses
        //between player's actions or when game is suspended

        this.player.changeStatus(status);

        //if the game was suspended and the player is trying to reconnect then the game may unsuspend and continue
        if(game.isSuspended()){
            game.proceed();
            System.out.println("Virtual View says: game was suspended, now it is active");
        }

        //notify eventual thread that is waiting for client's action meanwhile he was disconnected
        //also notifies suspended game thread because this client gets reconnected
        game.notifyAll();

    }
}
