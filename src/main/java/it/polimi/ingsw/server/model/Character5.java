package it.polimi.ingsw.server.model;

import it.polimi.ingsw.client.model.ClientCharacter;
import it.polimi.ingsw.client.model.ClientCharacter5;
import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.exceptions.NoEnoughCoinsException;
import it.polimi.ingsw.exceptions.NoEnoughEntryTilesException;

/**sets noEntry tiles on islands*/
public class Character5 extends Character {
    private int noEntryTiles;
    protected int selectedIslandNumber;
    private int cost = 2;
    private String description  = "C5";
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
    public void execute() throws NoEnoughEntryTilesException {
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
    public int getNoEntryTiles(){
        return noEntryTiles;
    }

    @Override
    public void buy() throws NoEnoughCoinsException {
        game.getGameBoard().removeCoins(game.getCurrentPlayer(), cost);
        cost = 3;
    }


    @Override
    public ClientCharacter createClientCharacter(){
        ClientCharacter character = new ClientCharacter5();

        character.setCost(cost);
        character.setDescription(description);

        character.setNoEntryTiles(noEntryTiles);;

        return character;
    }


}
