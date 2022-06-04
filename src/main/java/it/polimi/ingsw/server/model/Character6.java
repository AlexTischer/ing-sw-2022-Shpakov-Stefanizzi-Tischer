package it.polimi.ingsw.server.model;

import it.polimi.ingsw.exceptions.EndOfChangesException;
import it.polimi.ingsw.modelChange.ExceptionChange;
import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.exceptions.NoEnoughCoinsException;

public class Character6 extends Character {
    /*In a Tie, currentPlayer wins*/

    private int cost = 2;
    protected int id = 6;

    @Override
    public void initialFill(Game game){
        super.initialFill(game);
    }

    @Override
    public void reassignProfessor() {
        Player leader = game.getPlayers().get(0);

        for (Color color: Color.values()){

            for (Player player: game.getPlayers()){
                if(player.getNumOfStudentsInDining(color) == leader.getNumOfStudentsInDining(color) && player.equals(game.getCurrentPlayer()))
                    leader = player;

                if(player.getNumOfStudentsInDining(color) > leader.getNumOfStudentsInDining(color))
                    leader = player;
            }

            if (game.getCurrentPlayer().equals(leader) && leader.getNumOfStudentsInDining(color) > 0){
                game.getGameBoard().addProfessor(leader, color);

                for (Player player: game.getPlayers())
                    if (!player.equals(leader))
                        game.getGameBoard().removeProfessor(player,color);
            }
        }

        //tell client to move to the next step in action phase
        ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        game.getGameBoard().notify(exceptionChange);
    }
    @Override
    public void buy() throws NoEnoughCoinsException {
        game.getGameBoard().removeCoins(game.getCurrentPlayer(), cost);
        cost = 3;
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
