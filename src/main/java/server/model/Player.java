package server.model;

import exceptions.NoEnoughCoinsException;
import exceptions.NumOfStudentsExceeded;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player implements Comparable{
    private String name;
    private SchoolBoard schoolBoard;
    private int coins;
    private TowerColor towerColor;
    private AssistantType assistantType;
    private Assistant playedAssistant;
    private Assistant[] assistants;


    public Player(String name, TowerColor towerColor, AssistantType assistantType, int numOfTowers) {
        this.name = name;
        this.towerColor = towerColor;
        this.assistantType = assistantType;
        this.assistants = new Assistant[10];
        this.coins = 0;
        schoolBoard = new SchoolBoard(this.towerColor, numOfTowers);
    }

    @Override
    public int compareTo(Object comparePlayer) {
        if (comparePlayer instanceof Player) {
            int compareRank = ((Player) comparePlayer).playedAssistant.getRank();
            /* For ascending order*/
            return this.playedAssistant.getRank() - compareRank;
        }
        else throw new IllegalArgumentException();
    }

    public TowerColor getTowerColor() {
        return towerColor;
    }

    public ArrayList<Color> getProfessorsColor() {
        return schoolBoard.getProfessorsColor();
    }

    public void addStudentToEntrance(Color studentColor) {
        schoolBoard.addStudentToEntrance(studentColor);
    }

    public void removeStudentFromEntrance(Color studentColor) {
        schoolBoard.removeStudentFromEntrance(studentColor);
    }

    public void addStudentToDining(Color studentColor) throws NumOfStudentsExceeded {
        schoolBoard.addStudentToDining(studentColor);
    }

    public void removeStudentFromDining(Color studentColor) {
        schoolBoard.removeStudentFromDining(studentColor);
    }

    public void addProfessor(Color color) {
        schoolBoard.addProfessor(color);
    }

    public void removeProfessor(Color color) {
        schoolBoard.removeProfessor(color);
    }

    public int getNumOfStudentsInDining(Color color) {
        return schoolBoard.getNumOfStudentsInDining(color);
    }

    public Assistant getPlayedAssistant() {
        return playedAssistant;
    }

    public AssistantType getAssistantType(){ return assistantType; }

    public void setAssistants(ArrayList<Assistant> assistants){

        for (int i=0 ; i < 10; i++)
            this.assistants[i] = assistants.get(i);
    }

    public void setPlayedAssistantRank(int rank) throws InvalidParameterException {
        if (rank != 0 && assistants[rank - 1] != null) {
            playedAssistant = assistants[rank - 1];
            assistants[rank - 1] = null;
        } else if (rank == 0) {
            playedAssistant = null;
        } else throw new InvalidParameterException();
    }

    public void removeCoins(int coins) throws NoEnoughCoinsException {
        if (this.coins >= coins) {
            this.coins -= coins;
        } else throw new NoEnoughCoinsException();
    }

    public void addCoins(int coins) {
        this.coins += coins;
    }

    /*returns list of not null assistants` ranks*/
    public List<Integer> getAssistantsRanks(){
        return Arrays.stream(assistants).filter(a -> a!=null).map(a -> a.getRank()).toList();
    }

    public boolean checkEmptyTowers(){
        return schoolBoard.checkEmptyTowers();
    }

    /*TEST METHODS*/
    public int getNumOfStudentsInEntrance(){
        return schoolBoard.getNumOfStudentsInEntrance();
    }

    public ArrayList<Color> getStudentsInEntrance(){
        return schoolBoard.getStudentsInEntrance();
    }

    public String getName() {
        return name;
    }
    public void addTower() {
        schoolBoard.addTower();
    }

    public int getNumOfTowers() {
        return schoolBoard.getNumOfTowers();
    }

    public void removeTower() {
        schoolBoard.removeTower();
    }

    public SchoolBoard getSchoolBoard() {
        return schoolBoard;
    }

    public int getCoins() {
        return coins;
    }

    public Assistant[] getAssistants() {
        return assistants;
    }
}
