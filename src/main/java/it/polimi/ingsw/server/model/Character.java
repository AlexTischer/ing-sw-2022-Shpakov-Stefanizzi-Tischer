package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.exceptions.NoEnoughCoinsException;
import it.polimi.ingsw.exceptions.NoEnoughStudentsException;
import it.polimi.ingsw.exceptions.NoEntryException;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class Character {
    protected Game game;
    protected int cost;
    protected int id;


    public int calculateInfluence(Island island, int islandNumber, Player player) throws NoEntryException {
        if (!island.getNoEntry()){
            int score = 0;
            for (Color color: player.getProfessorsColor()){
                score += island.getNumOfStudents(color);
            }
            if (player.getTowerColor().equals(island.getTowersColor())){
                score += island.getNumOfTowers();
            }
            return score;
        }
        else {
            /*takes no entry tile away from the island*/
            game.setNoEntry(islandNumber,false);
            throw new NoEntryException();
        }
    }

    public void reassignProfessor() {
        Player leader = game.getPlayers().get(0);

        for (Color color: Color.values()) {
            //try to find a leader for each color
            for (Player player: game.getPlayers()) {
                if (player.getNumOfStudentsInDining(color) > leader.getNumOfStudentsInDining(color))
                    leader = player;
            }
            //add a professor only if player has at least one student of that color in dining room
            //and only if it is the current player
            if (game.getCurrentPlayer().equals(leader) && leader.getNumOfStudentsInDining(color) > 0) {
                game.getGameBoard().addProfessor(leader, color);
                for (Player player: game.getPlayers()) {
                    if (!player.equals(leader))
                        game.getGameBoard().removeProfessor(player, color);
                }
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

    public void setSelectedIslandNumber(int selectedIsland) throws NoEnoughStudentsException {
        throw new UnsupportedOperationException();
    }

    public void setSelectedStudents(ArrayList<Color> selectedStudents){
        throw new UnsupportedOperationException();
    }

    public void setToBeSwappedStudents(ArrayList<Color> toBeSwappedStudents){
        throw new UnsupportedOperationException();
    }

    public void addNoEntryTile(){}

    public int getNoEntryTiles(){
        return -1;
    }

    public Color[] getStudentsSlot(){
        return null;
    }

    public int getId() {
        return id;
    }
}