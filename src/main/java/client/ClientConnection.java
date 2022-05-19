package client;

import client.controller.ClientController;
import modelChange.ModelChange;
import packets.Packet;

import java.io.*;
import java.net.Socket;
import java.util.Locale;
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
        socketOut.reset();
        socketOut.writeObject(packet);
        socketOut.flush();
        /*each send of packet is followed by read of model change*/
        try {
            ModelChange mc = (ModelChange) socketIn.readObject();
            clientController.changeModel(mc);
        } catch (ClassCastException | ClassNotFoundException e) {
            /*server has sent wrong object type*/
            e.printStackTrace();
        }
    }

    public void init() throws IOException {

        ObjectOutputStream socketOut = new ObjectOutputStream(socket.getOutputStream());
        socketOut.flush();

        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());

        //use stdin just for easiness
        Scanner stdin = new Scanner(System.in);

        String fromServer = socketIn.readUTF();

        while (!fromServer.equals("start")) {
            if (fromServer.equals("config")) {
                System.out.println("config started " + fromServer);
                //let client insert a configuration
                /*ask view to print the messages and request input*/
                boolean inputCorrect = false;
                while (!inputCorrect) {
                    try {
                        System.out.println("Please insert number of players:\n");
                        int numOfPlayers = Integer.parseInt(stdin.nextLine());
                        if (numOfPlayers < 2 || numOfPlayers > 4)
                            throw new IllegalArgumentException("Incorrect number of players value. Please try again");

                        //send configurations to the server
                        socketOut.reset();
                        socketOut.writeInt(numOfPlayers);
                        socketOut.flush();

                        fromServer = socketIn.readUTF();
                        if (fromServer.equals("ok")) {
                            inputCorrect = true;
                            System.out.println("Client received from server: " + fromServer);
                        } else {
                            inputCorrect = false;
                            System.out.println("Error from server received: \n" + fromServer);
                            continue;
                        }

                        System.out.println("Do you want to play with advanced settings ? y/yes n/no:\n");
                        String advancedSettings = stdin.nextLine();
                        /*-------------*/

                        if (advancedSettings.toLowerCase(Locale.ROOT).equals("y"))
                            advancedSettings = "true";
                        else if (advancedSettings.toLowerCase(Locale.ROOT).equals("n"))
                            advancedSettings = "false";
                        else
                            throw new IllegalArgumentException("Incorrect advanced settings response. Please try again");
                        inputCorrect = true;



                        socketOut.reset();
                        socketOut.writeUTF(advancedSettings);
                        socketOut.flush();

                        fromServer = socketIn.readUTF();
                        if (fromServer.equals("ok")) {
                            inputCorrect = true;
                            System.out.println("Client received from server: " + fromServer);
                        } else {
                            inputCorrect = false;
                            System.out.println("Error from server received: \n" + fromServer);
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Incorrect number of players value. Please try again");
                    } catch (IllegalArgumentException e) {
                        System.out.println(e);
                    }

                }

            } else if (fromServer.equals("name")) {
                boolean inputCorrect = false;
                while (!inputCorrect) {
                    System.out.println("Please insert name of player:\n");
                    name = stdin.nextLine();

                    socketOut.reset();
                    socketOut.writeUTF(name);
                    socketOut.flush();

                    fromServer = socketIn.readUTF();
                    if (fromServer.equals("ok")) {
                        inputCorrect = true;
                        System.out.println("\nClient received from server : " + fromServer + "\n");
                    } else {
                        inputCorrect = false;
                        System.out.println("\nError from server received: " + fromServer + "\n");
                    }
                }
            }
            else if (fromServer.equals("start")) {
                clientController.setClientName(name);
                System.out.println("Client sent name succesfully: ");
            }

            fromServer = socketIn.readUTF();
        }
    }
}
