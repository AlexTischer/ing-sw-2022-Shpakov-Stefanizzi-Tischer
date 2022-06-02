package it.polimi.ingsw.modelChange;

import it.polimi.ingsw.client.model.ClientGameBoard;

import java.util.List;

/*gets used when client gets (dis)connected from addToLobby*/
public class LobbyChange extends ModelChange{
    private List<String> userNames;


    @Override
    public void execute(ClientGameBoard gameBoard){
        gameBoard.setUserNames(userNames);
    }

    public LobbyChange(List<String> userNames) {
        this.userNames = userNames;
    }
}
