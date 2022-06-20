package it.polimi.ingsw.server.model;

import it.polimi.ingsw.client.model.ClientCharacter;
import it.polimi.ingsw.client.model.ClientCharacter11;
import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.exceptions.NoEnoughCoinsException;

public class Character11 extends Character {
    /*Removes 3 Students of studentColor from each player`s Dining*/

    private Color selectedStudent;
    private int cost = 3;
    private String description = "Every player (including yourself) must return 3 students of a color of your choice from their dining room to the bag";

    @Override
    public void initialFill(Game game){
        super.initialFill(game);
    }

    public void setSelectedStudent(Color selectedStudent){
        this.selectedStudent = selectedStudent;
    }

    public void execute() {
        for (Player player: game.getPlayers()){
            int numOfStudentsInDining = player.getNumOfStudentsInDining(selectedStudent);

            for (int i = 0; i < (Math.min(3, numOfStudentsInDining)); i++)
                game.removeStudentFromDining(player, selectedStudent);
                game.getGameBoard().addStudentToBag(selectedStudent);
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
        cost = 4;
    }


    @Override
    public ClientCharacter createClientCharacter(){
        ClientCharacter11 character = new ClientCharacter11();

        character.setCost(cost);
        character.setDescription(description);

        return character;
    }
}
