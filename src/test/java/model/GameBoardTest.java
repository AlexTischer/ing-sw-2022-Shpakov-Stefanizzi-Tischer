package model;

import controller.*;
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
    void moveStudentToIsland(){
        GameBoard testGameBoard = GameBoard.getInstanceOfGameBoard();
        Player testPlayer = new Player("Test", TowerColor.BLACK, AssistantType.ONE, 8);
        testGameBoard.init(2);
        testGameBoard.setCurrentPlayer(testPlayer);

        testPlayer.addStudentToEntrance(Color.GREEN);

        //int oldNumOfStudentsOnIsland =
        testGameBoard.moveStudentToIsland(Color.GREEN, 0);

    }
}
