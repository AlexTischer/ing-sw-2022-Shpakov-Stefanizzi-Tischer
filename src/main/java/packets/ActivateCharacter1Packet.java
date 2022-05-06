package packets;

import server.controller.GameForClient;
import server.model.Color;

public class ActivateCharacter1Packet /*TODO change name*/ extends Packet{

    @Override
    public void execute(GameForClient game) {
        Color color;

        try{
            color = (Color)getParameters().get(0);
        }
        catch (ClassCastException e){
            throw new ClassCastException("Invalid arguments");
        }

        game.activateCharacter(color);
    }
}
