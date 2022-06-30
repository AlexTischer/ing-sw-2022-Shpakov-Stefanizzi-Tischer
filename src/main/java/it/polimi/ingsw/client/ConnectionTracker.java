package it.polimi.ingsw.client;

import it.polimi.ingsw.exceptions.EndOfChangesException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class ConnectionTracker implements Runnable{
    ClientConnection connection;
    ObjectOutputStream socketOut;
    ObjectInputStream socketIn;

    public ConnectionTracker(ClientConnection connection, ObjectOutputStream socketOut, ObjectInputStream socketIn) {
        this.connection = connection;
        this.socketOut = socketOut;
        this.socketIn = socketIn;
    }

    @Override
    public void run() {
        //sends ping every 2 seconds
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
                //thread exception
            }
        }
    }
}
