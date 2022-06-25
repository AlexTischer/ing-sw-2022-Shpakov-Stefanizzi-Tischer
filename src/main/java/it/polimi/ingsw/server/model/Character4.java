package it.polimi.ingsw.server.model;
import it.polimi.ingsw.client.model.ClientCharacter;
import it.polimi.ingsw.client.model.ClientCharacter4;
import it.polimi.ingsw.exceptions.NoEnoughCoinsException;
import it.polimi.ingsw.exceptions.NoEntryException;

/*students of selectedStudent color don’t count in influence*/
public class Character4 extends Character {

    private int id = 4;
    private Color selectedStudent;
    private int cost = 3;
    private String description = "When calculating influence, student's color of your choice won't count";


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

        ClientCharacter4 character = new ClientCharacter4();

        character.setId(id);
        character.setCost(cost);
        character.setDescription(description);
        character.setFirstUse(firstUse);


        return character;
    }

}
