package model;

import controller.Game;
import model.Player;
import exceptions.NoEnoughCoinsException;
import exceptions.NoEnoughEntryTilesException;
import exceptions.NoEnoughStudentsException;
import exceptions.NoEntryException;
import model.*;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class Character {
    protected Game game;
    private int cost;

    public int calculateInfluence(Island island, int islandNumber) throws NoEntryException{
        if (!island.getNoEntry()){
            int score = 0;
            for (Color color: game.getCurrentPlayer().getProfessorsColor()){
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

    public void reassignProfessor() {
        Player leader = game.getPlayers().get(0);

        for (Color color: Color.values()){
            for (Player player: game.getPlayers()){
                if (player.getNumOfStudentsInDining(color) > leader.getNumOfStudentsInDining(color))
                    leader = player;
            }
            if (game.getCurrentPlayer().equals(leader)){
                game.getGameBoard().addProfessor(leader, color);
                for (Player player: game.getPlayers())
                    if (!player.equals(leader))
                        game.getGameBoard().removeProfessor(player, color);
            }
        }
    }

    public boolean moveMotherNature(int steps){
        return (game.getCurrentPlayer().getPlayedAssistant().getMovements()>=abs(steps));
    }

    public void initialFill(Game game){
        this.game=game;
    }

    public void execute() {}

    public void buy() throws NoEnoughCoinsException {
    }

    public int getCost(){
        return cost;
    }

    public void setSelectedStudent(Color selectedStudent){
        throw new UnsupportedOperationException();
    }

    public void setSelectedIslandNumber(int selectedIsland) throws NoEnoughStudentsException{
    }

    public void setSelectedStudents(ArrayList<Color> selectedStudents){
        throw new UnsupportedOperationException();
    }

    public void setToBeSwappedStudents(ArrayList<Color> toBeSwappedStudents){
        throw new UnsupportedOperationException();
    }

    public void addNoEntryTile(){}

}