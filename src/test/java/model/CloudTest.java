package model;

import exceptions.NumOfStudentsExceeded;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import model.Cloud;
import model.Color;
import java.util.ArrayList;



public class CloudTest extends TestCase {

    @Test
    void addStudentExceptionTest() {
        Cloud testcloud = new Cloud(4);
        for (int i = 0; i < testcloud.getMaxNumOfStudents(); i++) {
            testcloud.addStudent(Color.getRandom());
        }

        assertThrows(NumOfStudentsExceeded.class, ()->{ testcloud.addStudent(Color.getRandom()); });
    }

    @Test
    void getStudentColorTest(){
        Cloud testcloud = new Cloud(4);
        ArrayList<Color> testarray = new ArrayList<>();

        testcloud.addStudent(Color.BLUE);
        testarray.add(Color.BLUE);
        testcloud.addStudent(Color.GREEN);
        testarray.add(Color.GREEN);
        testcloud.addStudent(Color.PINK);
        testarray.add(Color.PINK);
        testcloud.addStudent(Color.BLUE);
        testarray.add(Color.BLUE);

        assertEquals(testcloud.getStudentsColors(), testarray);

    }

    @Test
    void removeStudentsTest() {
        Cloud testcloud = new Cloud(4);
        for (int i = 0; i < testcloud.getMaxNumOfStudents(); i++) {
            testcloud.addStudent(Color.getRandom());
        }

        testcloud.removeStudents();

        assertEquals(0, testcloud.getStudentsColors().size());
    }

    @Test
    void getNumOfStudentsTest(){
        Cloud testcloud = new Cloud(3);
        Cloud testcloudbis = new Cloud(4);

        assertEquals(3, testcloud.getMaxNumOfStudents());
        assertEquals(4, testcloudbis.getMaxNumOfStudents());
    }

}
