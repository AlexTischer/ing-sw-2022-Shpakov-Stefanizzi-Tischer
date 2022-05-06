package modelChange;

import client.model.ClientGameBoard;
import server.model.Cloud;
import server.model.Color;

import java.util.ArrayList;

public class MotherNatureChange extends ModelChange{

    private int positionOfMotherNature;

    @Override
    public void execute(ClientGameBoard gameBoard){
        gameBoard.setPositionOfMotherNature(positionOfMotherNature);
    }

    public MotherNatureChange(int positionOfMotherNature){
        this.positionOfMotherNature=positionOfMotherNature;
    }
}
