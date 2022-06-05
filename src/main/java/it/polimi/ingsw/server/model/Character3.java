package it.polimi.ingsw.server.model;

import it.polimi.ingsw.client.model.ClientCharacter;
import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.exceptions.NoEnoughCoinsException;
import it.polimi.ingsw.exceptions.NoEntryException;

public class Character3 extends Character {
    /*does not count towers in influence*/

    private int cost = 3;
    private String description = "C3";

    @Override
    public void initialFill(Game game){
        super.initialFill(game);
    }

    @Override
    public int calculateInfluence(Island island, int islandNumber, Player player) throws NoEntryException {
        if (!island.getNoEntry()){
            int score = 0;
            for (Color color: player.getProfessorsColor()){
                score += island.getNumOfStudents(color);
            }

            return score;
        }
        else {
            game.setNoEntry(islandNumber,false);
            throw new NoEntryException();
        }
    }
    @Override
    public void buy() throws NoEnoughCoinsException {
        game.getGameBoard().removeCoins(game.getCurrentPlayer(), cost);
        cost = 4;
    }

    @Override
    public ClientCharacter createClientCharacter() {
        ClientCharacter clientCharacter = new ClientCharacter();
        clientCharacter.setCost(cost);
        clientCharacter.setDescription(description);
        return clientCharacter;
    }
}
