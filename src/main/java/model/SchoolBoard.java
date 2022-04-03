package model;

import java.util.*;
import exceptions.*;

public class SchoolBoard {
    private ArrayList<Color> entrance;
    private Map<Color,Integer> diningRoom;
    private Map<Color,Integer> professors;
    private int numOfTowers;
    private TowerColor towersColor;
    private int maxStudentsInEntrance;

    public SchoolBoard(TowerColor towerColor, int numOfTowers, int maxStudentsInEntrance){
        this.maxStudentsInEntrance = maxStudentsInEntrance;

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

    public void addStudentToEntrance(Color color) throws NumOfStudentsExceeded{
        if(entrance.size()<maxStudentsInEntrance) {
            entrance.add(color);
        }
        else throw new NumOfStudentsExceeded();
    }

    public void removeStudentFromEntrance(Color studentColor) throws NoEnoughStudentsException {
        if(entrance.contains(studentColor)) {
            entrance.remove(studentColor);
        }
        else throw new NoEnoughStudentsException();
    }

    /*1 Mike: what if entrance is empty ? How about throwing exception ?*/
    /*2 Mike: Shouldn`t I check professors each time I move student to the dining room ?*/

    public void moveStudentToDining(Color color) throws NumOfStudentsExceeded {
        if (diningRoom.get(color) < 10) {
            diningRoom.put(color, diningRoom.get(color) + 1);
            entrance.remove(color);
        } else throw new NumOfStudentsExceeded();
        /*check professor*/
    }

    public void removeStudentFromDining(Color studentColor) throws NumOfStudentsExceeded{
        if(diningRoom.get(studentColor)>0){
            diningRoom.put(studentColor, diningRoom.get(studentColor) - 1);
        }
        else throw new NumOfStudentsExceeded();
    }

    public void addStudentToDining(Color studentColor) throws NumOfStudentsExceeded {
        if (diningRoom.get(studentColor) < 10) {
            diningRoom.put(studentColor, diningRoom.get(studentColor) + 1);
        }
        else throw new NumOfStudentsExceeded();
    }

    /*Mike: what if entrance is empty ? How about throwing exception ?*/
    public void moveStudentToIsland(Color studentColor, Island island) throws NoEnoughStudentsException {
        if(entrance.contains(studentColor)) {
            entrance.remove(studentColor);
        }
        else throw new NoEnoughStudentsException();

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

    public void moveTowerToIsland(Island island){
        numOfTowers--;
        island.addTower(towersColor);
    }

    public void addProfessor(Color color){
        professors.put(color, 1);
    }

    public void removeProfessor(Color color){
        professors.put(color, 0);
    }

    public int getNumOfStudentsInEntrance(){
        return entrance.size();
    }

    public void addTower(){
        numOfTowers++;
    }
}
