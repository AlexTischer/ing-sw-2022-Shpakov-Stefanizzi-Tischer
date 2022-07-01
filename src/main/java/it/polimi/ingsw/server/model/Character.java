package it.polimi.ingsw.server.model;

import it.polimi.ingsw.client.model.ClientCharacter;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.modelChange.ExceptionChange;
import it.polimi.ingsw.server.controller.Game;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class Character {
    protected Game game;
    protected int cost;
    protected String description = "ERROR";
    protected int id;
    protected boolean firstUse = true;

    /**
     * Calculates influence score of the player on particular island
     * <p>1 point gets added for each student who`s color is controlled by player,
     * which means that player owns a professor of that color</p>
     * <p>1 point gets added for each tower belonging to player</p>
     * @param island  island to calculate influence on
     * @param islandNumber  index of such island inside {@link GameBoard#islands}
     * @param player  player to calculate influence for
     * @throws NoEntryException  if there were noEntryTile on an island and removes it from that island
     * */
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

    /**
     * <ul>
     * <li>For each color, finds a player that has most students of this color in dining room.
     * <p>Such player becomes the owner of the professor corresponding to this color.</p>
     * </li>
     * <li>Professor can belong only to 1 player at a time</li>
     * <li>If there are 2 players that have the same number of students of the certain color
     * then professor doesn`t get reassigned for that color and the owner doesn`t change</li>
     *
     * */
    public void reassignProfessor() {

        for (Color color: Color.values()) {
            Player leader = game.getPlayers().get(0);
            boolean notRealLeader = false;
            //try to find a leader for each color
            for (Player player: game.getPlayers()) {
                if (player.getNumOfStudentsInDining(color) > leader.getNumOfStudentsInDining(color)) {
                    leader = player;
                }
                if(player.getNumOfStudentsInDining(color) == 0){
                    game.getGameBoard().removeProfessor(player, color);
                }
            }
            for(Player player : game.getPlayers()){
                if(player.getNumOfStudentsInDining(color)==leader.getNumOfStudentsInDining(color) && player.getNumOfStudentsInDining(color) > 0 && !player.equals(leader)){
                    notRealLeader = true;
                }
            }
            //add a professor only if player has at least one student of that color in dining room
            //and only if it is the current player
            if (game.getCurrentPlayer().equals(leader) && leader.getNumOfStudentsInDining(color) > 0 && !notRealLeader) {
                game.getGameBoard().addProfessor(leader, color);
                for (Player player: game.getPlayers()) {
                    if (!player.equals(leader)) {
                        game.getGameBoard().removeProfessor(player, color);
                    }
                }
            }
        }
    }

    /**
     * @return  true if currentPlayer is allowed to move mother nature this number of steps, false otherwise
     * */
    public boolean moveMotherNature(int steps){
        return (game.getCurrentPlayer().getPlayedAssistant().getMovements()>=steps && steps>0);
    }

    public void initialFill(Game game){
        this.game=game;
    }

    /**
     * Executes a sequence of actions related to the properties of this character
     * <p>Note: can be also void if character does no action and only causes an effect</p>
     * */
    public void execute() {}

    public void buy() throws NoEnoughCoinsException {
    }

    public int getCost(){
        return cost;
    }

    /**
     * <p>This setting is used by {@link Character1}, {@link Character4}, {@link Character10} and {@link Character11}</p>
     * @param selectedStudent  student to be set as an attribute in this character
     * @throws UnsupportedOperationException  if this setting is not supported in this character
     *
     * */
    public void setSelectedStudent(Color selectedStudent){
        throw new UnsupportedOperationException("Students cannot be set on default character");
    }

    /**
     * Sets index of selected island as a Character`s attribute
     * <p>This setting is used by {@link Character1}, {@link Character5} and {@link Character12}</p>
     * @param selectedIsland  index of the island to be set as an attribute in this character
     * @throws UnsupportedOperationException  if this setting is not supported in this character
     * */
    public void setSelectedIslandNumber(int selectedIsland){
        throw new UnsupportedOperationException("Island cannot be set on this character");
    }

    /**
     * Sets selectedStudents as a Character`s attribute
     * <p>This setting is used by {@link Character7} and {@link Character9}</p>
     * @param selectedStudents  students needed for character execution in {@link #execute()}
     * @throws UnsupportedOperationException  if this setting is not supported in this character
     * <p>Note: for other exceptions thrown look {@link Character7#setSelectedStudents(ArrayList)}
     * and {@link Character9#setSelectedStudents(ArrayList)}</p>
     * */
    public void setSelectedStudents(ArrayList<Color> selectedStudents){
        throw new UnsupportedOperationException("Students cannot be set on this character");
    }

    /**
     * Sets index of selected island as a Character`s attribute
     * <p>This setting is used by {@link Character7} and {@link Character9}</p>
     * @param toBeSwappedStudents  students needed for character execution in {@link #execute()}
     * @throws UnsupportedOperationException  if this setting is not supported in this character
     * <p>Note: for other exceptions thrown look {@link Character7#setToBeSwappedStudents(ArrayList)}
     * and {@link Character9#setToBeSwappedStudents(ArrayList)}</p>
     * */
    public void setToBeSwappedStudents(ArrayList<Color> toBeSwappedStudents){
        throw new UnsupportedOperationException("Students cannot be set on this character");
    }

    public void addNoEntryTile(){}

    public int getNoEntryTiles(){
        return -1;
    }

    public Color[] getStudentsSlot(){
        return null;
    }

    public ClientCharacter createClientCharacter() {
        throw new UnsupportedOperationException("Client Character cannot be created on default character");
    }

    public int getId() {
        return id;
    }
}