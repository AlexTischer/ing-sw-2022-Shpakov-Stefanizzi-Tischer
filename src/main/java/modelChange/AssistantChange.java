package modelChange;

import client.model.ClientGameBoard;
import server.model.Assistant;
import server.model.Player;

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
