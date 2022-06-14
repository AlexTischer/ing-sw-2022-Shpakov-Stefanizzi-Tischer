package it.polimi.ingsw.server.model;

import it.polimi.ingsw.client.model.ClientCharacter;
import it.polimi.ingsw.exceptions.EndOfChangesException;
import it.polimi.ingsw.modelChange.ExceptionChange;
import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.exceptions.NoEnoughCoinsException;

public class Character6 extends Character {
    /*In a Tie, currentPlayer wins*/

    private int cost = 2;
    private String description  = "You can get the professor even if you have the same number of students as the player who currently controls that professor";
    @Override
    public void initialFill(Game game){
        super.initialFill(game);
    }

    @Override
    public void reassignProfessor() {

        for (Color color: Color.values()){
            Player leader = game.getPlayers().get(0);

            for (Player player: game.getPlayers()){
                if(player.getNumOfStudentsInDining(color) == leader.getNumOfStudentsInDining(color) && player.equals(game.getCurrentPlayer()))
                    leader = player;

                if(player.getNumOfStudentsInDining(color) > leader.getNumOfStudentsInDining(color))
                    leader = player;

                if(player.getNumOfStudentsInDining(color) == 0){
                    game.getGameBoard().removeProfessor(player, color);
                }
            }

            if (game.getCurrentPlayer().equals(leader) && leader.getNumOfStudentsInDining(color) > 0){
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

    @Override
    public ClientCharacter createClientCharacter() {
        ClientCharacter clientCharacter = new ClientCharacter();
        clientCharacter.setCost(cost);
        clientCharacter.setDescription(description);
        return clientCharacter;
    }

}
