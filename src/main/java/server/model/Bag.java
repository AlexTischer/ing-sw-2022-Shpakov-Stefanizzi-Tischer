package server.model;

import exceptions.NoEnoughStudentsException;

import java.util.*;

public class Bag {

    private LinkedList<Color> students;

    //use this list to simulate small bag of 10 students used to refill islands
    private List<Color> studentsColorPool = new ArrayList<Color>();

    public Bag(){

        students = new LinkedList<Color>();

        for(Color c : Color.values()){
            for(int i=0; i<130/Color.values().length; i++){
                students.add(c);
            }
        }
        Collections.shuffle(students);

        //add 2 students of each color, this will be used to refill islands
        for (Color color: Color.values()){
            studentsColorPool.add(color);
            studentsColorPool.add(color);
        }
    }

    public Color extractStudent() throws NoEnoughStudentsException {

        if(!(students.isEmpty())){
            return students.removeFirst();
        }
        else{
            throw new NoEnoughStudentsException();
        }
    }

    //method that simulates small bag of 10 students used at start to refill islands
    public Color extractStudentForIslands() {
        int poolIndex = new Random().nextInt(0, studentsColorPool.size());
        Color studentToReturn = null;

        boolean studentPooled = false;

        //pool the student that I need
        while (!studentPooled){
            studentToReturn = extractStudent();
            if (studentToReturn.equals(studentsColorPool.get(poolIndex))){
                //pooled the student that needed
                studentsColorPool.remove(poolIndex);
                studentPooled = true;
            }
            else{
                //return that student back to bag and shuffle it
                addStudent(studentToReturn);
            }
        }

        return studentToReturn;
    }

    public void addStudent(Color studentColor){
        students.add(studentColor);
        Collections.shuffle(students);
    }

    public boolean checkEmpty(){
        return students.isEmpty();
    }
}
