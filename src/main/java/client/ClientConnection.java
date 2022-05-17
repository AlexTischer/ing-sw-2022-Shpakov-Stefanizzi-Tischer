package client;

import client.controller.ClientController;
import modelChange.ModelChange;
import packets.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
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
        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream socketOut = new ObjectOutputStream(socket.getOutputStream());

        //use stdin stdout just for easiness
        Scanner stdin = new Scanner(System.in);
        PrintWriter stdout = new PrintWriter(System.out);

        String fromServer = socketIn.readUTF();
        if (fromServer.equals("config")){
            //let client insert a configuration
            while (true){
                try {
                    /*ask view to print the messages and request input*/
                    stdout.println("Please insert number of players:\n");
                    int numOfPlayer = Integer.parseInt(stdin.nextLine());
                    stdout.println("Do you want to play with advanced settings ? y/yes n/no:\n");
                    String advancedSettings = stdin.nextLine();
                    /*-------------*/

                    if (advancedSettings.equals("y"))
                        advancedSettings = "true";
                    else if(advancedSettings.equals("n"))
                        advancedSettings = "false";
                    else
                        throw new IllegalArgumentException("Incorrect advanced settings response");

                    //send configurations to the server
                    socketOut.writeInt(numOfPlayer);
                    socketOut.flush();
                    socketOut.writeChars(advancedSettings);
                    socketOut.flush();
                }
                catch (IllegalArgumentException e){
                    stdout.println("Error. Info: " + e.getMessage() + "\nTry again!");
                }
            }

        }
        else if (fromServer.equals("name")){
            while(fromServer.equals("name")){
                stdout.println("Please insert name of player:\n");
                name = stdin.nextLine();
                socketOut.writeChars(name);
                socketOut.flush();
                fromServer = socketIn.readUTF();
            }
            if (!fromServer.equals("name") && !fromServer.equals("start")){
                stdout.println("Error:\n");
                this.close();
            }
        }
        else if(!fromServer.equals("start")){
            stdout.println("Error:\n");
            this.close();
        }
        if(fromServer.equals("start"))
            clientController.setClientName(name);
    }
}
