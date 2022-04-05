package model;

import exceptions.NoEnoughCoinsException;

/** adds 2 points to influence of currentPlayer */
public class Character2 extends Character {

    private int cost = 2;

    public int calculateInfluence(Island island){
        int score = 2;
        for (Color color: game.getCurrentPlayer().getProfessorsColor()) {
            score += island.getNumOfStudents(color);
        }
        return score;
    }

    @Override
    public void buy() throws NoEnoughCoinsException {
        game.getCurrentPlayer().removeCoins(cost);
        cost = 3;
    }


}
