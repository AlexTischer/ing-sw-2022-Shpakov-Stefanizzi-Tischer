package it.polimi.ingsw.client.model;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.packets.ActivateCharacter4Packet;
import it.polimi.ingsw.packets.ActivateCharacterPacket;
import it.polimi.ingsw.server.model.Color;

import java.util.ArrayList;

public class ClientCharacter7 extends ClientCharacter{
    @Override
    public ActivateCharacterPacket createPacket(View view){

        ArrayList<Color> selectedStudents = new ArrayList<Color>();
        ArrayList<Color> toBeSwappedStudents = new ArrayList<Color>();

        view.printMessage(getDescription());

        for (int i = 0; i < 2; i++) {
            Color student = null;
            boolean correctStudent = false;
            while (!correctStudent) {
                view.printMessage("Select the student from your dining room you want to swap");
                student = view.askStudentColor();
                if (view.getController().getGameBoard().getPlayer(view.getController().getGameBoard().getClientName()).getSchoolBoard().getDiningRoom().get(student)>0) {
                    correctStudent = true;
                } else {
                    view.printMessage("No such student on this card. Try again");
                }
            }
            selectedStudents.add(student);

            correctStudent = false;
            while (!correctStudent) {
                view.printMessage("Select the student from your entrance you want to swap");
                student = view.askStudentColor();
                if (view.getController().getGameBoard().getPlayer(view.getController().getGameBoard().getClientName()).getSchoolBoard().getEntrance().contains(student)) {
                    correctStudent = true;
                } else {
                    view.printMessage("No such student on this card. Try again");
                }
            }
            toBeSwappedStudents.add(student);

            if(i<1){
                if(!view.askBoolean("Do you want to swap another student?")){
                    i=2;
                }
            }
        }

        ActivateCharacter4Packet packet = new ActivateCharacter4Packet(selectedStudents, toBeSwappedStudents);

        return packet;
    }
}
