package model;

import exceptions.NoEnoughCoinsException;
import exceptions.NumOfStudentsExceeded;
import model.*;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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

    public void moveStudentToIsland(Color studentColor, Island island) {
        schoolBoard.moveStudentToIsland(studentColor, island);
    }

    @Override
    public int compareTo(Object comparePlayer) {
        if (comparePlayer instanceof Player) {
            int compareRank = ((Player) comparePlayer).playedAssistant.getRank();
            /* For Descending order*/
            return compareRank - this.playedAssistant.getRank();
        }
        else throw new IllegalArgumentException();
    }

    public TowerColor getTowerColor() {
        return towerColor;
    }

    public ArrayList<Color> getProfessorsColor() {
        return schoolBoard.getProfessorsColor();
    }

    public void moveStudentToDining(Color studentColor) throws NumOfStudentsExceeded {
        schoolBoard.moveStudentToDining(studentColor);
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

    public List<Assistant> getAssistants(){
        return Arrays.stream(assistants).toList();
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
}
