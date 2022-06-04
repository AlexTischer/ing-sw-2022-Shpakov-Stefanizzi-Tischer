package it.polimi.ingsw.server.model;

import it.polimi.ingsw.client.model.ClientCharacter;
import it.polimi.ingsw.client.model.ClientCharacter12;
import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.exceptions.NoEnoughCoinsException;

/** resolves an island without moving mother nature*/
public class Character12 extends Character {
    protected int selectedIslandNumber;
    private int cost = 3;
    protected int id = 12;

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
        game.getGameBoard().removeCoins(game.getCurrentPlayer(), cost);
        cost = 4;
    }

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public ClientCharacter createClientCharacter(){
        ClientCharacter character = new ClientCharacter12();

        character.setCost(cost);
        character.setId(id);

        return character;
    }

}
