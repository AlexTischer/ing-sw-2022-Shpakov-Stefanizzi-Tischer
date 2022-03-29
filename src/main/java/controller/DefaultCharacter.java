package controller;

import exceptions.NoEntryException;
import model.*;

import java.util.ArrayList;

public class DefaultCharacter {
    protected Game game;
    private int cost;

    public int calculateInfluence(Island island, int islandNumber) throws NoEntryException{
        if (!island.getNoEntry()){
            int score = 0;
            for (Color color: game.currentPlayer.getProfessorsColor()){
                score += island.getNumOfStudents(color);
            }
            if (game.currentPlayer.getTowerColor().equals(island.getTowersColor())){
                score += island.getNumOfTowers();
            }
            return score;
        }
        else {
            game.setNoEntry(islandNumber,false);
            throw new NoEntryException();
        }
    }

    public void reassignProfessor(Color professorColor){

    }

    public boolean moveMotherNature(int steps){
        return (game.currentPlayer.getPlayedAssistant().getRank()>steps);
    }

    public void initialFill(Game game){
        this.game=game;
    }

    public void execute(){

    }

    public void buy(){

    }

    public int getCost(){
        return cost;
    }

    public void setSelectedStudent(Color selectedStudent){
        throw new UnsupportedOperationException();
    }

    public void setSelectedIsland(int selectedIsland){
        throw new UnsupportedOperationException();
    }

    public void setSelectedStudents(ArrayList<Color> selectedStudents){
        throw new UnsupportedOperationException();
    }

    public void setToBeSwappedStudents(ArrayList<Color> toBeSwappedStudents){
        throw new UnsupportedOperationException();
    }

    public void addEntryTile(){
        throw new UnsupportedOperationException();
    }

}