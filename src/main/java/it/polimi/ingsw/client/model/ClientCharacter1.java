package it.polimi.ingsw.client.model;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.packets.ActivateCharacter2Packet;
import it.polimi.ingsw.packets.ActivateCharacterPacket;
import it.polimi.ingsw.server.model.Color;

import java.util.Arrays;

public class ClientCharacter1 extends ClientCharacter{

    private Color[] students;

    @Override
    public ActivateCharacterPacket createPacket(View view){

        //view.printMessage(getDescription());
        Color student = null;
        boolean correctStudent = false;
        while(!correctStudent) {
            view.printMessage("From this card, select the student you want to move");

            //TEST
            System.out.println("ClientCharacter1: sto chiedendo di prendere lo studente sulla carta");
            student = view.askStudentColorFromCharacter();
            //TEST
            System.out.println("ho selezionato lo studente di colore " + student);
            if(Arrays.stream(students).toList().contains(student))
            {correctStudent=true;}
            else{
                view.printErrorMessage("No such student on this card. Try again");
            }
        }
        view.printMessage("Choose the island you want to move the student to");

        //TEST
        System.out.println("ora chiedo la destinazione isola");
        int islandNumber = view.askIslandNumber();

        ActivateCharacter2Packet packet = new ActivateCharacter2Packet(student, islandNumber-1);

        return packet;
    }

    public void setStudents(Color[] students) {
        this.students = students;
    }
    public Color[] getStudents() {
        return students;
    }

}
