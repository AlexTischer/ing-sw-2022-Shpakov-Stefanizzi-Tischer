package model;

import exceptions.NumOfStudentsExceeded;

import java.util.ArrayList;

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

    public void addStudent(Color studentColor) throws NumOfStudentsExceeded{
        if(students.size()<maxNumOfStudents){
            students.add(studentColor);
        }
        else throw new NumOfStudentsExceeded();
    }

    public void removeStudents(){
        students.clear();
    }
}
