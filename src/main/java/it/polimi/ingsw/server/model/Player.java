package it.polimi.ingsw.server.model;

import it.polimi.ingsw.exceptions.NoEnoughCoinsException;
import it.polimi.ingsw.exceptions.NoEnoughTowersException;
import it.polimi.ingsw.exceptions.NumOfStudentsExceeded;
import it.polimi.ingsw.exceptions.StudentNotFoundException;
import it.polimi.ingsw.server.controller.Game;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**This class represents a player
 * <ul>
 *     Player has
 *     <li>{@link #name} represents name of this player</li>
 *     <li>{@link #schoolBoard} owned by this player</li>
 *     <li>{@link #coins} number of coins that player owns</li>
 *     <li>{@link #towerColor} color of the towers that player owns</li>
 *     <li>{@link #assistantType} type of the players' assistants</li>
 *     <li>{@link #assistants} assistants' hand of this player</li>
 *     <li>{@link #playedAssistant} the assistant played in current turn</li>
 *     <li>{@link #isActive()} tells whether the client associated with this player is connected to the server</li>
 * </ul>
 * */
public class Player implements Comparable{
    private String name;
    private SchoolBoard schoolBoard;
    private int coins;
    private TowerColor towerColor;
    private AssistantType assistantType;
    private Assistant[] assistants;
    private Assistant playedAssistant;
    private boolean isActive;


    public Player(String name, TowerColor towerColor, AssistantType assistantType, int numOfTowers) {
        this.name = name;
        this.towerColor = towerColor;
        this.assistantType = assistantType;
        this.assistants = new Assistant[10];
        this.coins = 0;
        schoolBoard = new SchoolBoard(this.towerColor, numOfTowers);
        this.isActive = true;
    }

    /**Calls {@link SchoolBoard#addStudentToEntrance(Color)}*/
    public void addStudentToEntrance(Color studentColor) {
        schoolBoard.addStudentToEntrance(studentColor);
    }

    /**Calls {@link SchoolBoard#removeStudentFromEntrance(Color)}
     * @throws StudentNotFoundException  if there is no student of such color in entrance*/
    public void removeStudentFromEntrance(Color studentColor) {
        schoolBoard.removeStudentFromEntrance(studentColor);
    }

    /**Calls {@link SchoolBoard#addStudentToDining(Color)}
     * @throws NumOfStudentsExceeded  if there is no free space in dining room for that color*/
    public void addStudentToDining(Color studentColor){
        schoolBoard.addStudentToDining(studentColor);
    }

    /**Calls {@link SchoolBoard#removeStudentFromDining(Color)}
     * @throws StudentNotFoundException  if there is no student of such color in the dining room*/
    public void removeStudentFromDining(Color studentColor) {
        schoolBoard.removeStudentFromDining(studentColor);
    }

    /**Calls {@link SchoolBoard#addProfessor(Color)}*/
    public void addProfessor(Color color) {
        schoolBoard.addProfessor(color);
    }

    /**Calls {@link SchoolBoard#removeProfessor(Color)}*/
    public void removeProfessor(Color color) {
        schoolBoard.removeProfessor(color);
    }

    /** Used by a comparator to sort players in ascending order according to {@link #playedAssistant} rank value
     * <p>If {@link #playedAssistant} of any player is null and we are in the moment we need to sort players, then
     * it means that client associated to a player was disconnected from server. Such player is moved to the end
     * of a players list in {@link Game}</p>
     * @throws NullPointerException  if comparePlayer is null
     * @throws IllegalArgumentException  if comparePlayer is not an instance of {@link Player} class
     * */
    @Override
    public int compareTo(Object comparePlayer) {
        if (comparePlayer instanceof Player) {
            /* For ascending order*/
            //if this player is not active then he moves to the end of player array
            if (playedAssistant == null)
                return 1;

            //if player to compare is not active then he moves to the end of players array
            if (((Player) comparePlayer).playedAssistant == null)
                return -1;

            int compareRank = ((Player) comparePlayer).playedAssistant.getRank();

            return this.playedAssistant.getRank() - compareRank;
        }
        else throw new IllegalArgumentException();
    }

    /**Initialized player's assistants' hand*/
    public void setAssistants(ArrayList<Assistant> assistants){

        for (int i=0 ; i < 10; i++)
            this.assistants[i] = assistants.get(i);
    }

    /**Sets {@link #playedAssistant} given a rank from one to ten.
     * <p>Sets {@link #playedAssistant} to null if rank is equal to zero</p>
     * @param rank  the rank of the assistant player want to play
     * @throws InvalidParameterException  if assistant with this rank was already played*/
    public void setPlayedAssistant(int rank) {
        if (rank != 0 && assistants[rank - 1] != null) {
            playedAssistant = assistants[rank - 1];
            assistants[rank - 1] = null;
        } else if (rank == 0) {
            playedAssistant = null;
        } else throw new InvalidParameterException("You already played this assistant!");
    }

    /**Changes {@link #isActive} in case client was disconnected or reconnected
     * <p>If client gets disconnected ( status = false ) then {@link #playedAssistant} is set to null</p>
     * <p>That serves to make relative client wait for the next turn in case of reconnection</p>
     * @param status  the new status of this player*/
    public void changeStatus(boolean status){
        this.isActive = status;

        //take away assistant from not active player hand
        if (!isActive)
            playedAssistant = null;
    }

    public boolean isActive(){
        return isActive;
    }

    /**
     * @throws NoEnoughCoinsException  if number of coins to remove is greater than number of coins that player has*/
    public void removeCoins(int coins){
        if (this.coins >= coins) {
            this.coins -= coins;
        } else throw new NoEnoughCoinsException();
    }

    /**
     * @param coins  number of coins to add to player*/
    public void addCoins(int coins) {
        this.coins += coins;
    }

    /**
     * @return list of available assistants' ranks*/
    public List<Integer> getAssistantsRanks(){
        return Arrays.stream(assistants).filter(a -> a!=null).map(a -> a.getRank()).toList();
    }

    /**Calls {@link SchoolBoard#checkEmptyTowers()}*/
    public boolean checkEmptyTowers(){
        return schoolBoard.checkEmptyTowers();
    }

    /**Calls {@link SchoolBoard#addTower()}*/
    public void addTower() {
        schoolBoard.addTower();
    }

    /**Calls {@link SchoolBoard#removeTower()}
     * @throws NoEnoughTowersException  if there are no towers on the school board*/
    public void removeTower() {
        schoolBoard.removeTower();
    }

    /**
     * @return  name of this player*/
    public String getName() {
        return name;
    }

    /**Calls {@link SchoolBoard#getNumOfStudentsInDining(Color)}*/
    public int getNumOfStudentsInDining(Color color) {
        return schoolBoard.getNumOfStudentsInDining(color);
    }

    public Assistant getPlayedAssistant() {
        return playedAssistant;
    }

    public AssistantType getAssistantType(){ return assistantType; }

    /**Calls {@link SchoolBoard#getNumOfTowers()}*/
    public int getNumOfTowers() {
        return schoolBoard.getNumOfTowers();
    }

    /**@return  school board associated with this player*/
    public SchoolBoard getSchoolBoard() {
        return schoolBoard;
    }

    /**Calls {@link SchoolBoard#getNumOfStudentsInEntrance()}*/
    public int getNumOfStudentsInEntrance(){
        return schoolBoard.getNumOfStudentsInEntrance();
    }

    /**Calls {@link SchoolBoard#getStudentsInEntrance()}*/
    public ArrayList<Color> getStudentsInEntrance(){
        return schoolBoard.getStudentsInEntrance();
    }

    public Assistant[] getAssistants() {
        return assistants;
    }

    public TowerColor getTowerColor() {
        return towerColor;
    }

    public int getCoins() {
        return coins;
    }

    /**Calls {@link SchoolBoard#getProfessorsColor()}*/
    public ArrayList<Color> getProfessorsColor() {
        return schoolBoard.getProfessorsColor();
    }

}
