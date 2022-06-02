package it.polimi.ingsw.modelChange;

import it.polimi.ingsw.client.model.ClientGameBoard;

public class ExceptionChange extends ModelChange{

    private RuntimeException e;

    @Override
    public void execute(ClientGameBoard gameBoard){
        throw e;
    }

    public ExceptionChange(RuntimeException e){
        this.e = e;
    }
}