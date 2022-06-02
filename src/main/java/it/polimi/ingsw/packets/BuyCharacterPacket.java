package it.polimi.ingsw.packets;

import it.polimi.ingsw.server.controller.GameForClient;

public class BuyCharacterPacket extends Packet{

    @Override
    public void execute(GameForClient game) {
        int characterNumber;

        try{
            characterNumber = (int)getParameters().get(0);
        }
        catch (ClassCastException e){
            throw new ClassCastException("Invalid arguments");
        }

        game.buyCharacter(characterNumber);
    }
}
