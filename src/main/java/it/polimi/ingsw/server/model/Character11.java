package it.polimi.ingsw.server.model;

import it.polimi.ingsw.client.model.ClientCharacter;
import it.polimi.ingsw.client.model.ClientCharacter11;
import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.exceptions.NoEnoughCoinsException;

public class Character11 extends Character {
    /*Removes 3 Students of studentColor from each player`s Dining*/

    private Color selectedStudent;
    private int cost = 3;
    protected int id = 11;

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
    }
    @Override
    public void buy() throws NoEnoughCoinsException {
        game.getGameBoard().removeCoins(game.getCurrentPlayer(), cost);
        cost = 4;
    }

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public ClientCharacter createClientCharacter(){
        ClientCharacter character = new ClientCharacter11();

        character.setCost(cost);
        character.setId(id);

        return character;
    }
}
