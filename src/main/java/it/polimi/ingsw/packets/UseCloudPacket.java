package it.polimi.ingsw.packets;

import it.polimi.ingsw.server.controller.GameForClient;

public class UseCloudPacket extends Packet{

    @Override
    public void execute(GameForClient game) {
        int cloudNumber;

        try{
            cloudNumber = (int)getParameters().get(0);
        }
        catch (ClassCastException e){
            throw new ClassCastException("Invalid arguments");
        }

        game.useCloud(cloudNumber);
    }
}
