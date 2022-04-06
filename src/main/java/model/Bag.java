package model;

import exceptions.NoEnoughStudentsException;

import java.util.*;

public class Bag {

    private LinkedList<Color> students;

    public Bag(){

        students = new LinkedList<Color>();

        for(Color c : Color.values()){
            for(int i=0; i<130/Color.values().length; i++){
                students.add(c);
            }
        }
        Collections.shuffle(students);
    }

    public Color extractStudent() throws NoEnoughStudentsException {

        if(!(students.isEmpty())){
            return students.removeFirst();
        }
        else{
            throw new NoEnoughStudentsException();
        }
    }
}
