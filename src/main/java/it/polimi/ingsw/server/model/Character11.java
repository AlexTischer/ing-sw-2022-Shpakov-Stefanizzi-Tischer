package it.polimi.ingsw.server.model;

import it.polimi.ingsw.client.model.ClientCharacter;
import it.polimi.ingsw.client.model.ClientCharacter11;
import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.exceptions.NoEnoughCoinsException;

public class Character11 extends Character {
    /*Removes 3 Students of studentColor from each player`s Dining*/

    private int id=11;
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
        game.getGameBoard().removeCoins(game.getCurrentPlayer(), cost);
        cost = 4;
    }


    @Override
    public ClientCharacter createClientCharacter(){
        ClientCharacter character = new ClientCharacter11();

        character.setId(id);
        character.setCost(cost);
        character.setDescription(description);

        return character;
    }
}
