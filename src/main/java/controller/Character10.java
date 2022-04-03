package controller;

import exceptions.NoEnoughStudentsException;
import model.Color;

import java.util.ArrayList;

/** moves selected student to currentPlayer dining*/

public class Character10 extends Character{
    private ArrayList<Color> students;
    protected Color selectedStudent;

    @Override
    public void initialFill(Game game){
        super.initialFill(game);
        for(int i=0;i<4;i++){
            students.add(game.getStudent());
        }
    }

    @Override
    public void setSelectedStudent(Color selectedStudent) throws NoEnoughStudentsException {
        if(students.contains(selectedStudent)) {
            this.selectedStudent = selectedStudent;
        }
        else throw new NoEnoughStudentsException();
    }

    @Override
    public void execute() {
        game.addStudentToDining(game.currentPlayer, selectedStudent);
        students.remove(selectedStudent);
        students.add(game.getStudent());
    }
}
