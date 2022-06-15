package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.model.ClientPlayer;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.model.ClientGameBoard;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.modelChange.ModelChange;
import it.polimi.ingsw.packets.*;
import it.polimi.ingsw.server.model.Color;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientController {
    private View view;
    private ClientGameBoard gameBoard;
    private ClientConnection connection;
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
    }

    public void changeModel(ModelChange change){
        try {
            change.execute(gameBoard);
        }
        catch (EndOfGameException e){
            //stamp gameBoard because endOfGameException may arrive instead of endOfChangesException
            gameBoard.showOnView();
            view.printMessage(e.getMessage());
            connection.close();
            throw new EndOfChangesException();
        }
        catch (EndOfChangesException e){
            //Once all the changes for a client move have been received, it's possible to show them on the View.
            gameBoard.showOnView();
            throw new EndOfChangesException();
        }
    }

    public void setClientName(String clientName){
        gameBoard.setGameOn(true);
        gameBoard.setClientName(clientName);
    }

    public void startTurn(){

        //setting CharacterActivated=true here means it won't ever be used. This is in order to use not advanced settings correctly.
        if(gameBoard.getPlayedCharacters()==null) {
            characterActivated=true;
        }
        else{
            characterActivated=false;
        }

        //can start the turn only if game is on
        if (isGameOn() && gameBoard.getCurrentPlayerName().equals(gameBoard.getClientName())) {
            //it is this client's turn
            System.out.println("Client Controller says: this is my turn - " + connection.getName());
            if (gameBoard.getPlayer(gameBoard.getCurrentPlayerName()).getPlayedAssistant() == null) {
                // if client's player does not have a played Assistant, it means it has to be set, and we are in planning phase.
                //TODO check for possible conflicts when this client
                // makes an action and there is another client that has been reconnected to the server
                //TODO ideally client must process any modelChange before endOfChanges arrives
                // client will see updated picture only after having completed an action when it receives endOfChanges or setCurrentPlayerName
                planningPhase();
            } else {
                actionPhase();
            }
        }
        else {
            //all clients start from here, if suddenly game is finished
            //then all clients will exit from all recursion calls because isGameOn() condition doesn't get satisfied
            //while (isGameOn()) {
                while (isGameOn() && !gameBoard.getCurrentPlayerName().equals(gameBoard.getClientName())) {
                    //continue to process model changes until I receive one that executes setCurrentPlayerName on ClientGameBoard
                    try {
                        connection.waitModelChange();
                    } catch (IOException e) {
                        System.out.println("ClientController says: closing connection due IOException");
                        connection.close();
                    } catch (EndOfChangesException e) {
                        System.out.println("ClientController says: I have received and caught EndOfChangesException");
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
        if(isGameOn() && gameBoard.getPlayer(gameBoard.getCurrentPlayerName()).getAssistants()[assistantRank-1]!=null){

            //checking if another player has already played the same rank
            boolean alreadyPlayed = checkAssistant(assistantRank,gameBoard.getPlayer(gameBoard.getCurrentPlayerName()));

            //if not, generate the Packet
            if(alreadyPlayed) {
                Packet packet = new UseAssistantPacket(assistantRank);
                try {
                    this.connection.send(packet);
                }
                catch (IOException e) {
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
        System.out.println("ClientController says: " + gameBoard.getClientName() + " is in planning phase");
        boolean correctAssistant = false;
        while(!correctAssistant) {
            try {
                //client can make any move only if game is on
                useAssistant();
                correctAssistant = true;
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

        while (studentMoves < (gameBoard.getPlayers().size() == 3? 4: 3) && isGameOn()){
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
                                }
                                catch (NumOfStudentsExceeded e){
                                    printMessage("The dining room is full for this student. Insert an island number!");
                                }
                                catch (IOException e) {
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
                                        System.out.println("Ooops. Something went wrong!");
                                        e.printStackTrace();
                                    }
                                }

                                //if not, retry
                                else{
                                    printMessage("This island does not exist, select another destination");
                                }
                            }
                        }
                    } else {
                        printMessage("Student does not exist, try again");
                    }
                }
            }
            else{
                buyAndActivateCharacter();
            }
        }
    }

    private void buyAndActivateCharacter(){

        boolean correctCharacter = false;
        int i=0;

        while(!correctCharacter && isGameOn()) {
            i = view.askCharacterNumber();
            if(i==-1){
                break;
            }
            else if(gameBoard.getPlayer(gameBoard.getClientName()).getCoins()>=gameBoard.getPlayedCharacters()[i-1].getCost()){
                correctCharacter=true;
            }
            else{
                printMessage("You don't have enough coins to buy this character, buy another one or cancel");
            }
        }
        if(correctCharacter && isGameOn()){
            try {
                connection.send(new BuyCharacterPacket(i-1));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if(correctCharacter && isGameOn()){
            try {
                ActivateCharacterPacket packet = gameBoard.getPlayedCharacters()[i-1].createPacket(view);
                connection.send(packet);
            } catch (UnsupportedOperationException e) {

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            //TODO control on client side failed
        }
    }

    private void moveMotherNature() {
        //the control is done on server
        boolean movedMN = false;
        if (view.chooseActionMotherNature(characterActivated) == 1 && isGameOn()) {
            while (!movedMN && isGameOn()) {
                int steps = view.askMotherNatureSteps();
                try {
                    connection.send(new MoveMotherNaturePacket(steps));
                    movedMN = true;
                } catch (IOException e) {
                    System.out.println("Ooops. Something went wrong!");
                    e.printStackTrace();
                } catch (IllegalArgumentException ex) {
                    printMessage(ex.getMessage());
                }
            }
        } else {
            buyAndActivateCharacter();
        }
    }

    private void useCloud(){
        boolean usedCloud = false;
        while(!usedCloud && isGameOn()) {
            if (view.chooseActionClouds(characterActivated) == 1) {
                boolean correctCloud = false;
                while (!correctCloud) {
                    int cloudNumber = view.askCloudNumber();
                    if (cloudNumber <= gameBoard.getPlayers().size() && cloudNumber > 0 && !(gameBoard.getCloud(cloudNumber-1).getStudents().isEmpty())) {
                        try {
                            connection.send(new UseCloudPacket(cloudNumber-1));
                            correctCloud = true;
                            usedCloud = true;
                        } catch (IOException e) {
                            System.out.println("Ooops. Something went wrong!");
                            e.printStackTrace();
                        }
                    }
                    else{
                        printMessage("the cloud is empty, select another cloud");
                    }

                }
            }
            else {
                buyAndActivateCharacter();
            }
        }
    }

    public void actionPhase() {
        System.out.println("ClientController says: action phase");

        moveStudents();
        moveMotherNature();
        useCloud();

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

    public boolean isGameOn(){
        return gameBoard.isGameOn();
    }

    public ClientGameBoard getGameBoard(){
        return gameBoard;
    }



}
