package it.polimi.ingsw.packets;

import it.polimi.ingsw.server.controller.GameForClient;
import it.polimi.ingsw.server.model.Color;

public class MoveStudentToIslandPacket extends Packet{

    private Color studentColor;
    private int islandNumber;

    public MoveStudentToIslandPacket(Color studentColor, int islandNumber){
        this.studentColor=studentColor;
        this.islandNumber=islandNumber;
    }
    @Override
    public void execute(GameForClient game) {
        game.moveStudentToIsland(studentColor, islandNumber);
    }
}
