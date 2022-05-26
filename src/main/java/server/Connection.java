package server;


import modelChange.ConnectionStatusChange;
import modelChange.ModelChange;
import packets.Packet;

import java.io.*;
import java.net.Socket;

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
        /*first I deregister client from server and then close socket*/
        System.out.println("Deregistering client with ip: " + clientSocket.getRemoteSocketAddress() + " and name: " + name);

        isActive = false;
        //deregistering is different based on whether game is already started
        if (server.isGameReady()) {
            server.changeConnectionStatus(new ConnectionStatusChange(name, false));
        }
        else {
            server.removeFromLobby(this);
        }

        try{
            clientSocket.close();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }

        System.out.println("Done!");
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
        System.out.println("I am connection. I have started !");
        try{
            isActive = true;

            boolean nameReady = false;

            //waits till client insert correct name
            socketOut.writeUTF("name");
            socketOut.flush();
            socketOut.reset();
            Object fromClient = new Object();

            //receives object from client with readObject() and sends object to client with writeObject()
            while(!nameReady){
                /*allows client to insert the name*/
                try {
                    fromClient = socketIn.readObject();
                    //if client sent ping message, then i need to respond and wait for the next input
                    if (fromClient.equals("ping")){
                        System.out.println(name + " sent ping");
                        socketOut.writeObject("pong");
                        socketOut.flush();
                        socketOut.reset();
                        continue;
                    }

                    System.out.println("Server received from client: " + fromClient);
                    try {
                        server.addClient(this, (String) fromClient);
                        System.out.println("Client " + fromClient + " added Client");
                        nameReady = true;
                        name = (String)fromClient;

                        //wait until enough number of clients connect
                        while (!server.isGameReady()) {
                            fromClient = socketIn.readObject();
                            //if client sent ping message, then i need to respond and wait for the next input
                            if (fromClient.equals("ping")){
                                System.out.println(name + " sent ping");
                                socketOut.writeObject("pong");
                                socketOut.flush();
                                socketOut.reset();
                            }
                        }
                        socketOut.writeObject("start");
                        socketOut.flush();
                        socketOut.reset();
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
            while(isActive()){
                if (server.isGameReady()) {
                    fromClient = new Object();
                    try {
                        fromClient = socketIn.readObject();
                        Packet packet = (Packet)fromClient;
                        userVirtualView.sendPacket(packet);
                    }
                    catch (ClassCastException | ClassNotFoundException e) {
                        try {
                            if (fromClient.equals("ping")){
                                //client sent ping message, server responds with pong
                                System.out.println(name + " sent ping");
                                socketOut.writeObject("pong");
                                socketOut.flush();
                                socketOut.reset();
                            }
                            else{
                                throw new IllegalArgumentException();
                            }
                        }
                        catch (ClassCastException | IllegalArgumentException e2){
                            //client sent neither packet, neither "ping" string
                            socketOut.writeObject("The packet or ping message is incorrect.");
                            socketOut.flush();
                            socketOut.reset();
                            //TODO manage exception raising if information sent from client is suspicious
                        }
                    }
                }
                else{
                    //even if server is not ready, I should be able to respond to ping messages
                    try {
                        fromClient = socketIn.readObject();
                    }
                    catch (ClassCastException | ClassNotFoundException e) {
                        try {
                            if (fromClient.equals("ping")){
                                //client sent ping message, server responds with pong
                                System.out.println(name + " sent ping");
                                socketOut.writeObject("pong");
                                socketOut.flush();
                                socketOut.reset();
                            }
                            else{
                                throw new IllegalArgumentException();
                            }
                        }
                        catch (ClassCastException | IllegalArgumentException e2){
                            //client sent neither packet, neither "ping" string
                            socketOut.writeObject("The ping message is incorrect. Try again");
                            socketOut.flush();
                            socketOut.reset();
                        }
                    }
                }
            }
        }
        catch(IOException e){
            System.err.println(e.getMessage());
            System.out.println("Closing socket of " + name);
        } finally {
            this.close();
        }
    }

}
