package it.polimi.ingsw.modelChange;

import it.polimi.ingsw.client.model.ClientGameBoard;
import it.polimi.ingsw.server.controller.GameForClient;

import java.io.Serializable;

/**
 * This is the class that represents every message (except for configuration strings and pongs) that the server can send to the client.
 * It is created on the server and filled with all the changes that happened to the model in order to replicate them on the clientModel.
 * It can also be used to change client' status or to reject a user's move (i.e. with {@link ExceptionChange}).
 * Once it's received by the client, it can be used through the {@link #execute(ClientGameBoard)} method that will be able to use {@link ClientGameBoard} methods.
 */
public class ModelChange implements Serializable {

    /**
     * Every ModelChange can be implemented in a specific way, but they all can be executed with this common method that will make changes to the client
     * @param gameBoard is the clientGameBoard that will be used to update the client's model.
     */
    public void execute(ClientGameBoard gameBoard){}
}
