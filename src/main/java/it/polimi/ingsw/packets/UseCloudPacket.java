package it.polimi.ingsw.packets;

import it.polimi.ingsw.server.controller.GameForClient;

public class UseCloudPacket extends Packet{


    private int cloudNumber;

    public UseCloudPacket(int cloudNumber){
        this.cloudNumber=cloudNumber;
    }

    @Override
    public void execute(GameForClient game) {
        game.useCloud(cloudNumber);
    }
}
