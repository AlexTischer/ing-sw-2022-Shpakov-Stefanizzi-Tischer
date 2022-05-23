package server;


import modelChange.ModelChange;
import packets.Packet;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

//This class is clientHandler that manages client on a separate thread
public class Connection implements Runnable{
    private Socket clientSocket;
    private ObjectInputStream socketIn;
    private ObjectOutputStream socketOut;
    private VirtualView userVirtualView;
    private Server server;
    private boolean isActive;


    public Connection(Socket clientSocket, ObjectInputStream socketIn, ObjectOutputStream socketOut, Server server) {
        this.clientSocket = clientSocket;
        this.socketIn = socketIn;
        this.socketOut = socketOut;
        this.server = server;
        try {
            clientSocket.setSoTimeout(15*1000);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isActive() {
        return isActive;
    }

    private void closeConnection(String msg){
        try{
            clientSocket.close();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
        isActive = false;
    }

    public void close(String msg){
        /*first I close socket, then I deregister client from server*/
        closeConnection(msg);
        System.out.println("Deregistering client with ip" + clientSocket.getRemoteSocketAddress());
        server.deregisterConnectionFromLobby(this);
        System.out.println("Done!");
    }

    public void send(ModelChange modelChange) {
        try{
            socketOut.writeObject(modelChange);
            socketOut.flush();
            socketOut.reset();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        System.out.println("I am connection. I have started !");
        try{
            isActive = true;

            boolean nameReady = false;

            //waits till client insert correct name
            socketOut.writeUTF("name");
            socketOut.flush();
            socketOut.reset();

            Object fromClient = new Object();
            while(!nameReady){
                /*allows client to insert the name*/
                try {
                    fromClient = socketIn.readObject();
                    //if client sent ping message, then i need to respond and wait for the next input
                    if (fromClient.equals("ping")){
                        socketOut.writeObject("pong");
                        socketOut.flush();
                        socketOut.reset();
                        continue;
                    }

                    System.out.println("Server received from client: " + fromClient);
                    try {
                        server.lobby(this, (String) fromClient);
                    }
                    catch (IllegalArgumentException e){
                        socketOut.writeObject("This name \"" + fromClient + "\" is already used. Please chose another name");
                        socketOut.flush();
                        socketOut.reset();
                        continue;
                    }

                    System.out.println("Client " + fromClient + " added to lobby");

                    synchronized (this) {
                        while (!server.isGameReady()) {
                            try {
                                this.wait();
                            } catch (InterruptedException e) {
                                //e.printStackTrace();
                                throw new IOException();
                            }
                        }
                    }
                    socketOut.writeObject("start");
                    socketOut.flush();
                    socketOut.reset();

                    nameReady = true;
                }
                catch (ClassNotFoundException | ClassCastException e) {
                    socketOut.writeObject("The name value is incorrect. Try again");
                    socketOut.flush();
                    socketOut.reset();
                }
            }

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
                                socketOut.writeUTF("pong");
                                socketOut.flush();
                                socketOut.reset();
                            }
                            else{
                                throw new IllegalArgumentException();
                            }
                        }
                        catch (ClassCastException | IllegalArgumentException e2){
                            //client sent neither packet, neither "ping" string
                        }
                    }
                }
            }
        } catch(IOException e){
            System.err.println(e.getMessage());
            System.out.println("got error" + e + ". \nClosing socket");
        } finally {
            close("Connection closed from server side, malicious client\nGood bye!");
        }
    }

}
