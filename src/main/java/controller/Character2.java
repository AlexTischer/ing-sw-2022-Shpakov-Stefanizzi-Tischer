package controller;

import exceptions.NoEnoughCoinsException;
import model.Color;
import model.Island;
import java.util.ArrayList;

/** adds 2 points to influence of currentPlayer */
public class Character2 extends Character {

    private int cost = 2;

    public int calculateInfluence(Island island){
        int score = 2;
        for (Color color: game.currentPlayer.getProfessorsColor()) {
            score += island.getNumOfStudents(color);
        }
        return score;
    }

    @Override
    public void buy() throws NoEnoughCoinsException {
        game.currentPlayer.removeCoins(cost);
        cost = 3;
    }


}
