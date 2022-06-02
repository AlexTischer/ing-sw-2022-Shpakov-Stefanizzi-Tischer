package it.polimi.ingsw.modelChange;

import it.polimi.ingsw.client.model.ClientGameBoard;
import it.polimi.ingsw.server.model.Player;

public class CoinsOfPlayerChange extends ModelChange{

    private int numOfCoins;
    private String playerName;


    @Override
    public void execute(ClientGameBoard gameBoard){
        gameBoard.getPlayer(playerName).setCoins(numOfCoins);
    }

    public CoinsOfPlayerChange(Player player){
        this.numOfCoins = player.getCoins();
        playerName = player.getName();
    }
}
