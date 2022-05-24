package client.controller;

import client.ClientConnection;
import client.View;
import client.model.ClientGameBoard;
import modelChange.ModelChange;
import packets.*;
import server.model.Color;

import java.io.IOException;
import java.util.ArrayList;

public class ClientController implements GameForClient{
    private View view;
    private ClientGameBoard gameBoard;
    private ClientConnection connection;
    private boolean connectionActive;

    public void attachConnection(ClientConnection connection){
        this.connection = connection;
    }

    public void attachModel(ClientGameBoard gameBoard){
        this.gameBoard = gameBoard;
    }

    public void attachView(View view){
        this.view = view;
    }

    //gets executed when connection is lost
    public void detachConnection(){
        this.connection = null;
        connectionActive = false;
    }

    //TODO fill methods with packet creation
    public void moveStudentToIsland(Color studentColor, int islandNumber){
        Packet packet = new MoveStudentToIslandPacket();
        /*...*/
        try {
            this.connection.send(packet);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void moveStudentToDining(Color studentColor){

    }

    public void useCloud(int cloudNumber){

    }

    public void moveMotherNature(int steps){

    }

    public void useAssistant(int assistantRank){

    }

    public void buyCharacter(int characterNumber){

    }

    public void activateCharacter(int islandNumber){

    }

    public void activateCharacter(ArrayList<Color> toBeSwappedStudents, ArrayList<Color> selectedStudents){

    }

    public void activateCharacter(Color color, int islandNumber){

    }

    public void activateCharacter(Color color){

    }

    public void changeModel(ModelChange change){

        try {
            change.execute(gameBoard);
        }
        catch (Exception e){
            //TODO implement handling of exception raised by server model
            e.printStackTrace();
        }
    }

    public void setClientName(String clientName){
        gameBoard.setClientName(clientName);
    }

    public void startGame(){
        view.printMessage("Ready to start the game!");
    }

    public void planningPhase() {
        System.out.println("ClientController says: Starting while(true) loop to keep client alive");
        while(true){}
        //TODO ask view to ask user to choose Assistant
    }

    public void actionPhase() {
        //TODO ask view to ask user to choose actions
    }

    public int askNumOfPlayers() {
        int numOfPlayers;
        numOfPlayers = view.askNumOfPlayers();
        while (!( numOfPlayers >= 2 && numOfPlayers <= 4)){
            view.printMessage("Incorrect number of players value. Please try again");
            numOfPlayers = view.askNumOfPlayers();
        }
        return numOfPlayers;
    }

    public String askAdvancedSettings(){
        return view.askAdvancedSettings();
    }

    public String askName(){
        return view.askName();
    }

    public void printMessage(String message){
        view.printMessage(message);
    }
}
