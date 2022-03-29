package model;

import java.util.ArrayList;
import java.util.EmptyStackException;

public class Cloud {
    private ArrayList<Color> students;
    private int maxNumOfStudents;

    public Cloud(int maxNumOfStudents){
        students = new ArrayList<Color>();
        this.maxNumOfStudents = maxNumOfStudents;
    }

    public ArrayList<Color> getStudentsColors(){
        return (ArrayList<Color>) students.clone();
    }

    public int getMaxNumOfStudents(){
        return maxNumOfStudents;
    }

    /*implementation must be changed*/
    public void addStudent(Color color) throws EmptyStackException {
        if (students.size()==0) {
            students.add(color);
        }
        else throw new EmptyStackException();
    }

    public void removeStudents(){
        students.clear();
    }
}
