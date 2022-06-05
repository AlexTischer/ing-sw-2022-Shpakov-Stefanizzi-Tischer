package it.polimi.ingsw.modelChange;

import it.polimi.ingsw.client.model.ClientGameBoard;
import it.polimi.ingsw.exceptions.EndOfGameException;

public class EndOfGameChange extends ModelChange{
    private String winner;

    //TODO manage end of game in case there are 4 players
    //hint: client can do it locally, analyzing num of players and tower color
    // the victory of one team member implicates the victory of entire team
    public void execute(ClientGameBoard gameBoard){
        String message;
        if(winner == null){
            message = "You can't play. The game is already started. Try to reconnect later!";
        }
        else if (winner.equals(gameBoard.getClientName()))
            message = "You win!";
        else{
            message = "Game Over! \n" +  winner + " wins!";
        }

        //turn off the game
        gameBoard.setGameOn(false);

        throw new EndOfGameException(message);
    }

    public EndOfGameChange(String winner){
        this.winner = winner;
    }

}
