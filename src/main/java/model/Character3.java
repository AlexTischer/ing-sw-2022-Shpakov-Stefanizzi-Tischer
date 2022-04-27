package model;

import controller.Game;
import exceptions.NoEnoughCoinsException;
import exceptions.NoEntryException;

import java.util.ArrayList;

public class Character3 extends Character{
    /*does not count towers in influence*/

    private int cost = 3;

    @Override
    public void initialFill(Game game){
        super.initialFill(game);
    }

    @Override
    public int calculateInfluence(Island island, int islandNumber) throws NoEntryException {
        if (!island.getNoEntry()){
            int score = 0;
            for (Color color: game.getCurrentPlayer().getProfessorsColor()){
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
        game.getCurrentPlayer().removeCoins(cost);
        cost = 4;
    }

}
