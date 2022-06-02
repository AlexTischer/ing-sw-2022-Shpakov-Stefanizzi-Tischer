package it.polimi.ingsw.packets;

import it.polimi.ingsw.server.controller.GameForClient;

public class UseAssistantPacket extends Packet{

    private int assistantRank;
    public UseAssistantPacket(int assistantRank) {
        this.assistantRank = assistantRank;
    }

    @Override
    public void execute(GameForClient game) {
        game.useAssistant(assistantRank);
    }
}
