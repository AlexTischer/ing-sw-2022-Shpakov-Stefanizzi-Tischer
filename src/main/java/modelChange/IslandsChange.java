package modelChange;

import client.model.ClientGameBoard;
import client.model.ClientIsland;
import server.model.Island;

import java.util.List;

public class IslandsChange extends ModelChange{

    private List<ClientIsland> islands;

    @Override
    public void execute(ClientGameBoard gameBoard){
        gameBoard.setIslands(islands);
    }

    public IslandsChange(List<Island> islands){
        for(Island i : islands){
            ClientIsland clientIsland = new ClientIsland();
            clientIsland.setNumOfTowers(i.getNumOfTowers());

            this.islands.add(clientIsland);
        }
    }
}
