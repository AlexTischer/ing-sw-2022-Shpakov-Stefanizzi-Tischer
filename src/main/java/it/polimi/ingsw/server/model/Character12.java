package it.polimi.ingsw.server.model;

import it.polimi.ingsw.client.model.ClientCharacter;
import it.polimi.ingsw.client.model.ClientCharacter12;
import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.exceptions.NoEnoughCoinsException;

/** resolves an island without moving mother nature*/
public class Character12 extends Character {

    private int id=12;
    protected int selectedIslandNumber;
    private int cost = 3;
    private String description = "Choose an Island and resolve it as if Mother Nature had ended her movement there";

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
        game.reassignIsland(selectedIslandNumber);
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
        cost = 4;
    }

    @Override
    public ClientCharacter createClientCharacter(){
        ClientCharacter12 character = new ClientCharacter12();

        character.setId(id);
        character.setCost(cost);
        character.setDescription(description);
        character.setFirstUse(firstUse);

        return character;
    }

}
