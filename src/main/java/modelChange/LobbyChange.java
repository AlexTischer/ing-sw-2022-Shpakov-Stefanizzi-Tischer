package modelChange;

import client.model.ClientGameBoard;

import java.util.ArrayList;
import java.util.List;

/*gets used when client gets (dis)connected from lobby*/
public class LobbyChange extends ModelChange{
    private List<String> userNames;


    @Override
    public void execute(ClientGameBoard gameBoard) throws Exception {
        gameBoard.setUserNames(userNames);
    }

    public LobbyChange(List<String> userNames) {
        this.userNames = userNames;
    }
}
