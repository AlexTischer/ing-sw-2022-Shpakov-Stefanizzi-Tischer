package modelChange;

import client.model.ClientCloud;
import client.model.ClientGameBoard;
import server.model.Cloud;
import server.model.Color;
import java.util.ArrayList;
import java.util.List;

public class CloudChange extends ModelChange{

    private int cloudNumber;
    private ArrayList<Color> students;


    @Override
    public void execute(ClientGameBoard gameBoard){
        ClientCloud cloud = gameBoard.getCloud(this.cloudNumber);
        cloud.setStudents(this.students);
    }

    public CloudChange(Cloud cloud, int cloudNumber){
        this.cloudNumber=cloudNumber;
        this.students = cloud.getStudentsColors();
    }
}
