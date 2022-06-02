package it.polimi.ingsw.modelChange;

import it.polimi.ingsw.client.model.ClientGameBoard;

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
