package modelChange;

import client.model.ClientGameBoard;
import client.model.ClientPlayer;

//sent to all clients when connection discovers that client is inactive
public class ConnectionStatusChange extends ModelChange{
    private String name;
    private boolean status;

    @Override
    public void execute(ClientGameBoard gameBoard){
        for (ClientPlayer p: gameBoard.getPlayers()){
            if (p.getName().equals(name)) {
                p.setConnectionStatus(status);
            }
        }
    }

    public ConnectionStatusChange(String name, boolean status) {
        this.name = name;
        this.status = status;
    }
}
