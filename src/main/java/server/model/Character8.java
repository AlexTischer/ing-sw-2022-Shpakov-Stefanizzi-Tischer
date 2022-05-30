package server.model;

import server.controller.Game;
import exceptions.NoEnoughCoinsException;

import static java.lang.Math.abs;

/** adds 2 to maximum number of steps */
public class Character8 extends Character {

    private int cost = 1;
    protected int id = 8;

    @Override
    public void initialFill(Game game){
        super.initialFill(game);
    }

    @Override
    public boolean moveMotherNature(int steps){
        return (game.getCurrentPlayer().getPlayedAssistant().getMovements()+2>=abs(steps));
    }

    @Override
    public void buy() throws NoEnoughCoinsException {
        game.getGameBoard().removeCoins(game.getCurrentPlayer(), cost);
        cost = 2;
    }

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public int getId() {
        return id;
    }
}
