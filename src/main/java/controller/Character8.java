package controller;


import exceptions.NoEnoughCoinsException;

/** adds 2 to maximum number of steps */
public class Character8 extends Character{

    private int cost = 1;

    @Override
    public boolean moveMotherNature(int steps){
        return (game.currentPlayer.getPlayedAssistant().getMovements()+2>=steps);
    }

    @Override
    public void buy() throws NoEnoughCoinsException {
        game.currentPlayer.removeCoins(cost);
        cost = 2;
    }

}
