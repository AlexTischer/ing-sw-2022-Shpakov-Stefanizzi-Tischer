package controller;
import exceptions.NoEntryException;
import model.Color;
import model.Island;

public class Character4 extends Character{
    /*students of selectedStudent color don’t count in influence*/

    private Color selectedStudent;

    public void setSelectedStudent(Color selectedStudent){
        this.selectedStudent = selectedStudent;
    }

    @Override
    public int calculateInfluence(Island island, int islandNumber) throws NoEntryException {
        if (!island.getNoEntry()){
            int score = 0;
            for (Color color: game.currentPlayer.getProfessorsColor()){
                if ( !color.equals(selectedStudent) )
                    score += island.getNumOfStudents(color);
            }
            if (game.currentPlayer.getTowerColor().equals(island.getTowersColor())){
                score += island.getNumOfTowers();
            }

            return score;
        }
        else {
            game.setNoEntry(islandNumber,false);
            throw new NoEntryException();
        }
    }
}
