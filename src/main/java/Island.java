import java.util.ArrayList;

public class Island {
    private int numOfIslands = 1;
    /*advice to set List<Student> as static type*/
    private ArrayList<Student> students = new ArrayList<Student>(0);
    private ArrayList<Tower> towers = new ArrayList<Tower>(0);

    public Island() {
    }

    public void addStudent(Student student){
    }

    public int getNumOfStudents(StudentColor studentColor){
        int n = 0;
        return n;
    }

    public void addTower(Tower tower){
    }

    public void setTowersColor(TowerColor towerColor){
    }

    public int getNumOfTowers() {
        int n = 0;
        return n;
    }

    public TowerColor getTowersColor(){
        TowerColor t = TowerColor.WHITE;
        return t;
    }


}