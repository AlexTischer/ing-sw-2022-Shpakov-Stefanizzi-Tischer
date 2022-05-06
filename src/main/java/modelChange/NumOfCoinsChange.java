package modelChange;

import client.model.ClientGameBoard;

public class NumOfCoinsChange extends ModelChange{

    private int numOfCoins;

    @Override
    public void execute(ClientGameBoard gameBoard){
        gameBoard.setNumOfCoins(numOfCoins);
    }

    public NumOfCoinsChange(int numOfCoins){
        this.numOfCoins=numOfCoins;
    }
}
