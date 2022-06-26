package server.model;

import it.polimi.ingsw.exceptions.NumOfStudentsExceeded;
import it.polimi.ingsw.exceptions.StudentNotFoundException;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.SchoolBoard;
import it.polimi.ingsw.server.model.TowerColor;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class SchoolBoardTest extends TestCase {

    @Test
    void removeStudentFromEntranceExceptionTest(){
        SchoolBoard testschoolboard = new SchoolBoard(TowerColor.BLACK, 8);
        for(int i=0; i<5; i++) {
            testschoolboard.addStudentToEntrance(Color.PINK);
        }
        for(int i=0; i<5; i++){
            testschoolboard.removeStudentFromEntrance(Color.PINK);
        }
        try{
            testschoolboard.removeStudentFromEntrance(Color.RED);
            assertTrue("false", false);
        }
        catch(StudentNotFoundException e){
        }
    }

    @Test
    void removeStudentFormDiningExceptionTest(){
        SchoolBoard testschoolboard = new SchoolBoard(TowerColor.BLACK, 8);
        try{
            testschoolboard.removeStudentFromDining(Color.getRandom());
            assertTrue("false", false);
        }
        catch(StudentNotFoundException e){
        }
    }

    @Test
    void moveStudentToDiningExceptionTest(){
        SchoolBoard testschoolboard = new SchoolBoard(TowerColor.BLACK, 8);
        for(int i=0;i<10;i++) {
            testschoolboard.addStudentToDining(Color.YELLOW);
        }

        testschoolboard.addStudentToEntrance(Color.YELLOW);

        try {
            testschoolboard.moveStudentToDining(Color.YELLOW);
            assertTrue("false", false);
        }
        catch(NumOfStudentsExceeded e) {
        }
    }

    @Test
    void addStudentToDiningExceptionTest(){
        SchoolBoard testschoolboard = new SchoolBoard(TowerColor.BLACK, 8);
        for(int i=0;i<10;i++){
            testschoolboard.addStudentToDining(Color.RED);
        }
        try{
            testschoolboard.addStudentToDining(Color.RED);
            assertTrue("false", false);
        }
        catch (NumOfStudentsExceeded e) {
        }

    }

    @Test
    void getNumOfStudentsInDiningTest(){
        SchoolBoard testschoolboard = new SchoolBoard(TowerColor.BLACK, 8);
           testschoolboard.addStudentToDining(Color.BLUE);
           testschoolboard.addStudentToDining(Color.BLUE);

           assertEquals(2, testschoolboard.getNumOfStudentsInDining(Color.BLUE));

    }

    @Test
    void getTowersColorTest(){
        SchoolBoard testschoolboard = new SchoolBoard(TowerColor.BLACK, 8);
        assertEquals(TowerColor.BLACK, testschoolboard.getTowerColor());
    }

    @Test
    void getProfessorColorTest(){
        SchoolBoard testschoolboard = new SchoolBoard(TowerColor.BLACK, 8);
        testschoolboard.addProfessor(Color.YELLOW);
        testschoolboard.addProfessor(Color.GREEN);

        ArrayList<Color> testprofessorlist = new ArrayList<>();
        testprofessorlist.add(Color.GREEN);
        testprofessorlist.add(Color.YELLOW);

        assertEquals(true , testprofessorlist.containsAll(testschoolboard.getProfessorsColor()));
    }

    @Test
    void getNumOfStudentsInEntranceTest(){
        SchoolBoard testschoolboard = new SchoolBoard(TowerColor.BLACK, 8);
        assertEquals(0, testschoolboard.getNumOfStudentsInEntrance());

        testschoolboard.addStudentToEntrance(Color.GREEN);
        assertEquals(1, testschoolboard.getNumOfStudentsInEntrance());
    }

    @Test
    void addTowerTest(){
        SchoolBoard testschoolboard = new SchoolBoard(TowerColor.BLACK, 5);
        testschoolboard.addTower();
        assertTrue(testschoolboard.getNumOfTowers()==6);
    }

}





