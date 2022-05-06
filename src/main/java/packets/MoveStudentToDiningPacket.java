package packets;

import server.controller.GameForClient;
import server.model.Color;

public class MoveStudentToDiningPacket extends Packet{

    @Override
    public void execute(GameForClient game) {

        Color studentColor;

        try{
            studentColor = (Color)getParameters().get(0);
        }
        catch (ClassCastException e){
            throw new ClassCastException("Invalid arguments");
        }

        game.moveStudentToDining(studentColor);
    }
}
