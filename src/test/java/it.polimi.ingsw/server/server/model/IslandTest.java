package it.polimi.ingsw.server.server.model;

import it.polimi.ingsw.exceptions.NoEntryException;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Island;
import it.polimi.ingsw.server.model.TowerColor;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

public class IslandTest extends TestCase {

    @Test
    void setNoEntryExceptionTest(){
        Island testisland = new Island();
        testisland.setNoEntry(true);

        try{
            testisland.setNoEntry(true);
            assertTrue("false", false);
        }
        catch (NoEntryException e){

        }
    }

    @Test
    void addStudentTest(){
        Island testisland = new Island();
        testisland.addStudent(Color.BLUE);

        assertEquals(1,testisland.getNumOfStudents(Color.BLUE));
    }

    @Test
    void addTowerExceptionTest(){
        Island testisland = new Island();
        testisland.addTower(TowerColor.BLACK);

        try{
            testisland.addTower(TowerColor.BLACK);
            assertTrue("false", false);
        }
        catch (UnsupportedOperationException e){
        }
    }

    @Test
    void getTowersColorTest(){
        Island testisland = new Island();
        testisland.addTower(TowerColor.WHITE);

        assertEquals(TowerColor.WHITE, testisland.getTowersColor());
    }

    @Test
    void mergeIslandTest(){
        Island testisland1 = new Island();
        Island testisland2 = new Island();

        testisland1.addTower(TowerColor.BLACK);
        testisland2.addTower(TowerColor.BLACK);

        /*adding some students into testisland1*/
        for(int i=0;i<5;i++) {
            testisland1.addStudent(Color.GREEN);
        }
        for(int i=0;i<3;i++) {
            testisland1.addStudent(Color.BLUE);
        }
        for(int i=0;i<1;i++) {
            testisland1.addStudent(Color.RED);
        }

        /*adding some students into testisland2*/
        for(int i=0;i<5;i++) {
            testisland1.addStudent(Color.GREEN);
        }
        for(int i=0;i<3;i++) {
            testisland1.addStudent(Color.BLUE);
        }
        for(int i=0;i<4;i++) {
            testisland1.addStudent(Color.PINK);
        }

        testisland1.mergeIsland(testisland2);

        /*checking students merge*/
        assertEquals(10, testisland1.getNumOfStudents(Color.GREEN));
        assertEquals(6, testisland1.getNumOfStudents(Color.BLUE));
        assertEquals(1, testisland1.getNumOfStudents(Color.RED));
        assertEquals(4, testisland1.getNumOfStudents(Color.PINK));
        assertEquals(0, testisland1.getNumOfStudents(Color.YELLOW));

        /*checking towers merge*/
        assertEquals(2, testisland1.getNumOfTowers());
    }
}
