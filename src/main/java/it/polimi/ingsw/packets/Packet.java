package it.polimi.ingsw.packets;

import it.polimi.ingsw.server.controller.GameForClient;

import java.io.Serializable;
import java.util.List;

/**
 * This is the class that represents every message (except for config strings and pings) that the client can send to the server.
 * It is created on the client and filled with all the actions that the user wish to make to the server's model.
 * Once it's received by the server, it can be used through the {@link #execute(GameForClient)} method that will use {@link GameForClient} methods.
 */
public abstract class Packet implements Serializable {

    public void execute(GameForClient game){}
}
