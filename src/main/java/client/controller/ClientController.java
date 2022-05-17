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
        gameBoard.setClientName(clientName);}

    public void pianificationPhase() {
        //TODO ask view to ask user to choose Assistant
    }

    public void actionPhase() {
        //TODO ask view to ask user to choose actions
    }

}
