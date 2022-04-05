package model;


import exceptions.NoEnoughCoinsException;

/** adds 2 to maximum number of steps */
public class Character8 extends Character{

    private int cost = 1;

    @Override
    public boolean moveMotherNature(int steps){
        return (game.getCurrentPlayer().getPlayedAssistant().getMovements()+2>=steps);
    }

    @Override
    public void buy() throws NoEnoughCoinsException {
        game.getCurrentPlayer().removeCoins(cost);
        cost = 2;
    }

}
