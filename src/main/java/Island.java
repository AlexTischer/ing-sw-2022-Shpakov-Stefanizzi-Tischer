import java.util.*;

public class Island {
    private int numOfIslands;
    private Map <Color, Integer> students;
    private int numOfTowers;
    private TowerColor towersColor;

    public Island() {
        students = new HashMap<Color, Integer>();
        numOfIslands = 1;
        numOfTowers = 0;
    }

    public void addStudent(Color color){
        students.put(color, students.get(color)+1);
    }

    public void setTowersColor(TowerColor towerColor) throws UnsupportedOperationException{
        if(numOfTowers>0){
            this.towersColor = towerColor;
        }
        else throw new UnsupportedOperationException();
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