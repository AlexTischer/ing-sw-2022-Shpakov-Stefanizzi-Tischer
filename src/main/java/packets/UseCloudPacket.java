package packets;

import server.controller.GameForClient;
import server.model.Color;

public class UseCloudPacket extends Packet{

    @Override
    public void execute(GameForClient game) {
        int cloudNumber;

        try{
            cloudNumber = (int)getParameters().get(0);
        }
        catch (ClassCastException e){
            throw new ClassCastException("Invalid arguments");
        }

        game.useCloud(cloudNumber);
    }
}
