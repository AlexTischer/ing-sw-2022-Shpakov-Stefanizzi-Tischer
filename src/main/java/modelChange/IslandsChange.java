package modelChange;

import client.model.ClientGameBoard;
import client.model.ClientIsland;
import server.model.Color;
import server.model.Island;
import java.util.List;
import java.util.Map;

public class IslandsChange extends ModelChange{

    private List<ClientIsland> islands;

    @Override
    public void execute(ClientGameBoard gameBoard){
        gameBoard.setIslands(islands);
    }

    public IslandsChange(List<Island> islands){
        for(Island i : islands){
            ClientIsland clientIsland = new ClientIsland();
            clientIsland.setNumOfIslands(i.getNumOfIslands());
            Map<Color, Integer> students = null;
            for(Color c : Color.values()){
                students.put(c, i.getNumOfStudents(c));
            }
            clientIsland.setStudents(students);
            clientIsland.setNumOfTowers(i.getNumOfTowers());
            clientIsland.setTowersColor(i.getTowersColor());
            clientIsland.setNoEntry(i.getNoEntry());
            this.islands.add(clientIsland);
        }
    }
}
