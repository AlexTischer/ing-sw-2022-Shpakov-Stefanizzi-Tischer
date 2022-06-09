package it.polimi.ingsw.server;

import it.polimi.ingsw.exceptions.WrongActionException;
import it.polimi.ingsw.modelChange.ExceptionChange;
import it.polimi.ingsw.modelChange.ModelChange;
import it.polimi.ingsw.packets.Packet;
import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.utils.Observer;

public class VirtualView implements Observer<ModelChange> {

    private Game game;
    private Player player;
    private Connection clientConnection;

    @Override
    public void update(ModelChange modelChange) {
        clientConnection.send(modelChange);
    }

    public void sendPacket(Packet packet) {
        /*send message to client*/
        System.out.println("Virtual View says: I am in sendPacket(). I have received " + packet.getClass());
        System.out.println("My player is " + player.getName() + "Current player is " + game.getCurrentPlayer().getName());

        //sends exception back to the client in case of error
        if (!player.getName().equals(game.getCurrentPlayer().getName())) {
            System.out.println(player.getName() + " is not current player");
            //if client tries to act while it's not his turn, then it is the wrong action
            clientConnection.send(new ExceptionChange(new WrongActionException()));
        }
        else {
            try{
                game.usePacket(packet);
            }
            catch (RuntimeException e){
                //any exception related to incorrect user action, sends an exception back to client
                clientConnection.send(new ExceptionChange(e));
                //exception must not be followed by endOfChanges because user still needs to make the correct move
            }
        }
    }

    public void sendStart(){
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

    public void changePlayerStatus(boolean status) {
        //player will become active only after the lock on game is released
        //which means that game waits for some player's action
        //commutation between player active and not active happens in pauses between player's actions
        synchronized (game) {
            this.player.changeStatus(status);

            //notify eventual thread that is waiting for client's action meanwhile he was disconnected
            game.notifyAll();
        }
    }
}
