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

    }

    public void moveStudentToDining(Color studentColor){

    }

    public void useCloud(int cloudNumber){

    }

    public void moveMotherNature(int steps){

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

    public void startTurn(){
        if(gameBoard.getCurrentPlayerName().equals(gameBoard.getClientName())){
            //my turn
            if(gameBoard.getPlayer(gameBoard.getCurrentPlayerName()).getPlayedAssistant()==null){
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
                } catch (IOException e) {
                    System.out.println("ClientController says: closing connection due to exception in receiving updates");
                    connection.close();
                } catch (EndOfChangesException e) {
                    continue;
                }
            }
        }
    }

    public void planningPhase() {
        System.out.println("ClientController says: Starting while(true) loop in planning phase to keep client alive");
        boolean correctAssistant = false;
        while(!correctAssistant) {
            try {
                useAssistant();
                correctAssistant = true;
                System.out.println("Assistant selected correctly");
            }catch (InvalidParameterException e){
                printMessage(e.getMessage());
            }catch (RepeatedAssistantRankException e){
                printMessage(e.getMessage());
            }
        }
    }

    private void moveStudents(boolean characterActivated){
        int studentMoves = 0;
        Color studentColor;
        boolean correctStudent;
        boolean correctDestination;

        while (studentMoves<=3){
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
                                    //TODO check if dining room is full
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
                                        connection.send(new MoveStudentToIslandPacket(studentColor, destination));
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

            }

        }
    }

    public void actionPhase() {
        System.out.println("ClientController says: Starting while(true) loop in action phase to keep client alive");
        boolean characterActivated = false;
        //TODO ask view to ask user to choose actions
        moveStudents(characterActivated);

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
