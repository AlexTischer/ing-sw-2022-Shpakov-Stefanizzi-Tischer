package it.polimi.ingsw.server.model;

import it.polimi.ingsw.exceptions.NoEnoughTowersException;
import it.polimi.ingsw.exceptions.NumOfStudentsExceeded;
import it.polimi.ingsw.exceptions.StudentNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**This class represents a school board of the player
 * <ul>The school board has
 *      <li>{@link #entrance} represents an entrance where students are located</li>
 *      <li>{@link #diningRoom} represents a dining room where students are located</li>
 *      <li>{@link #professors} represents the professors that player owns</li>
 *      <li>{@link #numOfTowers} represents how many towers a player has on it's school board</li>
 *      <li>{@link #towersColor} represents the color of tower that owner of this school board plays with</li>
 *      Students and professor are represented by {@link Color}
 * </ul>
 * <p>Each school board belongs to exactly one {@link Player}</p>
 * */
public class SchoolBoard {
    private ArrayList<Color> entrance;
    private Map<Color,Integer> diningRoom;
    private Map<Color,Integer> professors;
    private int numOfTowers;
    private TowerColor towersColor;

    public SchoolBoard(TowerColor towerColor, int numOfTowers){

        entrance = new ArrayList<Color>();

        diningRoom = new HashMap<Color, Integer>();
        /*diningRoom init*/
        for(Color c : Color.values()){
            diningRoom.put(c,0);
        }

        professors = new HashMap<Color, Integer>();
        /*professors init*/
        for(Color c : Color.values()){
            professors.put(c,0);
        }

        this.towersColor = towerColor;
        this.numOfTowers = numOfTowers;
    }

    public void addStudentToEntrance(Color color) {
        entrance.add(color);
    }

    /**
     * @param studentColor the color of the student to remove from entrance
     * @throws StudentNotFoundException  if there is no student of such color in entrance*/
    public void removeStudentFromEntrance(Color studentColor) {
        if(entrance.contains(studentColor)) {
            entrance.remove(studentColor);
        }
        else throw new StudentNotFoundException("You have no such student in entrance!");
    }

    /**
     * Moves the student from entrance to the dining room
     * <p>Calls {@link #removeStudentFromEntrance} in it's body</p>
     * @param color  color of the student to remove from entrance and add to dining room
     * @throws StudentNotFoundException  if there is no student of such color in entrance
     * @throws NumOfStudentsExceeded  if there is no free space in dining room for that color*/

    public void moveStudentToDining(Color color){
        if (diningRoom.get(color) < 10) {
            removeStudentFromEntrance(color);
            diningRoom.put(color, diningRoom.get(color) + 1);
        } else throw new NumOfStudentsExceeded();
        /*check professor*/
    }

    /**
     * @param studentColor  color of the student to remove from dining room
     * @throws StudentNotFoundException  if there is no student of such color in the dining room*/
    public void removeStudentFromDining(Color studentColor) {
        if(diningRoom.get(studentColor)>0){
            diningRoom.put(studentColor, diningRoom.get(studentColor) - 1);
        }
        else throw new StudentNotFoundException();
    }


    /**
     * @param studentColor  color of the student to add to dining room
     * @throws NumOfStudentsExceeded  if there is no free space in dining room for that color*/
    public void addStudentToDining(Color studentColor) {
        if (diningRoom.get(studentColor) < 10) {
            diningRoom.put(studentColor, diningRoom.get(studentColor) + 1);
        }
        else throw new NumOfStudentsExceeded("There is no space in dining room!");
    }

    /**
     * @return number of students of <code>studentColor</code>*/
    public int getNumOfStudentsInDining(Color studentColor){
        return diningRoom.get(studentColor);
    }

    public TowerColor getTowerColor(){
        return towersColor;
    }

    /**
     * @return  the list of professors that player owns. Each professor is represented by a {@link Color}*/
    public ArrayList<Color> getProfessorsColor(){
        //creating professors list to return
        ArrayList<Color> professorsList = new ArrayList<Color>();

        for(Color c : Color.values()){
            if(professors.get(c)==1){
                professorsList.add(c);
            }
        }
        return professorsList;
    }

    /**
     * @return true if {@link #numOfTowers} is 0, false otherwise*/
    public boolean checkEmptyTowers(){
        return numOfTowers==0;
    }

    /**
     * @param color  color of the professor to add to this school board*/
    public void addProfessor(Color color){
        professors.put(color, 1);
    }

    /**
     * @param color  color of the professor to remove from this school board*/
    public void removeProfessor(Color color){
        professors.put(color, 0);
    }

    public int getNumOfStudentsInEntrance(){
        if(entrance.isEmpty()){
            return 0;
        }
        else {
            return entrance.size();
        }
    }

    /**
     * Increments {@link #numOfTowers} on the school board*/
    public void addTower(){
        numOfTowers++;
    }

    /**
     * removes one tower from the school board
     * @throws NoEnoughTowersException  if there are no towers on the school board*/
    public void removeTower() {
        if (numOfTowers < 1)
            throw new NoEnoughTowersException();

        numOfTowers--;
    }
    public int getNumOfTowers(){
        return numOfTowers;
    }

    /*TEST METHODS*/

    /**
     * @return  the list of players contained in entrance*/
    public ArrayList<Color> getStudentsInEntrance(){
        return (ArrayList<Color>) entrance.clone();
    }


}

