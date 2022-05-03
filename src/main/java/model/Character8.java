package model;

import controller.Game;
import exceptions.NoEnoughCoinsException;

/** adds 2 to maximum number of steps */
public class Character8 extends Character{

    private int cost = 1;

    @Override
    public void initialFill(Game game){
        super.initialFill(game);
    }

    @Override
    public boolean moveMotherNature(int steps){
        return (game.getCurrentPlayer().getPlayedAssistant().getMovements()+2>=steps);
    }

    @Override
    public void buy() throws NoEnoughCoinsException {
        game.getGameBoard().removeCoins(game.getCurrentPlayer(), cost);
        cost = 2;
    }

}
