package server.model;

import server.controller.Game;
import exceptions.NoEnoughCoinsException;

public class Character6 extends Character {
    /*In a Tie, currentPlayer wins*/

    private int cost = 2;

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

            if (game.getCurrentPlayer().equals(leader)){
                game.getGameBoard().addProfessor(leader, color);

                for (Player player: game.getPlayers())
                    if (!player.equals(leader))
                        game.getGameBoard().removeProfessor(player,color);
            }
        }
    }
    @Override
    public void buy() throws NoEnoughCoinsException {
        game.getGameBoard().removeCoins(game.getCurrentPlayer(), cost);
        cost = 3;
    }

}
