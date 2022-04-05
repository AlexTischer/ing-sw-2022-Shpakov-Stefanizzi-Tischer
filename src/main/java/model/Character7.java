package model;

import exceptions.NoEnoughCoinsException;

import java.util.ArrayList;
import java.util.List;

public class Character7 extends Character {

    /*Swaps selectedStudents from Dining with toBeSwappedStudents from Entrance in currentPlayer`s schoolBoard */
    private List<Color> selectedStudents;
    private List<Color> toBeSwappedStudents;
    private int cost = 1;

    public void setSelectedStudents(ArrayList<Color> selectedStudents){
        this.selectedStudents = selectedStudents;
    }

    public void setToBeSwappedStudents(ArrayList<Color> toBeSwappedStudents){
        this.toBeSwappedStudents = toBeSwappedStudents;
    }

    public void execute(){
        if (selectedStudents.size() != toBeSwappedStudents.size())
            throw new IllegalArgumentException("2 lists don`t have the same size");

        for(int i = 0; i < selectedStudents.size(); i++){
            Color studentToEntrance = selectedStudents.get(i);
            game.getCurrentPlayer().removeStudentFromDining(studentToEntrance);

            Color studentToDining = toBeSwappedStudents.get(i);
            game.getCurrentPlayer().removeStudentFromEntrance(studentToDining);

            game.getCurrentPlayer().addStudentToEntrance(studentToEntrance);
            game.getCurrentPlayer().addStudentToDining(studentToDining);
        }

    }
    @Override
    public void buy() throws NoEnoughCoinsException {
        game.getCurrentPlayer().removeCoins(cost);
        cost = 2;
    }


}
