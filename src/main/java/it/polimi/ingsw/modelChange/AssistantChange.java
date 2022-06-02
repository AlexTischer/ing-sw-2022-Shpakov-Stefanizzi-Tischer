package it.polimi.ingsw.modelChange;

import it.polimi.ingsw.client.model.ClientGameBoard;
import it.polimi.ingsw.server.model.Assistant;
import it.polimi.ingsw.server.model.Player;

public class AssistantChange extends ModelChange{

    private String playerName;
    private Assistant assistant;


    @Override
    public void execute(ClientGameBoard gameBoard){
        gameBoard.getPlayer(playerName).setPlayedAssistant(assistant);
    }

    public AssistantChange(Player player){
        this.assistant = player.getPlayedAssistant();
        playerName = player.getName();
    }
}
