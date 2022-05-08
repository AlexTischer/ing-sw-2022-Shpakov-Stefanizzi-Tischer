package server.model;

import server.controller.Game;
import exceptions.NoEnoughCoinsException;

/** adds 2 points to influence of currentPlayer */
public class Character2 extends Character {

    private int cost = 2;

    @Override
    public void initialFill(Game game){
        super.initialFill(game);
    }


    //TODO accept Player parameter
    public int calculateInfluence(Island island, int islandNumber, Player player){
        int score = 2;
        for (Color color: player.getProfessorsColor()){
            score += island.getNumOfStudents(color);
        }
        if (player.getTowerColor().equals(island.getTowersColor())){
            score += island.getNumOfTowers();
        }
        return score;
    }

    @Override
    public void buy() throws NoEnoughCoinsException {
        game.getGameBoard().removeCoins(game.getCurrentPlayer(), cost);
        cost = 3;
    }


}
