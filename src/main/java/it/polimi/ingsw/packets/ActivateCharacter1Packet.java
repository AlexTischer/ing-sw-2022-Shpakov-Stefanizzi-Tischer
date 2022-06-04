package it.polimi.ingsw.packets;

import it.polimi.ingsw.server.controller.GameForClient;
import it.polimi.ingsw.server.model.Color;

public class ActivateCharacter1Packet extends ActivateCharacterPacket{

    @Override
    public void execute(GameForClient game) {
        Color color;

        try{
            color = (Color)getParameters().get(0);
        }
        catch (ClassCastException e){
            throw new ClassCastException("Invalid arguments");
        }

        game.activateCharacter(color);
    }
}
