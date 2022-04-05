package model;

import exceptions.NoEnoughCoinsException;

/** calculate influence without moving mother nature*/
public class Character12 extends Character{
    protected int selectedIslandNumber;
    private int cost = 3;

    @Override
    public void setSelectedIslandNumber(int selectedIslandNumber) {
        this.selectedIslandNumber = selectedIslandNumber;
    }

    public void execute(){
        game.calculateInfluence(selectedIslandNumber);
    }
    
    @Override
    public void buy() throws NoEnoughCoinsException {
        game.getCurrentPlayer().removeCoins(cost);
        cost = 4;
    }

}
