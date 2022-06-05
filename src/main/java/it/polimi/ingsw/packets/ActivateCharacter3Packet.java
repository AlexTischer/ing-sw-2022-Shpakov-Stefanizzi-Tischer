package it.polimi.ingsw.packets;

import it.polimi.ingsw.server.controller.GameForClient;


public class ActivateCharacter3Packet extends ActivateCharacterPacket{


    private int islandNumber;

    public ActivateCharacter3Packet(int islandNumber){
        this.islandNumber=islandNumber;
    }

    @Override
    public void execute(GameForClient game) {

        game.activateCharacter(islandNumber);
    }
}
