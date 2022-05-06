package modelChange;

import client.model.ClientGameBoard;
import server.model.*;

public class PlayerChange extends ModelChange{

    private String name;
    private SchoolBoard schoolBoard;
    private int coins;
    private TowerColor towerColor;
    private AssistantType assistantType;
    private Assistant playedAssistant;
    private Assistant[] assistants;

    @Override
    public void execute(ClientGameBoard gameBoard){
        /*TODO gameBoard.getPlayer(Player.getName()).setSomeThing(this.someThing)*/
    }

    public PlayerChange(Player player){
        /*TODO this.someThing=player.getSomeThing*/
    }
}
