package client;

import client.controller.ClientController;
import modelChange.ModelChange;
import packets.Packet;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.security.InvalidParameterException;
import java.util.Scanner;

public class ClientConnection {
    private Socket socket;
    private ObjectInputStream socketIn;
    private ObjectOutputStream socketOut;
    private ClientController clientController;
    private boolean isActive;
    private String name;

    public ClientConnection(Socket socket) {
        this.socket = socket;
    }

    public boolean isActive() {
        return isActive;
    }

    private void closeConnection(){
        try{
            socket.close();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
        isActive = false;
    }

    public void close(){
        /*first I close socket, then I deregister client from server*/
        System.out.println("Closing connection");
        clientController.detachConnection();
        closeConnection();
        System.out.println("Done!");
    }

    public void attachController(ClientController controller){
        this.clientController = controller;
    }

    public void send(Packet packet) throws IOException{
        synchronized (this) {
            socketOut.writeObject(packet);
            socketOut.flush();
            socketOut.reset();
        }
        /*each send of packet is followed by read of model change or pong message*/
        Object fromServer = new Object();
        boolean modelChangeReceived = false;

        while (!modelChangeReceived) {
            try {
                fromServer = socketIn.readObject();
                ModelChange mc = (ModelChange) fromServer;
                clientController.changeModel(mc);
                modelChangeReceived = true;
            } catch (ClassCastException | ClassNotFoundException | InvalidParameterException e) {
                /*server has sent wrong object type*/
                try {
                    if (!fromServer.equals("pong")){
                        throw new IllegalArgumentException();
                    }
                }
                catch (ClassCastException | IllegalArgumentException e2){
                    //client didn't receive model change neither pong message during its game phase
                    //anomalous server behaviour
                    this.close();
                }
            }
        }
    }

    public void init() throws IOException {

        ObjectOutputStream socketOut = new ObjectOutputStream(socket.getOutputStream());
        socketOut.flush();

        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());

        //use stdin just for easiness
        Scanner stdin = new Scanner(System.in);

        String fromServer = socketIn.readUTF();
        //new Thread(new ConnectionTracker(this, socketOut, socketIn)).start();

        while (!fromServer.equals("start")) {
            //if the pong message from server was received , client should wait for the next different message
            while (fromServer.equals("pong")){
                fromServer = socketIn.readUTF();
            }
            if (fromServer.equals("config")) {
                System.out.println("ClientConnection says: config started " + fromServer);
                //let client insert a configuration
                /*ask view to print the messages and request input*/
                boolean inputCorrect = false;
                while (!inputCorrect) {
                        int numOfPlayers = clientController.askNumOfPlayers();
                        //send configurations to the server
                        //guarantee that ConnectionTracker doesn't write in socket at the same time
                        synchronized (this) {
                                socketOut.writeObject((Object)numOfPlayers);
                                socketOut.flush();
                                socketOut.reset();
                            }

                        fromServer = socketIn.readUTF();

                        //if the pong message from server was received , client should wait for the next different message
                        while(fromServer.equals("pong")){
                            fromServer = socketIn.readUTF();
                        }
                        if (fromServer.equals("ok")) {
                            inputCorrect = true;
                            System.out.println("ClientConnection says: Client received from server:" + fromServer);
                        } else {
                            inputCorrect = false;
                            System.out.println("ClientConnection says: Error from server received:" + fromServer);
                            continue;
                        }
                }

                inputCorrect=false;

                while (!inputCorrect) {

                    String advancedSettings = clientController.askAdvancedSettings();

                        //connectionTracker and ClientConnection should not write in socket at the same time
                        synchronized (this) {
                            socketOut.writeObject(advancedSettings);
                            socketOut.flush();
                            socketOut.reset();
                        }
                        fromServer = socketIn.readUTF();
                        while (fromServer.equals("pong")) {
                            fromServer = socketIn.readUTF();
                        }
                        if (fromServer.equals("ok")) {
                            inputCorrect = true;
                            System.out.println("Client Connection says: Client received from server: " + fromServer);
                        } else {
                            inputCorrect = false;
                            System.out.println("Client Connection says: Error from server received: \n" + fromServer);
                            continue;
                        }

                }

            }
            else if (fromServer.equals("name")) {
                boolean inputCorrect = false;
                while (!inputCorrect) {
                    name = clientController.askName();

                    //connectionTracker and ClientConnection should not write in socket at the same time
                    synchronized (this) {
                        socketOut.writeObject(name);
                        socketOut.flush();
                        socketOut.reset();
                    }

                    //Server added me to lobby if mu name is ok
                    Object lobbyChange = new Object();

                    boolean waitingLobbyChange = true;
                    while (waitingLobbyChange) {
                        try {
                            lobbyChange = socketIn.readObject();
                            clientController.changeModel((ModelChange) lobbyChange);
                            inputCorrect = true;
                        }
                        catch (ClassCastException e) {
                            try {
                                fromServer = (String) lobbyChange;
                                if (fromServer.equals("pong")) {
                                    continue;
                                }
                                else if (fromServer.equals("start")){
                                    waitingLobbyChange = false;
                                }
                                else{
                                    clientController.printMessage("Error from server received: \n" + fromServer);
                                    waitingLobbyChange = false;
                                }

                            } catch (ClassCastException e2) {
                                System.out.println("Client Connection says: error class cast ex");
                            }
                        }
                        catch (ClassNotFoundException e){
                            System.out.println("Client Connection says: error class not found ex");
                        }
                    }
                }
            }
            if (fromServer.equals("start")) {
                clientController.setClientName(name);
                Object gameBoardChange = new Object();
                boolean waitingGameBoardChange = true;
                while (waitingGameBoardChange) {
                    try {
                        gameBoardChange = socketIn.readObject();
                        clientController.changeModel((ModelChange) gameBoardChange);
                        waitingGameBoardChange = false;
                    } catch (ClassCastException e) {
                        try {
                            fromServer = (String) gameBoardChange;
                            if (fromServer.equals("pong")) {
                                continue;
                            } else {
                                System.out.println("ClientConnection says: Error from server received: \n" + fromServer);
                                waitingGameBoardChange = false;
                            }

                        } catch (ClassCastException e2) {
                            System.out.println("ClientConnection says: Error from server received: \n" + fromServer);
                        }
                    } catch (ClassNotFoundException e) {
                        System.out.println("ClientConnection says: error class not found ex");
                    }

                    clientController.startGame();
                    //server ready
                }
            }
            else {
                fromServer = socketIn.readUTF();
            }
        }
    }
}
