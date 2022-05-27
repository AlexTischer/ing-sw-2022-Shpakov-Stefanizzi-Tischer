package server;

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
    public void update(ModelChange message) {
        clientConnection.send(message);
    }

    public void sendPacket(Packet packet) {
        /*send message to client*/

        //disconnect malicious client that tries to make an action when it`s not his turn
        //or sends exception back to the client in case of error
        if (player != game.getCurrentPlayer())
            clientConnection.close();
        else {
            try{
                game.usePacket(packet);
            }
            catch (WrongActionException e){
                clientConnection.close();
            }
            catch (Exception e){
                clientConnection.send(new ExceptionChange(e));
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
}
