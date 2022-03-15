import java.util.*;

public abstract class Character {
    private int cost;
    private List<Student> students;

    public Character(int cost, Student[] students) {
        this.cost = cost;

        for(Student s: students){
            this.students.add(s);
        }
    }

    public Set<StudentColor> getStudentsColors(){
        return null;
    }

    public void pushStudent(Student student){

    }

    public Student popStudent(){
        return null;
    }

    public int getCost(){
        return this.cost;
    }

    public void activate(){

    }


}
