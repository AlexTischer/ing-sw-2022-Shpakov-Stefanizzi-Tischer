package it.polimi.ingsw.server.model;

import it.polimi.ingsw.client.model.ClientCharacter;
import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.exceptions.NoEnoughCoinsException;
import it.polimi.ingsw.exceptions.NoEntryException;

/*does not count towers in influence*/
public class Character3 extends Character {

    private int id=3;
    private int cost = 3;
    private String description = "When calculating influence, Towers won't count";

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
    public ClientCharacter createClientCharacter() {
        ClientCharacter character = new ClientCharacter();
        character.setId(id);
        character.setCost(cost);
        character.setDescription(description);
        character.setFirstUse(firstUse);

        return character;
    }
}
