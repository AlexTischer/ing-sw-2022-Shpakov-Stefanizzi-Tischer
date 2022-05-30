package modelChange;

import client.model.ClientGameBoard;

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