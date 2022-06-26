package it.polimi.ingsw.modelChange;

import it.polimi.ingsw.client.model.ClientGameBoard;
import it.polimi.ingsw.client.model.ClientPlayer;

import java.util.Locale;

//sent to all clients when connection discovers that client is inactive
public class ConnectionStatusChange extends ModelChange{
    private String name;
    private boolean status;

    @Override
    public void execute(ClientGameBoard gameBoard){
        gameBoard.setPlayerStatus(name, status);
    }

    public ConnectionStatusChange(String name, boolean status) {
        this.name = name;
        this.status = status;
    }
}
