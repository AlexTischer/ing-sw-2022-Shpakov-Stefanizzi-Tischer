package packets;

import server.controller.GameForClient;
import server.model.Color;

public class ActivateCharacter3Packet /*TODO change name*/ extends Packet{

    @Override
    public void execute(GameForClient game) {
        int islandNumber;

        try{
            islandNumber = (int)getParameters().get(0);
        }
        catch (ClassCastException e){
            throw new ClassCastException("Invalid arguments");
        }

        game.activateCharacter(islandNumber);
    }
}
