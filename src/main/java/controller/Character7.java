package controller;

import model.Color;

import java.util.ArrayList;
import java.util.List;

public class Character7 extends Character {

    /*Swaps selectedStudents from Dining with toBeSwappedStudents from Entrance in currentPlayer`s schoolBoard */
    private List<Color> selectedStudents;
    private List<Color> toBeSwappedStudents;

    public void setSelectedStudents(ArrayList<Color> selectedStudents){
        this.selectedStudents = selectedStudents;
    }

    public void setToBeSwappedStudents(ArrayList<Color> toBeSwappedStudents){
        this.toBeSwappedStudents = toBeSwappedStudents;
    }

    public void execute(){
        if (selectedStudents.size() != toBeSwappedStudents.size())
            throw new UnsupportedOperationException("2 lists don`t have the same size");

        for(int i = 0; i < selectedStudents.size(); i++){
            Color studentToEntrance = selectedStudents.get(i);
            game.currentPlayer.removeStudentFromDining(studentToEntrance);

            Color studentToDining = toBeSwappedStudents.get(i);
            game.currentPlayer.removeStudentFromEntrance(studentToDining);

            game.currentPlayer.addStudentToEntrance(studentToEntrance);
            game.currentPlayer.addStudentToDining(studentToDining);
        }

    }

}
