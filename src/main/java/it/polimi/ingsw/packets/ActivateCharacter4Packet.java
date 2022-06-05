package it.polimi.ingsw.packets;

import it.polimi.ingsw.server.controller.GameForClient;
import it.polimi.ingsw.server.model.Color;

import java.util.ArrayList;

public class ActivateCharacter4Packet extends ActivateCharacterPacket{

    ArrayList<Color> toBeSwappedStudents;
    ArrayList<Color> selectedStudents;


    public ActivateCharacter4Packet(ArrayList<Color> selectedStudents, ArrayList<Color> toBeSwappedStudents){
        this.selectedStudents=selectedStudents;
        this.toBeSwappedStudents=toBeSwappedStudents;
    }
    @Override
    public void execute(GameForClient game) {
        game.activateCharacter(toBeSwappedStudents, selectedStudents);
    }
}
