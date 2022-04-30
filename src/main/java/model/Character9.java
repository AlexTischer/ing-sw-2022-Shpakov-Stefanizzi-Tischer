package model;

import controller.Game;
import exceptions.NoEnoughCoinsException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**Swaps selectedStudents in students with toBeSwappedStudents in currentPlayer Entrance **/
public class Character9 extends Character{
    private Color[] students;
    private List<Color> selectedStudents;
    private List<Color> toBeSwappedStudents;
    private int maxNumOfStudents = 6;
    private int cost = 1;

    public void initialFill(Game game){
        super.initialFill(game);
        students = new Color[maxNumOfStudents];

        for (int i = 0; i < maxNumOfStudents; i++)
            students[i] = game.getStudent();

    }

    public void setSelectedStudents(ArrayList<Color> selectedStudents){
        if(selectedStudents.size() > 3)
            throw new IllegalArgumentException("You can select at most 3 students");

        for (Color studentColor: selectedStudents)
            if (!Arrays.asList(students).contains(studentColor))
                throw new IllegalArgumentException("No such student color on the card");

        this.selectedStudents = selectedStudents;
    }

    public void setToBeSwappedStudents(ArrayList<Color> toBeSwappedStudents){
        if (toBeSwappedStudents.size() > 3)
            throw new IllegalArgumentException("You can select at most 3 students");

        this.toBeSwappedStudents = toBeSwappedStudents;
    }

    public void execute(){
        if (selectedStudents.size() != toBeSwappedStudents.size())
            throw new IllegalArgumentException("2 lists don`t have the same size");

        /*create temporary list to remove students easily*/
        List<Color> tempStudents = new ArrayList<Color>(Arrays.asList(students));

        for(int i = 0; i < selectedStudents.size(); i++){
            Color studentToEntrance = selectedStudents.get(i);
            tempStudents.remove(studentToEntrance);

            Color studentToCharacter = toBeSwappedStudents.get(i);
            game.getCurrentPlayer().removeStudentFromEntrance(studentToCharacter);

            game.getCurrentPlayer().addStudentToEntrance(studentToEntrance);
            tempStudents.add(studentToCharacter);
        }

        students = ( Color[] )tempStudents.toArray();

    }

    @Override
    public void buy() throws NoEnoughCoinsException {
        game.getCurrentPlayer().removeCoins(cost);
        cost = 2;
    }

    public Color[] getStudents(){
        return students;
    }


}
