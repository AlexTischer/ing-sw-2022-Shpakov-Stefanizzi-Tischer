package server.model;

import java.util.*;
import exceptions.NoEnoughStudentsException;
import exceptions.NoEnoughTowersException;
import exceptions.NumOfStudentsExceeded;
import exceptions.StudentNotFoundException;

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

    public void addStudentToEntrance(Color color) throws NumOfStudentsExceeded {
        entrance.add(color);
    }

    public void removeStudentFromEntrance(Color studentColor) throws NoEnoughStudentsException {
        if(entrance.contains(studentColor)) {
            entrance.remove(studentColor);
        }
        else throw new StudentNotFoundException();
    }

    public void moveStudentToDining(Color color) throws NumOfStudentsExceeded{
        if (diningRoom.get(color) < 10) {
            removeStudentFromEntrance(color);
            diningRoom.put(color, diningRoom.get(color) + 1);
        } else throw new NumOfStudentsExceeded();
        /*check professor*/
    }

    public void removeStudentFromDining(Color studentColor) throws NoEnoughStudentsException{
        if(diningRoom.get(studentColor)>0){
            diningRoom.put(studentColor, diningRoom.get(studentColor) - 1);
        }
        else throw new StudentNotFoundException();
    }

    public void addStudentToDining(Color studentColor) throws NumOfStudentsExceeded {
        if (diningRoom.get(studentColor) < 10) {
            diningRoom.put(studentColor, diningRoom.get(studentColor) + 1);
        }
        else throw new NumOfStudentsExceeded();
    }


    //TODO remove this method: use removeStudentFrom
    public void moveStudentToIsland(Color studentColor, Island island) {
        removeStudentFromEntrance(studentColor);
        island.addStudent(studentColor);
    }

    public int getNumOfStudentsInDining(Color studentColor){
        return diningRoom.get(studentColor);
    }

    public TowerColor getTowersColor(){
        return towersColor;
    }

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

    //TODO remove this method: use removeTower
    public void moveTowerToIsland(Island island){
        if(numOfTowers>0) {
            numOfTowers--;
            island.addTower(towersColor);
        } else throw new NoEnoughTowersException();
    }

    public boolean checkEmptyTowers(){
        return numOfTowers==0;
    }

    public void addProfessor(Color color){
        professors.put(color, 1);
    }

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

    public void addTower(){
        numOfTowers++;
    }

    public void removeTower() {
        if (numOfTowers < 1)
            throw new NoEnoughTowersException();

        numOfTowers--;
    }

    /*TEST METHODS*/

    public ArrayList<Color> getStudentsInEntrance(){
        return (ArrayList<Color>) entrance.clone();
    }

    public int getNumOfTowers(){
        return numOfTowers;
    }


}

