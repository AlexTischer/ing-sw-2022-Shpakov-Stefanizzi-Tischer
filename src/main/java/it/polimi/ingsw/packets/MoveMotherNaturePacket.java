package it.polimi.ingsw.packets;

import it.polimi.ingsw.server.controller.GameForClient;

public class MoveMotherNaturePacket extends Packet{

    @Override
    public void execute(GameForClient game) {

        int steps;

        try{
            steps = (int)getParameters().get(0);
        }
        catch (ClassCastException e){
            throw new ClassCastException("Invalid arguments");
        }

        game.moveMotherNature(steps);
    }
}
