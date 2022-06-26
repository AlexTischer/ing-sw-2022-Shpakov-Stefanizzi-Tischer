package it.polimi.ingsw.server;


import it.polimi.ingsw.modelChange.ConnectionStatusChange;
import it.polimi.ingsw.modelChange.ModelChange;
import it.polimi.ingsw.packets.Packet;

import java.io.*;
import java.net.Socket;
import java.util.Locale;

//This class is clientHandler that manages client on a separate thread
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

    public void close(){
        if (isActive) {
            /*first I deregister client from server and then close socket*/
            System.out.println("Deregistering client with ip: " + clientSocket.getRemoteSocketAddress());

            isActive = false;

            //if name is equal to null that means that client wasn't
            //added to lobby neither virtual view was created for him
            if (name != null) {
                //deregistering is different based on whether game is already started
                if (server.isGameReady()) {
                    server.changeConnectionStatus(new ConnectionStatusChange(name, false));
                    userVirtualView.changePlayerStatus(false);
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
            System.out.println("Done!");
        }
    }

    public void send(ModelChange modelChange) {
        if (isActive) {
            try {
                socketOut.writeObject(modelChange);
                socketOut.flush();
                socketOut.reset();
            } catch (IOException e) {
                //e.printStackTrace();
                System.out.println("Caught IO Exception");
                this.close();
            }
        }
    }

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
                    fromClient = socketIn.readObject();
                    System.out.println("Connection says: I am ready to receive the name!");
                    //if client sent ping message, then i need to respond and wait for the next input
                    if (fromClient.equals("ping")){
                        socketOut.writeObject("pong");
                        socketOut.flush();
                        socketOut.reset();
                        continue;
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
                                socketOut.writeObject("pong");
                                socketOut.flush();
                                socketOut.reset();
                            }
                        }
                    }
                    catch (IllegalArgumentException e){
                        socketOut.writeObject("This name \"" + fromClient + "\" is already used. Please chose another name");
                        socketOut.flush();
                        socketOut.reset();
                    }
                }
                catch (ClassNotFoundException | ClassCastException e) {
                    socketOut.writeObject("The name value is incorrect. Try again");
                    socketOut.flush();
                    socketOut.reset();
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
                                //client sent ping message, server responds with pong
                                socketOut.writeObject("pong");
                                socketOut.flush();
                                socketOut.reset();
                            } else {
                                throw new IllegalArgumentException("Anomalous object received from client " + name);
                            }
                        } catch (ClassCastException | IllegalArgumentException e2) {
                            //client sent neither packet, neither "ping" string
                            socketOut.writeObject("The packet or ping message is incorrect.");
                            socketOut.flush();
                            socketOut.reset();
                            //TODO manage exception raising if information sent from client is suspicious
                        }
                    }
                } else {
                    //even if server is not ready, I should be able to respond to ping messages
                    try {
                        fromClient = socketIn.readObject();
                    } catch (ClassCastException | ClassNotFoundException e) {
                        try {
                            if (fromClient.equals("ping")) {
                                //client sent ping message, server responds with pong
                                socketOut.writeObject("pong");
                                socketOut.flush();
                                socketOut.reset();
                            } else {
                                throw new IllegalArgumentException();
                            }
                        } catch (ClassCastException | IllegalArgumentException e2) {
                            //client sent neither packet, neither "ping" string
                            socketOut.writeObject("The ping message is incorrect. Try again");
                            socketOut.flush();
                            socketOut.reset();
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

    public void sendStart() {
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
