package it.polimi.ingsw.server.model;

import it.polimi.ingsw.client.model.ClientCharacter;
import it.polimi.ingsw.client.model.ClientCharacter9;
import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.exceptions.NoEnoughCoinsException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**Swaps selectedStudents in students on card with toBeSwappedStudents in currentPlayer Entrance **/
public class Character9 extends Character {

    private int id=9;
    private List<Color> students;
    private List<Color> selectedStudents;
    private List<Color> toBeSwappedStudents;
    private int cost = 1;

    private String description = "Take up to 3 students from this card and replace them with the same number of students from your entrance";

    public void initialFill(Game game){
        super.initialFill(game);
        students = new ArrayList<Color>();

        for (int i = 0; i < 6; i++)
            students.add(i, game.getStudent());

    }

    public void setSelectedStudents(ArrayList<Color> selectedStudents){
        if(selectedStudents.size() > 3)
            throw new IllegalArgumentException("You can select at most 3 students on card");

        List<Color> testStudents = new LinkedList<Color>(students);
        for (Color studentColor: selectedStudents) {
            if (testStudents.contains(studentColor))
                testStudents.remove(studentColor);
            else
                throw new IllegalArgumentException("No such students on the card");
        }

        this.selectedStudents = selectedStudents;
    }

    public void setToBeSwappedStudents(ArrayList<Color> toBeSwappedStudents){
        if (toBeSwappedStudents.size() > 3)
            throw new IllegalArgumentException("You can select at most 3 students");

        List<Color> testStudents = game.getCurrentPlayer().getStudentsInEntrance();
        for (Color studentColor: toBeSwappedStudents) {
            if (testStudents.contains(studentColor))
                testStudents.remove(studentColor);
            else
                throw new IllegalArgumentException("No such students in player entrance");
        }

        this.toBeSwappedStudents = toBeSwappedStudents;
    }

    public void execute(){
        if (selectedStudents.size() != toBeSwappedStudents.size())
            throw new IllegalArgumentException("The students selected on card and students selected in entrance don't have the same size");

        /*create temporary list to remove students easily*/
        List<Color> tempStudents = new ArrayList<Color>(students);

        for(int i = 0; i < selectedStudents.size(); i++){
            Color studentToEntrance = selectedStudents.get(i);
            tempStudents.remove(studentToEntrance);

            Color studentToCharacter = toBeSwappedStudents.get(i);
            game.getGameBoard().removeStudentFromEntrance(game.getCurrentPlayer(), studentToCharacter);

            game.getGameBoard().addStudentToEntrance(game.getCurrentPlayer(), studentToEntrance);
            tempStudents.add(studentToCharacter);
        }

        for(int i=0; i <tempStudents.size();i++){
            students.add(i, tempStudents.get(i));
        }

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
        ClientCharacter9 character = new ClientCharacter9();

        character.setId(id);
        character.setCost(cost);
        character.setDescription(description);
        character.setFirstUse(firstUse);
        character.setStudents(getStudentsSlot());

        return character;
    }
}
