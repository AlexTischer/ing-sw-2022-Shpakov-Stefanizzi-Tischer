package it.polimi.ingsw.server.model;
import it.polimi.ingsw.client.model.ClientCharacter;
import it.polimi.ingsw.client.model.ClientCharacter4;
import it.polimi.ingsw.exceptions.NoEnoughCoinsException;
import it.polimi.ingsw.exceptions.NoEntryException;

public class Character4 extends Character {
    /*students of selectedStudent color don’t count in influence*/

    private Color selectedStudent;
    private int cost = 3;
    private String description = "C4";


    public void setSelectedStudent(Color selectedStudent){
        this.selectedStudent = selectedStudent;
    }


    @Override
    public int calculateInfluence(Island island, int islandNumber, Player player) throws NoEntryException {
        if (!island.getNoEntry()){
            int score = 0;
            for (Color color: player.getProfessorsColor()){
                if ( !color.equals(selectedStudent) )
                    score += island.getNumOfStudents(color);
            }
            if (player.getTowerColor().equals(island.getTowersColor())){
                score += island.getNumOfTowers();
            }

            return score;
        }
        else {
            game.setNoEntry(islandNumber,false);
            throw new NoEntryException();
        }
    }

    @Override
    public void buy() throws NoEnoughCoinsException {
        game.getGameBoard().removeCoins(game.getCurrentPlayer(), cost);
        cost = 4;
    }

    @Override
    public ClientCharacter createClientCharacter(){

        ClientCharacter character = new ClientCharacter4();

        character.setCost(cost);
        character.setDescription(description);


        return character;
    }

}
