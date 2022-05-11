package server;

import modelChange.ModelChange;
import packets.Packet;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Connection implements Runnable{
    private Socket clientSocket;
    private ObjectInputStream socketIn;
    private ObjectOutputStream socketOut;
    private VirtualView clientVirtualView;
    private Server server;
    private boolean isActive;

    public Connection(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    public boolean isActive() {
        return isActive;
    }

    private synchronized void closeConnection(){
        try{
            socketOut.writeChars("Connection closed from the server side");
            clientSocket.close();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
        isActive = false;
    }

    public void close(){
        /*first I close socket, then I deregister client from server*/
        closeConnection();
        System.out.println("Deregistering client with ip" + clientSocket.getRemoteSocketAddress().toString());
        server.deregisterConnection(this);
        System.out.println("Done!");
    }

    public void send(ModelChange modelChange) {
        try{
            socketOut.writeObject(modelChange);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try{
            isActive = true;
            socketIn = new ObjectInputStream(clientSocket.getInputStream());
            socketOut = new ObjectOutputStream(clientSocket.getOutputStream());

            /*allows client to insert the name*/
            socketOut.writeChars("name");
            String name = socketIn.readUTF();
            server.lobby(this, name);

            while(isActive()){
                if (server.getGameReady()) {
                    try {
                        Packet packet = (Packet) socketIn.readObject();
                        clientVirtualView.sendPacket(packet);
                    } catch (ClassCastException | ClassNotFoundException e) {
                        /*client has sent wrong object type*/
                        e.printStackTrace();
                    }
                }
            }
        } catch(IOException e){
            System.err.println(e.getMessage());
        } finally {
            close();
        }
    }
}
