import java.util.*;

public class SchoolBoard {
    private ArrayList<Color> entrance;
    private Map<Color,Integer> diningRoom;
    private Map<Color,Integer> professors;
    private int numOfTowers;
    private TowerColor towersColor;

    public SchoolBoard(TowerColor towerColor, int numOfTowers){
        entrance = new ArrayList<Color>();

        diningRoom = new HashMap<Color, Integer>();
        //diningRoom init
        for(Color c : Color.values()){
            diningRoom.put(c,0);
        }

        professors = new HashMap<Color, Integer>();
        //professors init
        for(Color c : Color.values()){
            professors.put(c,0);
        }

        this.towersColor = towerColor;
        this.numOfTowers = numOfTowers;
    }

    public void addStudentToEntrance(Color color){
        entrance.add(color);
    }

    /*1 Mike: what if entrance is empty ? How about throwing exception ?*/
    /*2 Mike: Shouldn`t I check professors each time I move student to the dining room ?*/
    public void moveStudentToDining(Color color){
        diningRoom.put(color, diningRoom.get(color)+1);
        entrance.remove(color);
    }

    /*Mike: what if entrance is empty ? How about throwing exception ?*/
    public void moveStudentToIsland(Color color, Island island){
        island.addStudent(color);
        entrance.remove(color);
    }

    /*Mike: can I move student from dining room into an island ?
    * void moveStudentFromDiningToIsland() ?*/

    public int getNumOfStudentsInDining(Color color){
        return diningRoom.get(color);
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

