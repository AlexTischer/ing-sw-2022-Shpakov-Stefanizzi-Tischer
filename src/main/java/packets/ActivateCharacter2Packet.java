package packets;

import server.controller.GameForClient;
import server.model.Color;

public class ActivateCharacter2Packet extends Packet{

    @Override
    public void execute(GameForClient game) {
        Color color;
        int islandNumber;

        try{
            color = (Color)getParameters().get(0);
            islandNumber = (int)getParameters().get(1);
        }
        catch (ClassCastException e){
            throw new ClassCastException("Invalid arguments");
        }

        game.activateCharacter(color, islandNumber);
    }
}
