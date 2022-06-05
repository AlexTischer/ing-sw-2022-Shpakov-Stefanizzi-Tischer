package it.polimi.ingsw.client.model;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.packets.ActivateCharacter4Packet;
import it.polimi.ingsw.packets.ActivateCharacterPacket;
import it.polimi.ingsw.server.model.Color;

import java.util.ArrayList;

public class ClientCharacter7 extends ClientCharacter{
    private String description;

    @Override
    public ActivateCharacterPacket createPacket(View view){

        ArrayList<Color> selectedStudents = new ArrayList<Color>();
        ArrayList<Color> toBeSwappedStudents = new ArrayList<Color>();

        view.printMessage(description);

        for (int i = 0; i < 3; i++) {
            Color student = null;
            boolean correctStudent = false;
            while (!correctStudent) {
                view.printMessage("");//TODO add request
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
                view.printMessage("");//TODO add request
                student = view.askStudentColor();
                if (view.getController().getGameBoard().getPlayer(view.getController().getGameBoard().getClientName()).getSchoolBoard().getEntrance().contains(student)) {
                    correctStudent = true;
                } else {
                    view.printMessage("No such student on this card. Try again");
                }
            }
            toBeSwappedStudents.add(student);
        }

        ActivateCharacter4Packet packet = new ActivateCharacter4Packet(selectedStudents, toBeSwappedStudents);

        return packet;
    }
}
