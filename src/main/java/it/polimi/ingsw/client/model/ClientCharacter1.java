package it.polimi.ingsw.client.model;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.packets.ActivateCharacter2Packet;
import it.polimi.ingsw.packets.ActivateCharacterPacket;
import it.polimi.ingsw.server.model.Color;

import java.util.Arrays;

public class ClientCharacter1 extends ClientCharacter{

    private String description;
    private Color[] students;

    @Override
    public ActivateCharacterPacket createPacket(View view){

        view.printMessage(description);
        Color student = null;
        boolean correctStudent = false;
        while(!correctStudent) {
            view.printMessage("");//TODO add request
            student = view.askStudentColor();
            if(Arrays.stream(students).toList().contains(student))
            {correctStudent=true;}
            else{
                view.printMessage("No such student on this card. Try again");
            }
        }
        view.printMessage("Insert island number you want to move the student to");
        int islandNumber = view.askIslandNumber();

        ActivateCharacter2Packet packet = new ActivateCharacter2Packet(student, islandNumber);

        return packet;
    }

}
