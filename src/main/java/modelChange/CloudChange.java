package modelChange;

import client.model.ClientGameBoard;
import server.model.Cloud;
import server.model.Color;
import java.util.ArrayList;

public class CloudChange extends ModelChange{

    private int cloudNumber;
    private ArrayList<Color> students;


    @Override
    public void execute(ClientGameBoard gameBoard){
        /*TODO gameBoard.getCloud(cloudNumber).setSomeThing(this.someThing)*/
    }

    public CloudChange(Cloud cloud, int cloudNumber){
        this.cloudNumber=cloudNumber;
        /*TODO this.someThing=island.getSomeThing*/
    }
}
