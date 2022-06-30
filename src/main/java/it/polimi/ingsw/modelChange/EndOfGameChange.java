package it.polimi.ingsw.modelChange;

import it.polimi.ingsw.client.model.ClientGameBoard;
import it.polimi.ingsw.client.model.ClientPlayer;
import it.polimi.ingsw.exceptions.EndOfGameException;
import it.polimi.ingsw.server.model.Player;

public class EndOfGameChange extends ModelChange{
    private String winner;

    public void execute(ClientGameBoard gameBoard){
        String message;
        String cowinner = "";
        if(winner == null){
            message = "You can't play. There is no free space. Try to reconnect later!";
        }
        else if (winner.equals(gameBoard.getClientName())){
            if(gameBoard.getPlayers().size() == 4){
                for (ClientPlayer p : gameBoard.getPlayers()) {
                    if (!p.getName().equals(winner) && p.getTowerColor().equals(gameBoard.getPlayer(winner).getTowerColor())) {
                        cowinner = p.getName();
                    }
                }
                message = "You and " + cowinner + " win!";
            }
            else {
                message = "You win!";
            }
        }
        else if (gameBoard.getPlayer(winner).getTowerColor().equals(gameBoard.getPlayer(gameBoard.getClientName()).getTowerColor())){
            if(gameBoard.getPlayers().size() == 4){
                message = "You and " + winner + " win!";
            }
            else {
                message = "You win!";
            }
        }
        else {
            if (gameBoard.getPlayers().size() == 4) {
                for (ClientPlayer p : gameBoard.getPlayers()) {
                    if (!p.getName().equals(winner) && p.getTowerColor().equals(gameBoard.getPlayer(winner).getTowerColor())) {
                        winner = winner + " and " + p.getName();
                    }
                }
                message = "Game Over! " + winner + " win!";
            } else {
                message = "Game Over! " + winner + " wins!";
            }
        }
        //turn off the game
        gameBoard.setGameOn(false);

        throw new EndOfGameException(message);
    }

    public EndOfGameChange(String winner){
        this.winner = winner;
    }

}
