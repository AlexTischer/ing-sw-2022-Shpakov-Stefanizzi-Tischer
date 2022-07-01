package it.polimi.ingsw.server.model;

import it.polimi.ingsw.client.model.ClientCharacter;
import it.polimi.ingsw.client.model.ClientCharacter5;
import it.polimi.ingsw.exceptions.NoEnoughCoinsException;
import it.polimi.ingsw.exceptions.NoEnoughEntryTilesException;
import it.polimi.ingsw.server.controller.Game;

/**sets noEntry tiles on islands*/
public class Character5 extends Character {

    private int id = 5;
    private int noEntryTiles;
    protected int selectedIslandNumber;
    private int cost = 2;
    private String description  = "Place a No Entry tile on an island of your choice. The first time Mother Nature ends her movement there, influence will not be calculated.";
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
        game.getGameBoard().removeCoinsFromPlayer(game.getCurrentPlayer(), cost);
        //if it's first use then we need to leave one coin on the card
        if (firstUse){
            game.getGameBoard().addCoinsToBank(cost-1);
            firstUse = false;
        }
        else {
            game.getGameBoard().addCoinsToBank(cost);
        }
        cost = 3;
    }


    @Override
    public ClientCharacter createClientCharacter(){
        ClientCharacter5 character = new ClientCharacter5();

        character.setId(id);
        character.setCost(cost);
        character.setDescription(description);
        character.setFirstUse(firstUse);
        character.setNoEntryTiles(noEntryTiles);;

        return character;
    }


}
