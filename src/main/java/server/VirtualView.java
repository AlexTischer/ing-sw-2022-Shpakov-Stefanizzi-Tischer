package server;

import exceptions.WrongActionException;
import modelChange.ExceptionChange;
import modelChange.ModelChange;
import packets.Packet;
import server.controller.Game;
import server.model.Player;
import utils.Observer;

public class VirtualView implements Runnable, Observer<ModelChange> {

    private Game game;
    private Player player;
    private Connection clientConnection;
    @Override
    public void run() {

    }

    @Override
    public void update(ModelChange message) {

    }

    public void sendPacket(Packet packet) {
        //disconnect malicious client that tries make an action when it`s not his turn
        if (player != game.getCurrentPlayer())
            clientConnection.close();
        else {
            try{
                game.usePacket(packet);
            }
            catch (WrongActionException e){
                clientConnection.close();
            }
            catch (IllegalArgumentException e){
                clientConnection.send(new ExceptionChange(e));
            }
        }

            /*send message to client*/
    }
}
