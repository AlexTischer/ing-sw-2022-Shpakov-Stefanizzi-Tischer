package it.polimi.ingsw.client.model;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.packets.ActivateCharacter1Packet;
import it.polimi.ingsw.packets.ActivateCharacter2Packet;
import it.polimi.ingsw.packets.ActivateCharacterPacket;
import it.polimi.ingsw.server.model.Color;

import java.io.Serializable;

public class ClientCharacter4 extends ClientCharacter {
    private String description;

    @Override
    public ActivateCharacterPacket createPacket(View view){

        view.printMessage(description);
        view.printMessage("");//TODO add request
        Color color = view.askStudentColor();

        ActivateCharacter1Packet packet = new ActivateCharacter1Packet(color);

        return packet;
    }
}
