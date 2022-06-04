package it.polimi.ingsw.packets;

import it.polimi.ingsw.server.controller.GameForClient;

public class MoveMotherNaturePacket extends Packet{

    private int steps;

    public MoveMotherNaturePacket(int steps){
        this.steps=steps;
    }
    @Override
    public void execute(GameForClient game) {
        game.moveMotherNature(steps);
    }
}
