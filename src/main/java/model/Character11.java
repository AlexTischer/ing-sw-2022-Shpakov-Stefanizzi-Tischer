package model;

import controller.Player;
import exceptions.NoEnoughCoinsException;

public class Character11 extends Character{
    /*Removes 3 Students of studentColor from each player`s Dining*/

    private Color selectedStudent;
    private int cost = 3;

    public void setSelectedStudent(Color selectedStudent){
        this.selectedStudent = selectedStudent;
    }

    public void execute() {
        for (Player player: game.getPlayers()){
            int numOfStudentsInDining = player.getNumOfStudentsInDining(selectedStudent);

            for (int i = 0; i < (Math.min(3, numOfStudentsInDining)); i++)
                player.removeStudentFromDining(selectedStudent);
        }
    }
    @Override
    public void buy() throws NoEnoughCoinsException {
        game.getCurrentPlayer().removeCoins(cost);
        cost = 4;
    }


}
