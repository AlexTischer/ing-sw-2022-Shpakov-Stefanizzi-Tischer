package model;

import java.util.*;

public class Bag {

    private LinkedList<Color> students;

    //NUMOFSTUDENT must be defined (current rules set it to 130)
    public Bag(int NUMOFSTUDENTS){

        LinkedList<Color> students = new LinkedList<Color>();

        for(Color c : Color.values()){
            for(int i=0; i<NUMOFSTUDENTS/Color.values().length; i++){
                students.add(c);
            }
        }
        Collections.shuffle(students);
    }

    public Color extractStudent(){
        return students.removeFirst();
    }
}