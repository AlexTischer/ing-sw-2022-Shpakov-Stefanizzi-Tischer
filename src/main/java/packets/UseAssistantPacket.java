package packets;

import server.controller.GameForClient;

public class UseAssistantPacket extends Packet{

    @Override
    public void execute(GameForClient game) {
        int assistantRank;

        try{
            assistantRank = (int)getParameters().get(0);
        }
        catch (ClassCastException e){
            throw new ClassCastException("Invalid arguments");
        }

        // TODO game.useAssistant(assistantRank, !!PLAYER!!);
    }
}
