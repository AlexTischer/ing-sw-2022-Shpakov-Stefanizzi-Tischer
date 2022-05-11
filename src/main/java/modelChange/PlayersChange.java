package modelChange;

import client.model.ClientGameBoard;
import client.model.ClientPlayer;
import server.model.Player;

import java.util.ArrayList;

public class PlayersChange extends ModelChange{
    private ArrayList<ClientPlayer> players;

    @Override
    public void execute(ClientGameBoard gameBoard){
        gameBoard.setPlayers(players);
    }

    public PlayersChange(ArrayList<Player> players){
        for(Player p : players){
            //TODO cast Player --> ClientPlayer
        }
    }
}
