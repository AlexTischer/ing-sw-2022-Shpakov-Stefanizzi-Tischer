package model;

import controller.*;
import exceptions.NoEnoughStudentsException;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import java.security.InvalidAlgorithmParameterException;

import static org.junit.jupiter.api.Assertions.*;

public class GameBoardTest extends TestCase {

    @Test
    void initTest(){
        GameBoard testGameBoard = GameBoard.getInstanceOfGameBoard();
        testGameBoard.init(2);

        assertEquals(12, testGameBoard.getNumOfIslands());
        assertEquals(2, testGameBoard.getNumOfClouds());

        testGameBoard.init(3);
        assertEquals(3, testGameBoard.getNumOfClouds());

        testGameBoard.init(4);
        assertEquals(4, testGameBoard.getNumOfClouds());
    }


    @Test
    void refillEntranceTest(){
        /*2 players game*/
        GameBoard testGameBoard = GameBoard.getInstanceOfGameBoard();
        testGameBoard.init(2);
        Player testPlayer = new Player("Test", TowerColor.BLACK, AssistantType.ONE, 8);
        testGameBoard.refillEntrance(testPlayer);
        assertEquals(7, testPlayer.getNumOfStudentsInEntrance());

        /*3 players game*/
        testGameBoard.init(3);
        testPlayer = new Player("Test", TowerColor.BLACK, AssistantType.ONE, 6);
        testGameBoard.refillEntrance(testPlayer);
        assertEquals(9, testPlayer.getNumOfStudentsInEntrance());

        /*4 players game*/
        testGameBoard.init(4);
        testPlayer = new Player("Test", TowerColor.BLACK, AssistantType.ONE, 8);
        testGameBoard.refillEntrance(testPlayer);
        assertEquals(7, testPlayer.getNumOfStudentsInEntrance());

    }

    @Test
    void getCurrentPlayerTest(){
        GameBoard testGameBoard = GameBoard.getInstanceOfGameBoard();
        Player testPlayer = new Player("Test", TowerColor.BLACK, AssistantType.ONE, 8);
        testGameBoard.init(2);
        testGameBoard.setCurrentPlayer(testPlayer);

        assertEquals(testPlayer, testGameBoard.getCurrentPlayer());
    }

