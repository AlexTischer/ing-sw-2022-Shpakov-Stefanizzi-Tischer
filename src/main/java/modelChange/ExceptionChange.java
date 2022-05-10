package modelChange;

import client.model.ClientGameBoard;

public class ExceptionChange extends ModelChange{

    private Exception e;

    @Override
    public void execute(ClientGameBoard gameBoard) throws Exception {
        throw e;
    }

    public ExceptionChange(Exception e){
        this.e = e;
    }
}