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
    private String description = "Take 1 student from this card and place it in your dining room";

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
        game.reassignProfessor();
    }

    @Override
    public void buy() throws NoEnoughCoinsException {
        game.getGameBoard().removeCoinsFromPlayer(game.getCurrentPlayer(), cost);
        //if it's first use then we need to leave one coin on the card
        if (firstUse){
            game.getGameBoard().addCoinsToBank(cost-1);
            firstUse = false;
        }
        else {
            game.getGameBoard().addCoinsToBank(cost);
        }
        cost = 3;
    }

    @Override
    public Color[] getStudentsSlot(){
        Color[] toReturnStudents = null;
        if (students != null){
           toReturnStudents = new Color[students.size()];

            for (int i = 0; i < students.size(); i++)
                toReturnStudents[i] = students.get(i);
        }

        return toReturnStudents;
    }

    @Override
    public ClientCharacter createClientCharacter(){
        ClientCharacter10 character = new ClientCharacter10();

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
