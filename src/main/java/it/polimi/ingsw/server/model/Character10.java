package it.polimi.ingsw.server.model;

import it.polimi.ingsw.client.model.ClientCharacter;
import it.polimi.ingsw.client.model.ClientCharacter10;
import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.exceptions.NoEnoughCoinsException;
import it.polimi.ingsw.exceptions.NoEnoughStudentsException;

import java.util.ArrayList;

/** moves selected student to currentPlayer dining*/

public class Character10 extends Character {
    private ArrayList<Color> students;
    protected Color selectedStudent;
    private int cost = 2;
    private String description = "C10";

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


    @Override
    public ClientCharacter createClientCharacter(){
        ClientCharacter character = new ClientCharacter10();

        character.setCost(cost);
        character.setDescription(description);

        character.setStudents(getStudentsSlot());

        return character;
    }


    /*TEST METHODS*/
    public ArrayList<Color> getStudents(){
        return (ArrayList<Color>) students.clone();
    }

}
