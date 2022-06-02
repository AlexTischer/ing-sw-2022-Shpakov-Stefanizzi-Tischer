package it.polimi.ingsw.packets;

import it.polimi.ingsw.server.controller.GameForClient;

public class ActivateCharacter3Packet extends Packet{

    @Override
    public void execute(GameForClient game) {
        int islandNumber;

        try{
            islandNumber = (int)getParameters().get(0);
        }
        catch (ClassCastException e){
            throw new ClassCastException("Invalid arguments");
        }

        game.activateCharacter(islandNumber);
    }
}
