package it.polimi.ingsw.server.model;

import it.polimi.ingsw.client.model.ClientCharacter;
import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.exceptions.NoEnoughCoinsException;

import static java.lang.Math.abs;

/** adds 2 to maximum number of steps */
public class Character8 extends Character {

    private int id = 8;
    private int cost = 1;
    private String description = "You can move Mother Nature up to 2 steps more than what's indicated by the Assistant card you played";
    @Override
    public void initialFill(Game game){
        super.initialFill(game);
    }

    @Override
    public boolean moveMotherNature(int steps){
        return (game.getCurrentPlayer().getPlayedAssistant().getMovements()+2>=steps && steps>0);
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
        cost = 2;
    }

    @Override
    public ClientCharacter createClientCharacter() {
        ClientCharacter character = new ClientCharacter();
        character.setId(id);
        character.setCost(cost);
        character.setDescription(description);
        character.setFirstUse(firstUse);
        return character;
    }
}