    @Test
    void mergeIslandsTest(){
        GameBoard testGameBoard = GameBoard.getInstanceOfGameBoard();
        testGameBoard.init(2);

        /**MERGE 2 CONSECUTIVE ISLANDS**/

        /*moving MN to the 2 island*/
        testGameBoard.placeMotherNature(1);

        /*test merging 2 islands*/
        testGameBoard.moveStudentToIsland(Color.GREEN, 0);
        testGameBoard.moveStudentToIsland(Color.RED, 0);
        testGameBoard.moveStudentToIsland(Color.GREEN, 1);
        testGameBoard.moveStudentToIsland(Color.YELLOW, 1);

        testGameBoard.conquerIsland(0, TowerColor.BLACK);
        testGameBoard.conquerIsland(1, TowerColor.BLACK);

        assertEquals(true, testGameBoard.mergeIslands());
        assertEquals(11, testGameBoard.getNumOfIslands());
        assertEquals(0, testGameBoard.getPositionOfMotherNature());

        assertEquals(2, testGameBoard.getNumOfStudentsOnIsland(0, Color.GREEN));
        assertEquals(1, testGameBoard.getNumOfStudentsOnIsland(0, Color.RED));
        assertEquals(1, testGameBoard.getNumOfStudentsOnIsland(0, Color.YELLOW));
        assertEquals(0, testGameBoard.getNumOfStudentsOnIsland(0, Color.PINK));
        assertEquals(0, testGameBoard.getNumOfStudentsOnIsland(0, Color.BLUE));

        assertEquals(2, testGameBoard.getNumOfTowersOnIsland(0));
        assertEquals(TowerColor.BLACK, testGameBoard.getTowersColorOnIsland(0));

        assertEquals(false, testGameBoard.getNoEntryOnIsland(0));

        /*check that the rest of the islands are not modified*/
        for (int i = 1; i < testGameBoard.getNumOfIslands(); i++){
            for (Color color: Color.values())
                assertEquals(0, testGameBoard.getNumOfStudentsOnIsland(i, color));

            assertEquals(0, testGameBoard.getNumOfTowersOnIsland(i));
            assertEquals(null, testGameBoard.getTowersColorOnIsland(i));
            assertEquals(false, testGameBoard.getNoEntryOnIsland(i));
        }


        /**MERGE LAST AND THE FIRST ISLANDS**/

        /*place MN on the last island and conquer it*/
        testGameBoard.placeMotherNature(10);

        testGameBoard.moveStudentToIsland(Color.PINK, 10);
        testGameBoard.moveStudentToIsland(Color.BLUE, 10);

        /*test merging first and the last island*/
        testGameBoard.conquerIsland(10, TowerColor.BLACK);

        assertEquals(true, testGameBoard.mergeIslands());
        assertEquals(10, testGameBoard.getNumOfIslands());
        assertEquals(9, testGameBoard.getPositionOfMotherNature());

        assertEquals(2, testGameBoard.getNumOfStudentsOnIsland(9, Color.GREEN));
        assertEquals(1, testGameBoard.getNumOfStudentsOnIsland(9, Color.RED));
        assertEquals(1, testGameBoard.getNumOfStudentsOnIsland(9, Color.YELLOW));
        assertEquals(1, testGameBoard.getNumOfStudentsOnIsland(9, Color.PINK));
        assertEquals(1, testGameBoard.getNumOfStudentsOnIsland(9, Color.BLUE));

        assertEquals(3, testGameBoard.getNumOfTowersOnIsland(9));
        assertEquals(TowerColor.BLACK, testGameBoard.getTowersColorOnIsland(9));

        assertEquals(false, testGameBoard.getNoEntryOnIsland(9));

        /*check that the rest of the islands are not modified*/
        for (int i = 0; i < testGameBoard.getNumOfIslands()-1; i++){
            for (Color color: Color.values())
                assertEquals(0, testGameBoard.getNumOfStudentsOnIsland(i, color));

            assertEquals(0, testGameBoard.getNumOfTowersOnIsland(i));
            assertEquals(null, testGameBoard.getTowersColorOnIsland(i));
            assertEquals(false, testGameBoard.getNoEntryOnIsland(i));
        }

        /**TEST THAT NON-CONSECUTIVE ISLANDS ARE NOT MERGED**/

        /*place MN on the last island*/
        testGameBoard.placeMotherNature(9);

        testGameBoard.moveStudentToIsland(Color.PINK, 7);
        testGameBoard.moveStudentToIsland(Color.BLUE, 7);

        /*test that non-consecutive islands are not merged*/
        testGameBoard.conquerIsland(7, TowerColor.BLACK);

        assertEquals(false, testGameBoard.mergeIslands());
        assertEquals(10, testGameBoard.getNumOfIslands());
        assertEquals(9, testGameBoard.getPositionOfMotherNature());

        assertEquals(2, testGameBoard.getNumOfStudentsOnIsland(9, Color.GREEN));
        assertEquals(1, testGameBoard.getNumOfStudentsOnIsland(9, Color.RED));
        assertEquals(1, testGameBoard.getNumOfStudentsOnIsland(9, Color.YELLOW));
        assertEquals(1, testGameBoard.getNumOfStudentsOnIsland(9, Color.PINK));
        assertEquals(1, testGameBoard.getNumOfStudentsOnIsland(9, Color.BLUE));

        assertEquals(3, testGameBoard.getNumOfTowersOnIsland(9));
        assertEquals(TowerColor.BLACK, testGameBoard.getTowersColorOnIsland(9));

        assertEquals(false, testGameBoard.getNoEntryOnIsland(9));

        /*check that the rest of the islands are not modified*/
        for (int i = 0; i < testGameBoard.getNumOfIslands()-1; i++){
            if(i == 7){
                assertEquals(1, testGameBoard.getNumOfStudentsOnIsland(7, Color.PINK));
                assertEquals(1, testGameBoard.getNumOfStudentsOnIsland(7, Color.BLUE));

                assertEquals(1, testGameBoard.getNumOfTowersOnIsland(i));
                assertEquals(TowerColor.BLACK, testGameBoard.getTowersColorOnIsland(i));
            }
            else{
                for (Color color: Color.values())
                    assertEquals(0, testGameBoard.getNumOfStudentsOnIsland(i, color));

                assertEquals(0, testGameBoard.getNumOfTowersOnIsland(i));
                assertEquals(null, testGameBoard.getTowersColorOnIsland(i));
            }

            assertEquals(false, testGameBoard.getNoEntryOnIsland(i));
        }

        /**TEST MERGING BY COVERING A GAP**/

        testGameBoard.moveStudentToIsland(Color.RED, 8);
        testGameBoard.moveStudentToIsland(Color.BLUE, 8);

        /*test merging first and the last island*/
        testGameBoard.conquerIsland(8, TowerColor.BLACK);

        assertEquals(true, testGameBoard.mergeIslands());
        assertEquals(8, testGameBoard.getNumOfIslands());
        assertEquals(7, testGameBoard.getPositionOfMotherNature());

        assertEquals(2, testGameBoard.getNumOfStudentsOnIsland(7, Color.GREEN));
        assertEquals(2, testGameBoard.getNumOfStudentsOnIsland(7, Color.RED));
        assertEquals(1, testGameBoard.getNumOfStudentsOnIsland(7, Color.YELLOW));
        assertEquals(2, testGameBoard.getNumOfStudentsOnIsland(7, Color.PINK));
        assertEquals(3, testGameBoard.getNumOfStudentsOnIsland(7, Color.BLUE));

        assertEquals(5, testGameBoard.getNumOfTowersOnIsland(7));
        assertEquals(TowerColor.BLACK, testGameBoard.getTowersColorOnIsland(7));

        assertEquals(false, testGameBoard.getNoEntryOnIsland(7));

        /*check that the rest of the islands are not modified*/
        for (int i = 0; i < testGameBoard.getNumOfIslands()-1; i++){
            for (Color color: Color.values())
                assertEquals(0, testGameBoard.getNumOfStudentsOnIsland(i, color));

            assertEquals(0, testGameBoard.getNumOfTowersOnIsland(i));
            assertEquals(null, testGameBoard.getTowersColorOnIsland(i));
            assertEquals(false, testGameBoard.getNoEntryOnIsland(i));
        }
    }


