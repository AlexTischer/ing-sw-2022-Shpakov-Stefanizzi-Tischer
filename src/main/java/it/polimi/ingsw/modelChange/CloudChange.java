package it.polimi.ingsw.modelChange;

import it.polimi.ingsw.client.model.ClientCloud;
import it.polimi.ingsw.client.model.ClientGameBoard;
import it.polimi.ingsw.server.model.Cloud;
import it.polimi.ingsw.server.model.Color;

import java.util.ArrayList;

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
