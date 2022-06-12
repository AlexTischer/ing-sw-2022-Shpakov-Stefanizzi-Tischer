package it.polimi.ingsw.client.model;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.packets.ActivateCharacter1Packet;
import it.polimi.ingsw.packets.ActivateCharacterPacket;
import it.polimi.ingsw.server.model.Color;

public class ClientCharacter11 extends ClientCharacter{
    @Override
    public ActivateCharacterPacket createPacket(View view){

        view.printMessage(getDescription());
        view.printMessage("Choose the color of the students you want to be returned to the bag");
        Color color = view.askStudentColor();

        ActivateCharacter1Packet packet = new ActivateCharacter1Packet(color);

        return packet;
    }
}