    /*MAY BE IMPLEMENTED INSIDE GAME TEST !*/
    @Test
    void moveStudentToIsland(){
        GameBoard testGameBoard = GameBoard.getInstanceOfGameBoard();
        Player testPlayer = new Player("Test", TowerColor.BLACK, AssistantType.ONE, 8);
        testGameBoard.init(2);
        testGameBoard.setCurrentPlayer(testPlayer);

        /*moving student from entrance to island*/
        testPlayer.addStudentToEntrance(Color.GREEN);
        testGameBoard.moveStudentToIsland(testPlayer, Color.GREEN, 1);

        assertEquals(1, testGameBoard.getNumOfStudentsOnIsland(1, Color.GREEN));
        assertEquals(0, testPlayer.getNumOfStudentsInEntrance());

        /*adding a student to island*/
        testGameBoard.moveStudentToIsland(Color.GREEN, 1);

        assertEquals(2, testGameBoard.getNumOfStudentsOnIsland(1, Color.GREEN));
        assertEquals(0, testPlayer.getNumOfStudentsInEntrance());

        /*exception control*/
        assertThrows(IllegalArgumentException.class, ()->{
            testGameBoard.moveStudentToIsland(testPlayer, Color.GREEN, 0);
        } );
        assertThrows(IllegalArgumentException.class, ()->{
            testGameBoard.moveStudentToIsland(testPlayer, Color.GREEN, 13);
        } );
        assertThrows(IllegalArgumentException.class, ()->{
            testGameBoard.moveStudentToIsland(Color.GREEN, 0);
        } );
        assertThrows(IllegalArgumentException.class, ()->{
            testGameBoard.moveStudentToIsland(Color.GREEN, 13);
        } );

        assertThrows(NoEnoughStudentsException.class, ()->{
            testGameBoard.moveStudentToIsland(testPlayer, Color.RED, 1);
        } );
        assertThrows(NoEnoughStudentsException.class, ()->{
            testGameBoard.moveStudentToIsland(testPlayer, Color.BLUE, 1);
        } );
        assertThrows(NoEnoughStudentsException.class, ()->{
            testGameBoard.moveStudentToIsland(testPlayer, Color.PINK, 1);
        } );
        assertThrows(NoEnoughStudentsException.class, ()->{
            testGameBoard.moveStudentToIsland(testPlayer, Color.YELLOW, 1);
        } );
    }
}
