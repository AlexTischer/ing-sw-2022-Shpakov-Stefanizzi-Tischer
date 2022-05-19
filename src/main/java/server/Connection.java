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

    public Connection(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    public boolean isActive() {
        return isActive;
    }

    private void closeConnection(String msg){
        try{
            socketOut.writeUTF(msg);
            socketOut.flush();
            socketOut.reset();
            clientSocket.close();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
        isActive = false;
    }

    public void close(String msg){
        /*first I close socket, then I deregister client from server*/
        closeConnection(msg);
        System.out.println("Deregistering client with ip" + clientSocket.getRemoteSocketAddress().toString());
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
        try{
            isActive = true;
            socketOut = new ObjectOutputStream(clientSocket.getOutputStream());
            socketOut.flush();
            socketIn = new ObjectInputStream(clientSocket.getInputStream());

            boolean nameReady = false;
            //waits till client insert correct name
            while(!nameReady){
                /*allows client to insert the name*/
                socketOut.reset();
                socketOut.writeUTF("name");
                socketOut.flush();

                String name = socketIn.readUTF();
                System.out.println("Server received from client: " + name);
                try {
                    server.lobby(this, name);
                }
                catch (IllegalArgumentException e){
                    socketOut.reset();
                    socketOut.writeUTF("This name \"" + name + "\" is already used. Please chose another name");
                    socketOut.flush();
                }

                nameReady = true;
                socketOut.reset();
                socketOut.writeUTF("ok");
                socketOut.flush();

                socketOut.writeUTF("start");
                socketOut.flush();
                socketOut.reset();
            }

            while(isActive()){
                if (server.isGameReady()) {
                    try {
                        Packet packet = (Packet) socketIn.readObject();
                        userVirtualView.sendPacket(packet);
                    } catch (ClassCastException | ClassNotFoundException e) {
                        /*client has sent wrong object type*/
                        e.printStackTrace();
                    }
                }
                else{//control if client is alive
                    //I suppose it must be done on a separate thread
                    try {
                        socketOut.writeUTF("1");
                        socketOut.flush();
                        socketOut.reset();
                        /*set timeout so that if I don`t get the response, close the socket*/
                        clientSocket.setSoTimeout(10*1000);
                        /*execute it if response is sent*/
                        byte ping = socketIn.readByte();

                    } catch(IOException e){
                        /*this exception gets caught in case client has closed it`s socket*/
                        e.printStackTrace();
                        server.deregisterConnectionFromLobby(this);
                        close("Connection closed from server side. Dead client\nGood bye!");
                    }
                }
            }
        } catch(IOException e){
            System.err.println(e.getMessage());
        } finally {
            close("Connection closed from server side, malicious client\nGood bye!");
        }
    }
}
