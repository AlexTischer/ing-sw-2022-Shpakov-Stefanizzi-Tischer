package it.polimi.ingsw.packets;

import it.polimi.ingsw.server.controller.GameForClient;
import it.polimi.ingsw.server.model.Color;

public class BuyCharacterPacket extends Packet{

    private int characterNumber;


    public BuyCharacterPacket(int characterNumber){
        this.characterNumber=characterNumber;
    }

    @Override
    public void execute(GameForClient game) {

        game.buyCharacter(characterNumber);
    }
}
