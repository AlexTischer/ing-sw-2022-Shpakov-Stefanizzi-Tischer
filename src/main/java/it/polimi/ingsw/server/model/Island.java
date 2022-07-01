package it.polimi.ingsw.server.model;

import it.polimi.ingsw.exceptions.NoEntryException;

import java.util.HashMap;
import java.util.Map;

/**This class represents an island
 * <ul>
 *     Island has
 *     <li>{@link #students}  placed on island. Student is represented by {@link Color}</li>
 *     <li>{@link #numOfTowers}  number of towers placed on island. Can be greater then zero in case more islands were merged</li>
 *     <li>{@link #towersColor}  color of towers placed on island. The towers have the same color</li>
 *     <li>{@link #noEntry}  true if <code>NoEntryTile</code> is situated on this island, false otherwise</li>
 *     <li>{@link #numOfIslands}  number of islands that were merged with this one. Equals to 1 if no merge was done yet</li>
 * </ul>*/
public class Island {
    private Map <Color, Integer> students;
    private int numOfTowers;
    private TowerColor towersColor;
    private boolean noEntry;
    private int numOfIslands;

    public Island() {
        students = new HashMap<Color, Integer>();
        for(Color c : Color.values()){
            students.put(c, 0);
        }
        numOfIslands = 1;
        numOfTowers = 0;
        noEntry = false;
    }

    /**Sets <code>noEntryTile</code> on this island
     *
     * @param noEntry  true if <code>noEntryTile</code> is being placed, false if <code>noEntryTile</code> is being taken away
     * @throws NoEntryException  if more than one <code>noEntryTile</code> is being placed on this island*/
    public void setNoEntry(boolean noEntry){
        if(noEntry && this.noEntry){
            throw new NoEntryException();
        }
        else {
            this.noEntry = noEntry;
        }
    }

    public boolean getNoEntry(){
        return this.noEntry;
    }

    /**Unifies two islands by summing up their
     * <ul>
     *     <li>{@link #students}</li>
     *     <li>{@link #numOfIslands}</li>
     *     <li>{@link #numOfTowers}</li>
     * </ul>
     * @param island  island to be merged with this island
     * */
    public void mergeIsland(Island island){
        for(Color c : Color.values()){
            students.put(c, this.students.get(c)+island.students.get(c));
        }
        this.numOfIslands += island.numOfIslands;
        this.numOfTowers += island.numOfTowers;
    }

    /**
     * @param color  color of the student to place on this island*/
    public void addStudent(Color color){
        students.put(color, students.get(color)+1);
    }

    /**Puts a tower on this island only if it wasn't done before.
     * {@link #numOfTowers} can be greater than 1 only inside {@link #mergeIsland(Island)} method
     * @param towerColor  the color of the tower to put on this island
     * @throws UnsupportedOperationException  if more than one tower is being placed on a single island*/
    public void addTower(TowerColor towerColor) throws UnsupportedOperationException{
        if(numOfTowers==0){
            numOfTowers=1;
            this.towersColor=towerColor;
        }
        else throw new UnsupportedOperationException();
    }

    /**Changes the color of all towers on this island.
     * This method gets used when an island gets conquered by another player.
     * @param towerColor  the new color of all towers situated on this island
     * @throws UnsupportedOperationException  if there are no towers on this island*/
    public void setTowersColor(TowerColor towerColor) throws UnsupportedOperationException{
        if(numOfTowers>0){
            this.towersColor = towerColor;
        }
        else throw new UnsupportedOperationException("Error: No towers on island");
    }
    public int getNumOfIslands() {
        return numOfIslands;
    }

    public int getNumOfStudents(Color studentColor){
        return students.get(studentColor);
    }

    public int getNumOfTowers() {
        return numOfTowers;
    }

    public TowerColor getTowersColor(){
        return towersColor;
    }

}