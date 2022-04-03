package controller;

import model.Color;
import model.Island;
import java.util.ArrayList;

/** adds 2 points to influence of currentPlayer */
public class Character2 extends Character {

    public int calculateInfluence(Island island){
        int score = 2;
        for (Color color: game.currentPlayer.getProfessorsColor()) {
            score += island.getNumOfStudents(color);
        }
        return score;
    }

}
