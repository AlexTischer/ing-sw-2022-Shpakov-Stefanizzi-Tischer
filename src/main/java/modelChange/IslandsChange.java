package modelChange;

import client.model.ClientGameBoard;
import server.model.Island;

import java.util.List;

public class IslandsChange extends ModelChange{

    private List<Island> islands;

    @Override
    public void execute(ClientGameBoard gameBoard){
        gameBoard.setIslands(islands);
    }

    public IslandsChange(List<Island> islands){
        this.islands=islands;
    }
}
