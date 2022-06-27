package it.polimi.ingsw.client.model;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.packets.ActivateCharacter4Packet;
import it.polimi.ingsw.packets.ActivateCharacterPacket;
import it.polimi.ingsw.server.model.Color;

import java.util.ArrayList;
import java.util.Arrays;

public class ClientCharacter9 extends ClientCharacter{

    private Color[] students;

    @Override
    public ActivateCharacterPacket createPacket(View view){

        ActivateCharacter4Packet packet = null;
        ArrayList<Color> selectedStudents = new ArrayList<Color>();
        ArrayList<Color> toBeSwappedStudents = new ArrayList<Color>();
        ArrayList<Color> testStudentsFromCard = new ArrayList<>();
        ArrayList<Color> testStudentsFromEntrance = new ArrayList<>();
        for(Color s : students){
            testStudentsFromCard.add(s);
        }
        for (Color s : view.getController().getGameBoard().getPlayer(view.getController().getGameBoard().getClientName()).getSchoolBoard().getEntrance()){
            testStudentsFromEntrance.add(s);
        }
        //view.printMessage(getDescription());

        for (int i = 0; i < 3; i++) {
            boolean correctStudent = false;
            Color student = null;
            while (!correctStudent) {
                view.printMessage("Select the student from this card you want to swap");
                student = view.askStudentColorFromCharacter();
                if (testStudentsFromCard.contains(student)) {
                    correctStudent = true;
                    testStudentsFromCard.remove(student);
                } else {
                    view.printMessage("No such student on this card. Try again");
                }
            }
            selectedStudents.add(student);
            correctStudent = false;
            while (!correctStudent) {
                view.printMessage("Select the student from your entrance you want to swap");
                student = view.askStudentColor();
                if (testStudentsFromEntrance.contains(student)) {
                    correctStudent = true;
                    testStudentsFromEntrance.remove(student);
                } else {
                    view.printMessage("No such student in your entrance. Try again");
                }
            }

            toBeSwappedStudents.add(student);

            if(i<2){
                if(!view.askBoolean("Do you want to swap another student?")){
                    i=3;
                }
            }

        }

        packet = new ActivateCharacter4Packet(selectedStudents, toBeSwappedStudents);
        return packet;
    }

    public Color[] getStudents() {
        return students;
    }

    public void setStudents(Color[] students) {
        this.students = students;
    }
}