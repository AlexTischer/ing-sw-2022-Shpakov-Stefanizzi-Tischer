package it.polimi.ingsw.server.model;

import it.polimi.ingsw.exceptions.NoEnoughCoinsException;

import java.util.*;

/**Swaps selectedStudents from Dining with toBeSwappedStudents from Entrance in currentPlayer`s schoolBoard **/
public class Character7 extends Character {

    private List<Color> selectedStudents;
    private List<Color> toBeSwappedStudents;
    private int cost = 1;
    protected int id = 7;

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
                throw new IllegalArgumentException("No such students on the card");
        }

        this.selectedStudents = selectedStudents;
    }

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

    }
    @Override
    public void buy() throws NoEnoughCoinsException {
        game.getGameBoard().removeCoins(game.getCurrentPlayer(), cost);
        cost = 2;
    }

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public int getId() {
        return id;
    }

}
