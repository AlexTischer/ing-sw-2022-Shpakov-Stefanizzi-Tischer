package server;

import exceptions.RepeatedAssistantRankException;
import exceptions.WrongActionException;
import modelChange.ExceptionChange;
import modelChange.ModelChange;
import packets.Packet;
import server.controller.Game;
import server.model.Player;
import utils.Observer;

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
        System.out.println("My player is " + player.getName() + "\nCurrent player is " + game.getCurrentPlayer().getName());

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

    public boolean isActive(){
        return clientConnection.isActive();
    }

    public String getClientName(){
        return clientConnection.getClientName();
    }
}
