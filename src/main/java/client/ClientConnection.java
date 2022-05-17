package client;

import client.controller.ClientController;
import modelChange.ModelChange;
import packets.Packet;

import java.io.*;
import java.net.Socket;
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
        closeConnection();
        clientController.detachConnection();
        System.out.println("Done!");
    }

    public void attachController(ClientController controller){
        this.clientController = controller;
    }

    public void send(Packet packet) throws IOException{
        socketOut.writeObject(packet);
        socketOut.flush();
        try {
            ModelChange mc = (ModelChange) socketIn.readObject();
            clientController.changeModel(mc);
        } catch (ClassCastException | ClassNotFoundException e) {
            /*client has sent wrong object type*/
            e.printStackTrace();
        }
    }

    public void init() throws IOException {

        ObjectOutputStream socketOut = new ObjectOutputStream(socket.getOutputStream());
        socketOut.flush();

        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());

        //use stdin stdout just for easiness
        Scanner stdin = new Scanner(System.in);
        String fromServer = socketIn.readUTF();
        System.out.println(fromServer);
        System.out.println(fromServer.equals("config"));
        if (fromServer.equals("config")) {
            System.out.println("config started" + fromServer);
            //let client insert a configuration
            while (true) {
                try {
                    /*ask view to print the messages and request input*/
                    System.out.println("Please insert number of players:\n");
                    int numOfPlayer = Integer.parseInt(stdin.nextLine());
                    System.out.println("Do you want to play with advanced settings ? y/yes n/no:\n");
                    String advancedSettings = stdin.nextLine();
                    /*-------------*/

                    if (advancedSettings.equals("y"))
                        advancedSettings = "true";
                    else if (advancedSettings.equals("n"))
                        advancedSettings = "false";
                    else
                        throw new IllegalArgumentException("Incorrect advanced settings response");

                    //send configurations to the server
                    socketOut.writeInt(numOfPlayer);
                    socketOut.flush();
                    socketOut.writeUTF(advancedSettings);
                    socketOut.flush();
                } catch (IllegalArgumentException e) {
                    System.out.println("Error. Info: " + e.getMessage() + "\nTry again!");
                }
            }

        } else if (fromServer.equals("name")) {
            while (fromServer.equals("name")) {
                System.out.println("Please insert name of player:\n");
                name = stdin.nextLine();
                socketOut.writeUTF(name);
                socketOut.flush();
                fromServer = socketIn.readUTF();
                System.out.println("Client received from client: " + fromServer);
            }
            if (!fromServer.equals("name") && !fromServer.equals("start")) {
                System.out.println("Error:\n");
                this.close();
            }
        } else if (!fromServer.equals("start")) {
            System.out.println("Error:\n");
            this.close();
        }
        if (fromServer.equals("start")) {
            clientController.setClientName(name);
            System.out.println("Client sent name succesfully: ");
        }
    }
}
