package model;

import controller.Game;
import exceptions.NoEnoughCoinsException;
import exceptions.NoEnoughStudentsException;

import java.util.ArrayList;

/** moves selected student to currentPlayer dining*/

public class Character10 extends Character{
    private ArrayList<Color> students;
    protected Color selectedStudent;
    private int cost = 2;

    @Override
    public void initialFill(Game game){
        super.initialFill(game);
        students=new ArrayList<Color>();
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
        game.getGameBoard().addStudentToDining(game.getCurrentPlayer(), selectedStudent);
        students.remove(selectedStudent);
        students.add(game.getStudent());
    }

    @Override
    public void buy() throws NoEnoughCoinsException {
        game.getGameBoard().removeCoins(game.getCurrentPlayer(), cost);
        cost = 3;
    }

    /*TEST METHODS*/
    public ArrayList<Color> getStudents(){
        return (ArrayList<Color>) students.clone();
    }

}
