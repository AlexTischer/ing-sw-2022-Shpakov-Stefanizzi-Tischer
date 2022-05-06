package modelChange;

import client.model.ClientGameBoard;
import server.model.Color;
import server.model.Island;
import server.model.TowerColor;

import java.util.Map;

public class IslandChange extends ModelChange{

    private int islandNumber;
    private int numOfIslands;
    private Map<Color, Integer> students;
    private int numOfTowers;
    private TowerColor towersColor;
    private boolean noEntry;

    @Override
    public void execute(ClientGameBoard gameBoard){
        /*TODO gameBoard.getIsland(islandNumber).setSomeThing(this.someThing)*/
    }

    public IslandChange(Island island, int islandNumber){
        this.islandNumber=islandNumber;
        /*TODO this.someThing=island.getSomeThing*/
    }
}
