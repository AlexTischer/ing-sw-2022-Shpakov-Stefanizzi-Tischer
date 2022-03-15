import java.util.*;

public class Character {
    private int cost;
    private Effect effect;
    private List<Student> students;

    public Character(int cost, Effect effect, Student[] students) {
        this.cost = cost;
        this.effect = effect;

        for(Student s: students){
            this.students.add(s);
        }
    }

    public Set<Color> getStudentsColors(){
        return null;
    }

    public void pushStudent(Student student){

    }

    public Student popStudent(){
        return null;
    }

    public Effect getEffect(){
        
        /*how to prevent effect from being modified from the outside ?*/
        return this.effect;
    }

    public int getCost(){
        return this.cost;
    }

    public void activate(){

    }


}
