package it.polimi.ingsw.modelChange;

import it.polimi.ingsw.client.model.ClientGameBoard;

public class CoinsOfGameBoardChange extends ModelChange{

    private int numOfCoins;

    @Override
    public void execute(ClientGameBoard gameBoard){
        gameBoard.setNumOfCoins(numOfCoins);
    }

    public CoinsOfGameBoardChange(int numOfCoins){
        this.numOfCoins=numOfCoins;
    }
}
