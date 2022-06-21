package it.polimi.ingsw.server.model;

import it.polimi.ingsw.client.model.ClientCharacter;
import it.polimi.ingsw.client.model.ClientCharacter1;
import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.exceptions.NoEnoughCoinsException;

import java.util.ArrayList;

/** Move selected student to selected island */
public class Character1 extends Character {

    private int id=1;
    private ArrayList<Color> students;
    protected Color selectedStudent;
    protected int selectedIslandNumber;
    private int cost = 1;
    private String description = "Take 1 Student from this card and place it on an Island of your choice";

    @Override
    public void initialFill(Game game){
        super.initialFill(game);
        students=new ArrayList<Color>();
        for(int i=0; i<4; i++){
            students.add(game.getStudent());
        }
    }

    @Override
    public void execute(){
        students.remove(selectedStudent);
        game.addStudentToIsland(selectedStudent, selectedIslandNumber);
        students.add(game.getStudent());
    }

    @Override
    public void setSelectedStudent(Color selectedStudent){
        this.selectedStudent=selectedStudent;
    }

    @Override
    public void setSelectedIslandNumber(int selectedIslandNumber){
        this.selectedIslandNumber = selectedIslandNumber;
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
        cost = 2;
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
        ClientCharacter1 character = new ClientCharacter1();

        character.setId(id);
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