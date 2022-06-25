package it.polimi.ingsw.client.model;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.packets.ActivateCharacter3Packet;
import it.polimi.ingsw.packets.ActivateCharacterPacket;

public class ClientCharacter12 extends ClientCharacter{
    @Override
    public ActivateCharacterPacket createPacket(View view){

        //view.printMessage(getDescription());
        view.printMessage("Insert island number you want to calculate influence in");
        int islandNumber = view.askIslandNumber();

        ActivateCharacter3Packet packet = new ActivateCharacter3Packet(islandNumber-1);

        return packet;
    }
}
