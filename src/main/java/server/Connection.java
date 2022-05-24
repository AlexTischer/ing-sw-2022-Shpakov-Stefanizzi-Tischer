package server;


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

    public void close(String msg){
        try{
            clientSocket.close();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
        isActive = false;

        /*first I close socket, then I deregister client from server*/

        System.out.println("Deregistering client with ip: " + clientSocket.getRemoteSocketAddress());
        server.deregisterConnectionFromLobby(server.currentConnection);
        System.out.println("Done!");
    }

    public void send(ModelChange modelChange) {
        try{
            socketOut.writeObject(modelChange);
            socketOut.flush();
            socketOut.reset();
        }
        catch (IOException e){
//            e.printStackTrace();
            System.out.println("Caught IO Excepion");
            server.currentConnection.close("wasn't able to send modelChange");
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

            Object fromClient;
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
                        server.addToLobby(this, (String) fromClient);
                        System.out.println("Client " + fromClient + " added to addToLobby");
                        nameReady = true;
                        name = (String)fromClient;
                        clientSocket.setSoTimeout(10*1000);
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
