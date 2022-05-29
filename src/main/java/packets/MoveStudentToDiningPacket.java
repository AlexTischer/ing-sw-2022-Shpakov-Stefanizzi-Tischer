package packets;

import server.controller.GameForClient;
import server.model.Color;

public class MoveStudentToDiningPacket extends Packet{

    private Color studentColor;

    public MoveStudentToDiningPacket(Color studentColor){
        this.studentColor=studentColor;
    }
    @Override
    public void execute(GameForClient game) {
        game.moveStudentToDining(studentColor);
    }
}
