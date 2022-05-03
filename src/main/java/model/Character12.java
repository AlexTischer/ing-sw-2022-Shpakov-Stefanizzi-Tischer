package model;

import controller.Game;
import exceptions.NoEnoughCoinsException;

/** calculate influence without moving mother nature*/
public class Character12 extends Character{
    protected int selectedIslandNumber;
    private int cost = 3;

    @Override
    public void initialFill(Game game){
        super.initialFill(game);
    }

    @Override
    public void setSelectedIslandNumber(int selectedIslandNumber) {
        this.selectedIslandNumber = selectedIslandNumber;
    }

    @Override
    public void execute(){
        game.calculateInfluence(selectedIslandNumber);
    }
    
    @Override
    public void buy() throws NoEnoughCoinsException {
        game.getGameBoard().removeCoins(game.getCurrentPlayer(), cost);
        cost = 4;
    }

}
