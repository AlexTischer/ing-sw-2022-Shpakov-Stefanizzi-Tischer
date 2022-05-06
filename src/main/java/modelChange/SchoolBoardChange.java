package modelChange;

import client.model.ClientGameBoard;
import server.model.*;

import java.util.ArrayList;
import java.util.Map;

public class SchoolBoardChange extends ModelChange{

    private String playerName;
    private ArrayList<Color> entrance;
    private Map<Color,Integer> diningRoom;
    private Map<Color,Integer> professors;
    private int numOfTowers;
    private TowerColor towersColor;

    @Override
    public void execute(ClientGameBoard gameBoard){
        /*TODO gameBoard.getPlayer(Player.getName()).getSchoolBoard.setSomeThing(this.someThing)*/
    }

    public SchoolBoardChange(Player player){
        playerName=player.getName();
        /*TODO this.someThing=player.getSomeThing*/
    }
}
