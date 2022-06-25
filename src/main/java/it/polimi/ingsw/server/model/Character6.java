package it.polimi.ingsw.server.model;

import it.polimi.ingsw.client.model.ClientCharacter;
import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.exceptions.NoEnoughCoinsException;

/*When reassigning professor in a Tie, currentPlayer wins*/
public class Character6 extends Character {

    private int id=6;
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
        game.getGameBoard().removeCoinsFromPlayer(game.getCurrentPlayer(), cost);
        //if it's first use then we need to leave one coin on the card
        if (firstUse){
            game.getGameBoard().addCoinsToBank(cost-1);
            firstUse = false;
        }
        else {
            game.getGameBoard().addCoinsToBank(cost);
        }
        cost = 3;
    }

    @Override
    public ClientCharacter createClientCharacter() {
        ClientCharacter character = new ClientCharacter();
        character.setId(id);
        character.setCost(cost);
        character.setDescription(description);
        character.setFirstUse(firstUse);

        return character;
    }

}
