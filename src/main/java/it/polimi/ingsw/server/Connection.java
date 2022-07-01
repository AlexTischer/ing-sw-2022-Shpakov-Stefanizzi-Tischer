package it.polimi.ingsw.server;


import it.polimi.ingsw.modelChange.ConnectionStatusChange;
import it.polimi.ingsw.modelChange.ModelChange;
import it.polimi.ingsw.packets.Packet;
import it.polimi.ingsw.server.controller.CharacterDeck;
import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.server.model.Character;
import it.polimi.ingsw.server.model.GameBoard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.SchoolBoard;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidParameterException;


/**
 * <p>This class is the clientHandler and runs on a separate thread</p>
 * <ul> Contains:
 *     <li>{@link #clientSocket}  is the socket used to communicate with the client.</li>
 *     <li>{@link #socketIn} is the {@link ObjectInputStream} used to receive client's Strings (including Pings) and {@link Packet}.</li>
 *     <li>{@link #socketOut} is the {@link ObjectOutputStream} used to send Strings (including Pongs) and {@link ModelChange} to the client.</li>
 *     <li>{@link #userVirtualView} instance of {@link VirtualView} linked to specific client.</li>
 *     <li>{@link #server} instance of {@link Server}.</li>
 *     <li>{@link #isActive} boolean used to check whether connection is active.
 *     isActive is set to true during {@link #run()} and becomes false during {@link #close()}.</li>
 *     <li>{@link #name} name of specific client, set during {@link #run()} using string from client.
 *     It is always set to uppercase in {@link #run()}.</li>
 * </ul>
 */


public class Connection implements Runnable{
    private Socket clientSocket;
    private ObjectInputStream socketIn;
    private ObjectOutputStream socketOut;
    private VirtualView userVirtualView;
    private Server server;
    private boolean isActive;
    private String name;


    public Connection(Socket clientSocket, ObjectInputStream socketIn, ObjectOutputStream socketOut, Server server) {
        this.clientSocket = clientSocket;
        this.socketIn = socketIn;
        this.socketOut = socketOut;
        this.server = server;
    }

    public boolean isActive() {
        return isActive;
    }

