package it.polimi.ingsw.server.model;

import it.polimi.ingsw.client.model.ClientCharacter;
import it.polimi.ingsw.exceptions.NoEntryException;
import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.exceptions.NoEnoughCoinsException;

/** adds 2 points to influence of currentPlayer */
public class Character2 extends Character {

    private int cost = 2;
    private String description = "You get 2 more influence points";

    @Override
    public void initialFill(Game game){
        super.initialFill(game);
    }


    public int calculateInfluence(Island island, int islandNumber, Player player){
        if (!island.getNoEntry()) {
            int score = 2;
            for (Color color : player.getProfessorsColor()) {
                score += island.getNumOfStudents(color);
            }
            if (player.getTowerColor().equals(island.getTowersColor())) {
                score += island.getNumOfTowers();
            }
            return score;
        }
        else {
            /*takes no entry tile away from the island*/
            game.setNoEntry(islandNumber,false);
            throw new NoEntryException();
        }
    }

    @Override
    public ClientCharacter createClientCharacter() {
        ClientCharacter clientCharacter = new ClientCharacter();
        clientCharacter.setCost(cost);
        clientCharacter.setDescription(description);
        return clientCharacter;
    }

    @Override
    public void buy() throws NoEnoughCoinsException {
        game.getGameBoard().removeCoins(game.getCurrentPlayer(), cost);
        cost = 3;
    }

}