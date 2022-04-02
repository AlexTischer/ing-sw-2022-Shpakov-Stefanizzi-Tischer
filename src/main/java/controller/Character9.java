package controller;

import model.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Character9 extends Character{
    /*Swaps selectedStudents in students with toBeSwappedStudents in currentPlayer Entrance */
    private Color[] students;
    private List<Color> selectedStudents;
    private List<Color> toBeSwappedStudents;
    private int maxNumOfStudents;

    public void initialFill(Game game, int maxNumOfStudents){
        super.initialFill(game);
        this.maxNumOfStudents = maxNumOfStudents;

        students = new Color[maxNumOfStudents];

        for (int i = 0; i < getMaxNumOfStudents(); i++)
            students[i] = game.getStudent();

    }

    public void setSelectedStudents(ArrayList<Color> selectedStudents){
        for (Color studentColor: selectedStudents)
            if (!Arrays.asList(students).contains(studentColor))
                throw new IllegalArgumentException("No such student color on the card");

        this.selectedStudents = selectedStudents;
    }

    public void setToBeSwappedStudents(ArrayList<Color> toBeSwappedStudents){
        this.toBeSwappedStudents = toBeSwappedStudents;
    }

    public void execute(){
        if (selectedStudents.size() != toBeSwappedStudents.size())
            throw new UnsupportedOperationException("2 lists don`t have the same size");

        /*create temporary list to remove students easily*/
        List<Color> tempStudents = new ArrayList<Color>(Arrays.asList(students));

        for(int i = 0; i < selectedStudents.size(); i++){
            Color studentToEntrance = selectedStudents.get(i);
            tempStudents.remove(studentToEntrance);

            Color studentToCharacter = toBeSwappedStudents.get(i);
            game.currentPlayer.removeStudentFromEntrance(studentToCharacter);

            game.currentPlayer.addStudentToEntrance(studentToEntrance);
            tempStudents.add(studentToCharacter);
        }

        students = ( Color[] )tempStudents.toArray();

    }

}
