package client.controller;

import client.ClientConnection;
import client.model.ClientPlayer;
import client.view.View;
import client.model.ClientGameBoard;
import exceptions.EndOfChangesException;
import exceptions.RepeatedAssistantRankException;
import exceptions.WrongActionException;
import modelChange.ModelChange;
import packets.*;
import server.model.Assistant;
import server.model.Color;
import server.model.Player;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientController {
    private View view;
    private ClientGameBoard gameBoard;
    private ClientConnection connection;
    private boolean connectionActive;
    private boolean characterActivated = false;

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

    public void changeModel(ModelChange change){
        change.execute(gameBoard);
    }

    public void setClientName(String clientName){
        gameBoard.setClientName(clientName);
    }

    public void startTurn(){
        if(gameBoard.getCurrentPlayerName().equals(gameBoard.getClientName())){
            //it is this client's turn
            System.out.println("Client Controller says: this is my turn - " + connection.getName());
            if(gameBoard.getPlayer(gameBoard.getCurrentPlayerName()).getPlayedAssistant()==null){
                // if client's player does not have a played Assistant, it means it has to be set and we are in planning phase.
                planningPhase();
            }
            else {
                actionPhase();
            }
        }
        else{
            while(!gameBoard.getCurrentPlayerName().equals(gameBoard.getClientName())) {
                try {
                    connection.waitModelChange();
                }
                catch (IOException e) {
                    System.out.println("ClientController says: closing connection due IOException");
                    connection.close();
                }
                catch (EndOfChangesException e) {
                    System.out.println("ClientController says: I have received and caught EndOfChangesException");

                    //Once all the changes for a client move have been received, it's possible to show them on the View.
                    gameBoard.showOnView();
                }
            }
        }
    }

    private boolean checkAssistant(int assistantRank, ClientPlayer player){
        Set<Integer> playedAssistantsRanks = gameBoard.getPlayers().stream().filter(p -> p!=player && p.getPlayedAssistant()!=null).
                map(p -> p.getPlayedAssistant().getRank()).collect(Collectors.toSet());

        /*if a player wants to play rank not contained in his hand, then return false*/
        if (!player.getAssistantsRanks().contains(assistantRank))
            return false;

        /*if a player decides to play an assistant with rank already played by someone,
        it is allowable only when player has no other options*/
        if (playedAssistantsRanks.contains(assistantRank)){
            for (int rank: player.getAssistantsRanks()) {
                /*if player has an assistant with rank not yet played*/
                if (!playedAssistantsRanks.contains(rank))
                    return false;
            }
        }
        return true;
    }

    public void useAssistant(){
        int assistantRank = view.askAssistant();

        //checking if assistant rank is available
        if(gameBoard.getPlayer(gameBoard.getCurrentPlayerName()).getAssistants()[assistantRank]!=null){

            //checking if another player has already played the same rank
            boolean alreadyPlayed = checkAssistant(assistantRank,gameBoard.getPlayer(gameBoard.getCurrentPlayerName()));

            //if not, generate the Packet
            if(alreadyPlayed) {
                Packet packet = new UseAssistantPacket(assistantRank);
                try {
                    this.connection.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                throw new RepeatedAssistantRankException();
            }
        }
        else{
            throw new InvalidParameterException("You don't have this assistant");
        }
    }

    public void planningPhase() {
        System.out.println("ClientController says: " +gameBoard.getClientName() + " is in planning phase");
        boolean correctAssistant = false;
        while(!correctAssistant) {
            try {
                useAssistant();
                correctAssistant = true;
                System.out.println("Assistant selected correctly");
            }
            catch (InvalidParameterException | RepeatedAssistantRankException e){
                printMessage(e.getMessage());
            }
        }
    }

    private void moveStudents(){
        int studentMoves = 0;
        Color studentColor;
        boolean correctStudent;
        boolean correctDestination;

        while (studentMoves < (gameBoard.getPlayers().size() == 3? 4: 3)){
            correctStudent = false;
            correctDestination = false;

            if (view.chooseActionStudent(characterActivated) == 1) {

                //client has chosen to move a student
                while (!correctStudent) {

                    //asking the color of the student
                    studentColor = view.askStudentColor();

                    //checking if client has the selected student
                    if (gameBoard.getPlayer(gameBoard.getCurrentPlayerName()).getSchoolBoard().getEntrance().contains(studentColor)) {

                        correctStudent = true;
                        //asking the destination for the student
                        while (!correctDestination) {
                            int destination = view.askStudentDestination();

                            //if destination == 0, move the student to dining room
                            if (destination == 0) {
                                try {
                                    //TODO check if dining room is full and handle any other exception received from server
                                    connection.send(new MoveStudentToDiningPacket(studentColor));
                                    correctDestination = true;
                                    studentMoves++;
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

                            //if destination is [1-12]
                            } else {
                                //check if the island with the given destination exist
                                if(gameBoard.getIslands().size()>=destination){
                                    try {
                                        //decrement destination because on server side counting starts from 0
                                        connection.send(new MoveStudentToIslandPacket(studentColor, destination-1));
                                        correctDestination = true;
                                        studentMoves++;
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }

                                //if not, retry
                                else{
                                    System.out.println("This island does not exist, select another destination");
                                }
                            }
                        }
                    } else {
                        System.out.println("Student does not exist, try again");
                    }
                }
            }


            //TODO ask for character activation
            //chooseActionStudent returned 2
            else{
                buyAndActivateCharacter();
            }
        }
    }

    private void buyAndActivateCharacter(){}

    private void moveMotherNature() {
        if (view.chooseActionMotherNature(characterActivated) == 1) {
            boolean correctMotherNature = false;
            while(!correctMotherNature){
                System.out.println("Inside move mother nature while loop");
                synchronized (this) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        //TODO ask for character activation
        //chooseActionMotherNature returned 2
        else {
            buyAndActivateCharacter();
        }
    }
    public void actionPhase() {
        System.out.println("ClientController says: action phase");

        //TODO ask view to ask user to choose actions
        moveStudents();
        moveMotherNature();

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
