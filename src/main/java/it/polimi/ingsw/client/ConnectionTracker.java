package it.polimi.ingsw.client;

import it.polimi.ingsw.server.Connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * This class is run on a separate thread on the client and is used to send periodical pings to the server, in order not to let {@link java.net.SocketTimeoutException}
 * to be thrown on the server as long as the client is actually active.
 */
public class ConnectionTracker implements Runnable{
    ClientConnection connection;
    ObjectOutputStream socketOut;
    ObjectInputStream socketIn;

    public ConnectionTracker(ClientConnection connection, ObjectOutputStream socketOut, ObjectInputStream socketIn) {
        this.connection = connection;
        this.socketOut = socketOut;
        this.socketIn = socketIn;
    }

    /**
     * Activates the ping periodical sending as long as {@link Connection#isActive()} and the socket is open
     */
    @Override
    public void run() {
        //sends ping every 5 seconds
        while (connection.isActive()) {
            try {
                synchronized (connection) {
                    socketOut.writeObject("ping");
                    socketOut.flush();
                    socketOut.reset();
                }
                Thread.sleep(5 * 1000);
            } catch (IOException e) {
                connection.close();
            } catch (InterruptedException e) {
                connection.close();
            }
        }
    }
}
