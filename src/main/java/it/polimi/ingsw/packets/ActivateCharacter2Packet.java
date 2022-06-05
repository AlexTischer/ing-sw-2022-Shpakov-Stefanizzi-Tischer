package it.polimi.ingsw.packets;

import it.polimi.ingsw.server.controller.GameForClient;
import it.polimi.ingsw.server.model.Color;

public class ActivateCharacter2Packet extends ActivateCharacterPacket{


    private Color color;
    private int islandNumber;


    public ActivateCharacter2Packet(Color color, int islandNumber){
        this.color=color;
        this.islandNumber=islandNumber;
    }

    @Override
    public void execute(GameForClient game) {

        game.activateCharacter(color, islandNumber);
    }
}
