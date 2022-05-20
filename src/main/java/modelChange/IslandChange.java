package modelChange;

import client.model.ClientGameBoard;
import client.model.ClientIsland;
import server.model.Color;
import server.model.Island;
import server.model.TowerColor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IslandChange extends ModelChange{

    private int islandNumber;
    private int numOfIslands;
    private Map<Color, Integer> students = new HashMap<Color, Integer>();
    private int numOfTowers;
    private TowerColor towersColor;
    private boolean noEntry;

    @Override
    public void execute(ClientGameBoard gameBoard){
        List<ClientIsland> islands = gameBoard.getIslands();
        islands.get(islandNumber).setNumOfIslands(this.numOfIslands);
        islands.get(islandNumber).setNumOfTowers(this.numOfTowers);
        islands.get(islandNumber).setTowersColor(this.towersColor);
        islands.get(islandNumber).setNoEntry(this.noEntry);
        islands.get(islandNumber).setStudents(this.students);
    }

    public IslandChange(Island island, int islandNumber){
        this.islandNumber = islandNumber;
        this.numOfIslands = island.getNumOfIslands();
        this.numOfTowers = island.getNumOfTowers();
        this.towersColor = island.getTowersColor();
        this.noEntry = island.getNoEntry();
        for(Color c : Color.values()){
            this.students.put(c,island.getNumOfStudents(c));
        }
    }
}
