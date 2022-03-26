import java.util.*;

public class Island {
    private int numOfIslands;
    private Map <Color, Integer> students;
    private int numOfTowers;
    private TowerColor towersColor;

    public Island() {
        students = new HashMap<Color, Integer>();
        for(Color c : Color.values()){
            students.put(c, 0);
        }
        numOfIslands = 1;
        numOfTowers = 0;
    }

    public void mergeIsland(Island island){
        for(Color c : Color.values()){
            students.put(c, this.students.get(c)+island.students.get(c));
        }
        this.numOfIslands++;
        this.numOfTowers=this.numOfTowers+island.numOfTowers;
    }

    public void addStudent(Color color){
        students.put(color, students.get(color)+1);
    }

    public void setTowersColor(TowerColor towerColor) throws UnsupportedOperationException{
        if(numOfTowers>0){
            this.towersColor = towerColor;
        }
        else throw new UnsupportedOperationException("Error: No towers on island");
    }

    public int getNumOfStudents(Color studentColor){
        return students.get(studentColor);
    }

    public void addTower(TowerColor towerColor) throws UnsupportedOperationException{
        if(numOfTowers<numOfIslands){
            numOfTowers++;
            this.towersColor=towerColor;
        }
        else throw new UnsupportedOperationException();
    }

    public int getNumOfTowers() {
        return numOfTowers;
    }

    public TowerColor getTowersColor(){
        return towersColor;
    }


}