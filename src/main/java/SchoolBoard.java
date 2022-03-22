import java.util.*;

public class SchoolBoard {
    private ArrayList<Student> entrance = new ArrayList<Student>(0);
    /*private Student[][] diningRoom;*/
    private Map<StudentColor, List <Student> > diningRoom;
    private List<Professor> professorTable;
    private List<Tower> towerTable;
    private TowerColor towersColor;

    public void addStudent(Student student){
    }

    public void moveStudentToDining(Color studentColor){
    }

    /*moves first student with given color to a given island */
    public void moveStudentToIsland(Color studentColor, Island island){
    }

    public int getNumOfStudentsInDining(StudentColor studentColor){
        return 0;
    }

    public TowerColor getTowersColor(){
        return towersColor;
    }

    public Set<StudentColor> getProfessorsColor(){
        return null;
    }

    public Island popTower(){
        return null;
    }

    public void pushProfessor(Professor professor){
    }

    public Professor popProfessor(StudentColor studentColor){
        return null;
    }

}

