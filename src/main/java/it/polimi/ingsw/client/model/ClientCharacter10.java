package it.polimi.ingsw.client.model;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.packets.ActivateCharacter1Packet;
import it.polimi.ingsw.packets.ActivateCharacterPacket;
import it.polimi.ingsw.server.model.Color;

import java.util.Arrays;

public class ClientCharacter10 extends ClientCharacter{

    @Override
    public ActivateCharacterPacket createPacket(View view){

        view.printMessage(getDescription());
        Color student = null;
        boolean correctStudent = false;
        while(!correctStudent) {
            view.printMessage("From this card, choose the student you want to move to your dining room");
            student = view.askStudentColor();
            if(Arrays.stream(getStudents()).toList().contains(student))
            {correctStudent=true;}
            else{
                view.printMessage("No such student on this card. Try again");
            }
        }
        ActivateCharacter1Packet packet = new ActivateCharacter1Packet(student);

        return packet;
    }
}
