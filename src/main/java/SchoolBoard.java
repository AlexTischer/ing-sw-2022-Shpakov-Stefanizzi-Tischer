import java.util.ArrayList;

public class SchoolBoard {
    private ArrayList<Student> entrance = new ArrayList<Student>(0);
    private Student[][] diningRoom;
    private Professor[] professorTable;
    private Tower[] towerTable;
    private TowerColor towersColor;

    public void addStudent(Student student){
    }

    public void moveStudentToDining(Color color){
    }

    public void moveStudentToIsland(Color color, Island island){
    }

    public int getNumOfStudentsInDining(Color color){
    }

    public TowerColor getTowersColor(){
        return towersColor;
    }

    public Color getProfessorsColor(){
    }

    public void moveTowerToIsland(Island island){
    }

    public void pushProfessor(Professor professor){
    }

    public Professor popProfessor(Color color){
    }

}

