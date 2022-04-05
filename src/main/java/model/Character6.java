package model;

import controller.Player;
import exceptions.NoEnoughCoinsException;

public class Character6 extends Character{
    /*In a Tie, currentPlayer wins*/

    private int cost = 2;

    @Override
    public void reassignProfessor(Color professorColor) {
        Player leader = game.getPlayers().get(0);

        for (Color color: Color.values()){

            for (Player player: game.getPlayers()){
                if(player.getNumOfStudentsInDining(color) == leader.getNumOfStudentsInDining(color) && player.equals(game.getCurrentPlayer()))
                    leader = player;

                if (player.getNumOfStudentsInDining(color) > leader.getNumOfStudentsInDining(color))
                    leader = player;
            }

            if (game.getCurrentPlayer().equals(leader)){
                leader.addProfessor(color);

                for (Player player: game.getPlayers())
                    if (!player.equals(leader))
                        player.removeProfessor(color);
            }
        }
    }
    @Override
    public void buy() throws NoEnoughCoinsException {
        game.getCurrentPlayer().removeCoins(cost);
        cost = 3;
    }

}
