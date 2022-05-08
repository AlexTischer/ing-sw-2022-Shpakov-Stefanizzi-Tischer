package packets;

import server.controller.GameForClient;
import server.model.Color;

import java.util.ArrayList;

public class ActivateCharacter4Packet extends Packet{

    @Override
    public void execute(GameForClient game) {
        ArrayList<Color> toBeSwappedStudents;
        ArrayList<Color> selectedStudents;

        try{
            toBeSwappedStudents = (ArrayList<Color>)getParameters().get(0);
            selectedStudents = (ArrayList<Color>)getParameters().get(1);
        }
        catch (ClassCastException e){
            throw new ClassCastException("Invalid arguments");
        }

        game.activateCharacter(toBeSwappedStudents, selectedStudents);
    }
}
