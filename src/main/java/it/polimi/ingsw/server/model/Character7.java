package it.polimi.ingsw.server.model;

import it.polimi.ingsw.client.model.ClientCharacter;
import it.polimi.ingsw.client.model.ClientCharacter7;
import it.polimi.ingsw.exceptions.NoEnoughCoinsException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**Swaps selectedStudents from Dining with toBeSwappedStudents from Entrance in currentPlayer`s schoolBoard **/
public class Character7 extends Character {

    private int id=7;
    private List<Color> selectedStudents;
    private List<Color> toBeSwappedStudents;
    private int cost = 1;
    private String description = "You can swap up to 2 students between your entrance and your dining room";

    @Override
    /**
     * Sets {@link #selectedStudents} attribute
     * @throws IllegalArgumentException  if selectedStudents size is greater than 2,
     * or if there is a student in selectedStudents that is not present in the
     * currentPlayer`s dining room
     * */
    public void setSelectedStudents(ArrayList<Color> selectedStudents){
        if(selectedStudents.size() > 2)
            throw new IllegalArgumentException("You can select at most 2 students");

        Map<Color, Integer > testStudents = new HashMap<Color, Integer>();
        for (Color c: Color.values())
            testStudents.put(c, game.getCurrentPlayer().getNumOfStudentsInDining(c));

        for (Color studentColor: selectedStudents) {
            if (testStudents.get(studentColor) > 0)
                testStudents.put(studentColor, testStudents.get(studentColor)-1);
            else
                throw new IllegalArgumentException("No such students in your dining room");
        }

        this.selectedStudents = selectedStudents;
    }

    /**
     * Sets {@link #toBeSwappedStudents} attribute
     * @throws IllegalArgumentException  if selectedStudents size is greater than 2,
     * or if there is a student in selectedStudents that is not present in the
     * currentPlayer`s entrance
     * */
    public void setToBeSwappedStudents(ArrayList<Color> toBeSwappedStudents){
        if(toBeSwappedStudents.size() > 2)
            throw new IllegalArgumentException("You can select at most 2 students");

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
            throw new IllegalArgumentException("2 lists don`t have the same size");

        for(int i = 0; i < selectedStudents.size(); i++){
            Color studentToEntrance = selectedStudents.get(i);
            game.getGameBoard().removeStudentFromDining(game.getCurrentPlayer(), studentToEntrance);

            Color studentToDining = toBeSwappedStudents.get(i);
            game.getGameBoard().removeStudentFromEntrance(game.getCurrentPlayer(), studentToDining);

            game.getGameBoard().addStudentToEntrance(game.getCurrentPlayer(), studentToEntrance);
            game.getGameBoard().addStudentToDining(game.getCurrentPlayer(), studentToDining);
        }

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
        cost = 2;
    }

    @Override
    public ClientCharacter createClientCharacter(){

        ClientCharacter7 character = new ClientCharacter7();

        character.setId(id);
        character.setCost(cost);
        character.setDescription(description);
        character.setFirstUse(firstUse);

        return character;
    }

}
