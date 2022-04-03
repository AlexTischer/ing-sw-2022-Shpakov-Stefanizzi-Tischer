package controller;

import exceptions.NoEnoughEntryTilesException;
import exceptions.NoEntryException;

public class Character5 extends Character{
    private int noEntryTiles;
    protected int selectedIslandNumber;

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

    public void addNoEntryTile(){
        noEntryTiles++;
    }



}
