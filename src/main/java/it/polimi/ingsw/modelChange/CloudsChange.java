package it.polimi.ingsw.modelChange;

import it.polimi.ingsw.client.model.ClientCloud;
import it.polimi.ingsw.client.model.ClientGameBoard;
import it.polimi.ingsw.server.model.Cloud;

import java.util.ArrayList;
import java.util.List;

public class CloudsChange extends ModelChange{

    private List<ClientCloud> clientClouds = new ArrayList<ClientCloud>();

    @Override
    public void execute(ClientGameBoard gameBoard){
        gameBoard.setClouds(clientClouds);
    }

    public CloudsChange(List<Cloud> serverClouds){
        for(Cloud c : serverClouds){
            ClientCloud cloud = new ClientCloud();
            cloud.setStudents(c.getStudentsColors());
            clientClouds.add(cloud);
        }
    }
}
