package controller;

import exceptions.NoEnoughCoinsException;
import exceptions.NoEntryException;
import model.*;

import java.util.ArrayList;

public class Game {
    private GameBoard gameBoard;
    protected ArrayList<Player> players;
    protected Player currentPlayer;
    private Character currentCharacter;
    private Character playedCharacters[];
    private ArrayList<Assistant> playedAssistant;

    public void moveStudentToIsland(Color studentColor, int islandNumber){
        gameBoard.moveStudentToIsland(currentPlayer, studentColor, islandNumber);
    }

    public void moveStudentToDining(Color studentColor){
        removeStudentFromEntrance(studentColor);
        addStudentToDining(currentPlayer, studentColor);
    }

    protected void addStudentToEntrance(Player player){
        gameBoard.addStudentToEntrance(player, getStudent());
    }

    protected void addStudentToEntrance(Player player, Color studentColor){
        gameBoard.addStudentToEntrance(player, studentColor);
    }

    protected void addStudentToDining(Player player, Color studentColor){
        gameBoard.addStudentToDining(player, studentColor);
        reassignProfessor(studentColor);
    }

    protected void removeStudentFromEntrance(Color studentColor){
        gameBoard.removeStudentFromEntrance(currentPlayer, studentColor);
    }

    protected void removeStudentFromDining(Player player, Color studentColor){
        gameBoard.removeStudentFromDining(player, studentColor);
    }

    protected Color getStudent(){
        return gameBoard.getStudentFromBag();
    }

    protected void calculateInfluence(int islandNumber){
        try {
            gameBoard.calculateInfluence(islandNumber, currentCharacter);
        }
        catch (NoEntryException e){
            e.printStackTrace();
        }
    }

    protected void reassignProfessor(Color professorColor){
        currentCharacter.reassignProfessor(professorColor);
    }

    protected void setNoEntry(int islandNumber, boolean noEntry){
        try {
            gameBoard.setNoEntry(islandNumber, noEntry);
        }
        catch (NoEntryException e){
            for(Character c : playedCharacters){
                c.addNoEntryTile();
            }
        }
    }

    public void moveMotherNature(int steps){
        gameBoard.moveMotherNature(steps, currentCharacter);
    }

    public void buyCharacter(int characterNumber){
            try {
                currentPlayer.removeCoins(playedCharacters[characterNumber].getCost());
                playedCharacters[characterNumber].buy();
                this.currentCharacter=playedCharacters[characterNumber];
            }
            catch (NoEnoughCoinsException e) {
                /* Catch*/
            }
    }

    public void activateCharacter(int islandNumber){
        currentCharacter.setSelectedIslandNumber(islandNumber);
        currentCharacter.execute();
    }

    public void activateCharacter(ArrayList<Color> toBeSwappedStudents, ArrayList<Color> selectedStudents){
        currentCharacter.setSelectedStudents(selectedStudents);
        currentCharacter.setToBeSwappedStudents(toBeSwappedStudents);
    }

    public void activateCharacter(Color color, int islandNumber){
        currentCharacter.setSelectedStudent(color);
        currentCharacter.setSelectedIslandNumber(islandNumber);
        currentCharacter.execute();
    }

    public void activateCharacter(Color color){
        currentCharacter.setSelectedStudent(color);
        currentCharacter.execute();
    }

    public void useCloud(int cloudNumber){
        gameBoard.useCloud(currentPlayer, cloudNumber);
    }

    public void useAssistant(int assistantRank){}
    public void newTurn(){}
    public void newRound(){}


}