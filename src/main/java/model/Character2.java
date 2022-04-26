package model;

import controller.Game;
import exceptions.NoEnoughCoinsException;

import java.util.ArrayList;

/** adds 2 points to influence of currentPlayer */
public class Character2 extends Character {

    private int cost = 2;

    @Override
    public void initialFill(Game game){
        super.initialFill(game);
    }

    public int calculateInfluence(Island island, int islandNumber){
        int score = 2;
        for (Color color: game.getCurrentPlayer().getProfessorsColor()){
            score += island.getNumOfStudents(color);
        }
        if (game.getCurrentPlayer().getTowerColor().equals(island.getTowersColor())){
            score += island.getNumOfTowers();
        }
        return score;
    }

    @Override
    public void buy() throws NoEnoughCoinsException {
        game.getCurrentPlayer().removeCoins(cost);
        cost = 3;
    }


}
