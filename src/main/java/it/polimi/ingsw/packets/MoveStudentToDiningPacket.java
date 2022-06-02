package it.polimi.ingsw.packets;

import it.polimi.ingsw.server.controller.GameForClient;
import it.polimi.ingsw.server.model.Color;

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
