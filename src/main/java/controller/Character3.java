package controller;

import exceptions.NoEnoughCoinsException;
import exceptions.NoEntryException;
import model.Color;
import model.Island;

public class Character3 extends Character{
    /*does not count towers in influence*/

    private int cost = 3;

    @Override
    public int calculateInfluence(Island island, int islandNumber) throws NoEntryException {
        if (!island.getNoEntry()){
            int score = 0;
            for (Color color: game.currentPlayer.getProfessorsColor()){
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
        game.currentPlayer.removeCoins(cost);
        cost = 4;
    }

}
