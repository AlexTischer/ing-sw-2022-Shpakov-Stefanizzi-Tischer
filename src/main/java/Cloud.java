import java.util.ArrayList;
import java.util.EmptyStackException;

public class Cloud {
    private ArrayList<Color> students;

    public Cloud(){
        students = new ArrayList<Color>();
    }

    public ArrayList<Color> getStudentsColors(){
        return (ArrayList<Color>) students.clone();
    }


    public void addStudents(Color color) throws EmptyStackException {
        if (students.size()==0) {
            students.add(color);
        }
        else throw new EmptyStackException();
    }

    public void removeStudents(){
        students.clear();
    }
}
