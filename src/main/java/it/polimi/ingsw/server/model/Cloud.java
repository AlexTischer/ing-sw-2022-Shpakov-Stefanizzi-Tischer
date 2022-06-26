package it.polimi.ingsw.server.model;

import it.polimi.ingsw.exceptions.NumOfStudentsExceeded;
import it.polimi.ingsw.exceptions.StudentNotFoundException;

import java.util.ArrayList;

/**This class represents a cloud
 * <ul>
 *     Cloud has
 *     <li>{@link #students}  contained on this cloud. Student is represented by {@link Color}</li>
 *     <li>{@link #maxNumOfStudents}  represents maximum number of students that can be placed on this cloud
 *     It varies depending on number of players ( 7 for 2-players version and 4-players version, 9 for 3-players version )
 *     </li>
 * </ul>*/
public class Cloud {
    private ArrayList<Color> students;
    private int maxNumOfStudents;

    public Cloud(int maxNumOfStudents){
        students = new ArrayList<Color>();
        this.maxNumOfStudents = maxNumOfStudents;
    }

    /**
     * @return  students situated on this cloud. The original list gets cloned*/
    public ArrayList<Color> getStudentsColors(){
        return (ArrayList<Color>) students.clone();
    }

    public int getMaxNumOfStudents(){
        return maxNumOfStudents;
    }

    /**
     * @throws NumOfStudentsExceeded  if this cloud is already full of students*/
    public void addStudent(Color studentColor) throws NumOfStudentsExceeded{
        if(students.size()<maxNumOfStudents){
            students.add(studentColor);
        }
        else throw new NumOfStudentsExceeded();
    }

    /**
     * Removes all students from the cloud*/
    public void removeStudents(){
        students.clear();
    }

    /**
     * @param studentColor  color of the student to remove from cloud
     * @throws StudentNotFoundException  if there is no student of such color on this cloud*/
    public void removeStudent(Color studentColor){
        if (!getStudentsColors().contains(studentColor))
            throw new StudentNotFoundException("There is no such student on this cloud");

        students.remove(studentColor);
    }
}
