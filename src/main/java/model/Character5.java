package model;

import controller.Game;
import exceptions.NoEnoughCoinsException;
import exceptions.NoEnoughEntryTilesException;

public class Character5 extends Character{
    private int noEntryTiles;
    protected int selectedIslandNumber;
    private int cost = 2;

    @Override
    public void initialFill(Game game){
        super.initialFill(game);
        noEntryTiles = 4;
    }


    @Override
    public void setSelectedIslandNumber(int selectedIslandNumber){
        this.selectedIslandNumber = selectedIslandNumber;
    }

    @Override
    public void execute() throws NoEnoughEntryTilesException{
        if(noEntryTiles>0) {
            game.setNoEntry(selectedIslandNumber, true);
            noEntryTiles--;
        }
        else throw new NoEnoughEntryTilesException();
    }

    @Override
    public void addNoEntryTile(){
        noEntryTiles++;
    }

    @Override
    public void buy() throws NoEnoughCoinsException {
        game.getGameBoard().removeCoins(game.getCurrentPlayer(), cost);
        cost = 3;
    }



}
