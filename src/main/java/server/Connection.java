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
            socketOut.writeChars(msg);
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

            //waits till client insert correct name
            while(true){
                try{
                    /*allows client to insert the name*/
                    socketOut.writeChars("name");
                    String name = socketIn.readUTF();
                    //from this moment client needs to create model, view and controller
                    server.lobby(this, name);
                    break;
                }
                catch (IllegalArgumentException e){
                    continue;
                }
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
                        socketOut.writeByte(1);
                        socketOut.flush();
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
