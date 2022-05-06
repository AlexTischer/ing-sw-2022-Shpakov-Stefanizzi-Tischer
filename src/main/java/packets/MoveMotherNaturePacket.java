package packets;

import server.controller.GameForClient;
import server.model.Color;

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
