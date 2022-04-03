package controller;

import exceptions.NoEnoughCoinsException;
import model.Color;
import java.util.*;

public class Character6 extends Character{
    /*In a Tie, currentPlayer wins*/

    private int cost = 2;

    @Override
    public void reassignProfessor(Color professorColor) {
        Player leader = game.players.get(0);

        for (Color color: Color.values()){

            for (Player player: game.players){
                if(player.getNumOfStudentsInDining(color) == leader.getNumOfStudentsInDining(color) && player.equals(game.currentPlayer))
                    leader = player;

                if (player.getNumOfStudentsInDining(color) > leader.getNumOfStudentsInDining(color))
                    leader = player;
            }

            if (game.currentPlayer.equals(leader)){
                leader.addProfessor(color);

                for (Player player: game.players)
                    if (!player.equals(leader))
                        player.removeProfessor(color);
            }
        }
    }
    @Override
    public void buy() throws NoEnoughCoinsException {
        game.currentPlayer.removeCoins(cost);
        cost = 3;
    }

}