    /**
     * <p>Closes this connection</p>
     * <ul>
     *     <li>Sets {@link #isActive} to false</li>
     *     <li>if {@link Server#isGameReady()} is false then removes this connection from lobby calling {@link Server#removeFromLobby(Connection)}
     *     else if {@link Server#isGameReady()} is true calls {@link VirtualView#changePlayerStatus(boolean)}
     *     in order to change the status of specific {@link it.polimi.ingsw.server.model.Player} and {@link Server#changeConnectionStatus(ModelChange)}
     *     in order to notify other clients that specific {@link it.polimi.ingsw.server.model.Player} is inactive</li>
     *</ul>
     */
    public void close(){
        if (isActive) {
            /*first I deregister client from server and then close socket*/
            System.out.println("Deregistering client" + name + "with ip: " + clientSocket.getRemoteSocketAddress());

            isActive = false;

            //if name is equal to null that means that client wasn't
            //added to lobby neither virtual view was created for him
            if (name != null) {
                //deregistering is different based on whether game is already started
                if (server.isGameReady()) {
                    synchronized (Game.getInstanceOfGame()) {
                        server.changeConnectionStatus(new ConnectionStatusChange(name, false));
                        userVirtualView.changePlayerStatus(false);
                    }
                } else {
                    server.removeFromLobby(this);
                }

                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.out.println("Oops. Socket closing of" + name + "went wrong!");
                    System.err.println(e.getMessage());
                }
            }
            System.out.println("Deregistering "+name+" Done!");
        }
    }

    /**Sends modelChange to specific client
     * @param modelChange is the {@link ModelChange} that needs to be sent to the client
     */
    public synchronized void send(ModelChange modelChange) {
        if (isActive) {
            try {
                socketOut.writeObject(modelChange);
                socketOut.flush();
                socketOut.reset();
            } catch (IOException e) {
                System.out.println("Caught IO Exception");
                this.close();
            }
        }
    }

    /**
     * <p>Needs to be run on a new Thread. Once called, connection starts to get client's information in order to add it to the game and handle the incoming communication during the game:</p>
     * <ul>
     *     <li>Asks name and calls {@link Server#addClient(Connection, String)} to try to connect it</li>
     *     <li>Loops in a while as long as it {@link #isActive} and {@link Server#isGameReady()} waiting for client's messages, replying to Pings and
     *     forwarding to {@link VirtualView} each incoming {@link Packet} using {@link VirtualView#sendPacket(Packet)}</li>
     *     <li>Calls {@link #close()} if an exception is caught </li>
     * </ul>
     */
    @Override
    public void run() {
        //receives client name, sends lobby change and receives client packets during the game
        try{
            isActive = true;

            boolean nameReady = false;


            //waits till client insert correct name
            socketOut.writeUTF("name");
            socketOut.flush();
            socketOut.reset();

            send(server.createLobbyChange());

            Object fromClient = new Object();

            //receives object from client with readObject() and sends object to client with writeObject()
            while(!nameReady){
                /*allows client to insert the name*/
                try {
                    System.out.println("Connection says: I am ready to receive the name!");
                    fromClient = socketIn.readObject();
                    //if client sent ping message, then i need to respond and wait for the next input
                    if (fromClient.equals("ping")){
                        synchronized(this) {
                            socketOut.writeObject("pong");
                            socketOut.flush();
                            socketOut.reset();
                            continue;
                        }
                    }

                    System.out.println("Server received from client: " + fromClient);
                    try {
                        name = (String)fromClient;
                        name = name.toUpperCase();
                        server.addClient(this, name);

                        nameReady = true;

                        //wait until enough number of clients connect
                        while (!server.isGameReady()) {
                            fromClient = socketIn.readObject();
                            //if client sent ping message, then I need to respond and wait for the next input
                            if (fromClient.equals("ping")){
                                synchronized (this) {
                                    socketOut.writeObject("pong");
                                    socketOut.flush();
                                    socketOut.reset();
                                }
                            }
                        }
                    }
                    catch (IllegalArgumentException e){
                        synchronized (this) {
                            socketOut.writeObject("This name \"" + fromClient + "\" is already used. Please chose another name");
                            socketOut.flush();
                            socketOut.reset();
                        }
                    }
                }
                catch (ClassNotFoundException | ClassCastException e) {
                    synchronized (this) {
                        socketOut.writeObject("The name value is incorrect. Try again");
                        socketOut.flush();
                        socketOut.reset();
                    }
                }
            }

            //wait for client packets and forward them to virtualView
            while(isActive()) {
                if (server.isGameReady()) {
                    fromClient = new Object();
                    try {
                        fromClient = socketIn.readObject();
                        Packet packet = (Packet) fromClient;
                        userVirtualView.sendPacket(packet);
                    } catch (ClassCastException | ClassNotFoundException e) {
                        try {
                            if (fromClient.equals("ping")) {
                                synchronized (this) {
                                    //client sent ping message, server responds with pong
                                    socketOut.writeObject("pong");
                                    socketOut.flush();
                                    socketOut.reset();
                                }
                            } else {
                                throw new IllegalArgumentException("Anomalous object received from client " + name);
                            }
                        } catch (ClassCastException | IllegalArgumentException e2) {
                            synchronized (this) {
                                //client sent neither packet, neither "ping" string
                                socketOut.writeObject("The packet or ping message is incorrect.");
                                socketOut.flush();
                                socketOut.reset();
                            }
                        }
                    }
                } else {
                    //even if server is not ready, I should be able to respond to ping messages
                    try {
                        fromClient = socketIn.readObject();
                    } catch (ClassCastException | ClassNotFoundException e) {
                        try {
                            if (fromClient.equals("ping")) {
                                synchronized (this) {
                                    //client sent ping message, server responds with pong
                                    socketOut.writeObject("pong");
                                    socketOut.flush();
                                    socketOut.reset();
                                }
                            } else {
                                throw new IllegalArgumentException();
                            }
                        } catch (ClassCastException | IllegalArgumentException e2) {
                            synchronized (this) {
                                //client sent neither packet, neither "ping" string
                                socketOut.writeObject("The ping message is incorrect. Try again");
                                socketOut.flush();
                                socketOut.reset();
                            }
                        }
                    }
                }
            }
        }
        catch(IOException | InterruptedException e){
            System.out.println("Closing socket of " + name);
        }
        finally {
            this.close();
        }
    }

    public synchronized void sendStart() {
        if (isActive) {
            try {
                socketOut.writeObject("start");
                socketOut.flush();
                socketOut.reset();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void attachView(VirtualView view){
        this.userVirtualView = view;
    }

    public String getClientName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
