package it.polimi.ingsw.packets;

import it.polimi.ingsw.server.controller.GameForClient;
import it.polimi.ingsw.server.model.Color;

public class ActivateCharacter1Packet extends ActivateCharacterPacket{

    private Color color;

    public ActivateCharacter1Packet(Color color){
        this.color=color;
    }

    @Override
    public void execute(GameForClient game) {

        game.activateCharacter(color);
    }
}
