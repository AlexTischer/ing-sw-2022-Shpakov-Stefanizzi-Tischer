package model;
import exceptions.NoEnoughCoinsException;
import exceptions.NoEntryException;

public class Character4 extends Character{
    /*students of selectedStudent color don’t count in influence*/

    private Color selectedStudent;
    private int cost = 3;



    public void setSelectedStudent(Color selectedStudent){
        this.selectedStudent = selectedStudent;
    }

    @Override
    public int calculateInfluence(Island island, int islandNumber) throws NoEntryException {
        if (!island.getNoEntry()){
            int score = 0;
            for (Color color: game.getCurrentPlayer().getProfessorsColor()){
                if ( !color.equals(selectedStudent) )
                    score += island.getNumOfStudents(color);
            }
            if (game.getCurrentPlayer().getTowerColor().equals(island.getTowersColor())){
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


}
