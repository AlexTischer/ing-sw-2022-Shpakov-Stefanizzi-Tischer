package server.model;

import junit.framework.TestCase;
import exceptions.NoEnoughCoinsException;
import exceptions.NumOfCoinsExceeded;
import exceptions.NumOfStudentsExceeded;
import exceptions.StudentNotFoundException;
import org.junit.jupiter.api.Test;
import server.controller.CharacterDeck;
import server.controller.Game;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameBoardTest extends TestCase {

    @Test
    void initTest(){
        GameBoard testGameBoard = GameBoard.getInstanceOfGameBoard();
        testGameBoard.init(null,2);

        assertEquals(12, testGameBoard.getNumOfIslands());
        assertEquals(2, testGameBoard.getNumOfClouds());

        testGameBoard.init(null,3);
        assertEquals(3, testGameBoard.getNumOfClouds());

        testGameBoard.init(null,4);
        assertEquals(4, testGameBoard.getNumOfClouds());
    }


    @Test
    void refillEntranceTest(){
        /*2 players game*/
        GameBoard testGameBoard = GameBoard.getInstanceOfGameBoard();
        testGameBoard.init(null,2);
        Player testPlayer = new Player("Test", TowerColor.BLACK, AssistantType.ONE, 8);
        testGameBoard.refillEntrance(testPlayer);
        assertEquals(7, testPlayer.getNumOfStudentsInEntrance());

        /*3 players game*/
        testGameBoard.init(null, 3);
        testPlayer = new Player("Test", TowerColor.BLACK, AssistantType.ONE, 6);
        testGameBoard.refillEntrance(testPlayer);
        assertEquals(9, testPlayer.getNumOfStudentsInEntrance());

        /*4 players game*/
        testGameBoard.init(null,4);
        testPlayer = new Player("Test", TowerColor.BLACK, AssistantType.ONE, 8);
        testGameBoard.refillEntrance(testPlayer);
        assertEquals(7, testPlayer.getNumOfStudentsInEntrance());

    }

    @Test
    void getCurrentPlayerTest(){
        GameBoard testGameBoard = GameBoard.getInstanceOfGameBoard();
        Player testPlayer = new Player("Test", TowerColor.BLACK, AssistantType.ONE, 8);
        testGameBoard.init(null,2);
        testGameBoard.setCurrentPlayer(testPlayer);

        assertEquals(testPlayer, testGameBoard.getCurrentPlayer());
    }

    @Test
    void mergeIslandsTest(){
        GameBoard testGameBoard = GameBoard.getInstanceOfGameBoard();
        testGameBoard.init(null,2);

        /**MERGE 2 CONSECUTIVE ISLANDS**/

        /*moving MN to the 2 island*/
        testGameBoard.placeMotherNatureTEST(1);

        /*test merging 2 islands*/

        //removing init students on islands
        testGameBoard.addStudentToIsland(Color.GREEN, 0);
        testGameBoard.addStudentToIsland(Color.RED, 0);
        testGameBoard.addStudentToIsland(Color.GREEN, 1);
        testGameBoard.addStudentToIsland(Color.YELLOW, 1);

        testGameBoard.conquerIslandTEST(0, TowerColor.BLACK);
        testGameBoard.conquerIslandTEST(1, TowerColor.BLACK);

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


        /**MERGE THE LAST AND THE FIRST ISLANDS**/

        /*place MN on the last island and conquer it*/
        testGameBoard.placeMotherNatureTEST(10);

        testGameBoard.addStudentToIsland(Color.PINK, 10);
        testGameBoard.addStudentToIsland(Color.BLUE, 10);

        /*test merging first and the last island*/
        testGameBoard.conquerIslandTEST(10, TowerColor.BLACK);

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
        testGameBoard.placeMotherNatureTEST(9);

        testGameBoard.addStudentToIsland(Color.PINK, 7);
        testGameBoard.addStudentToIsland(Color.BLUE, 7);

        /*test that non-consecutive islands are not merged*/
        testGameBoard.conquerIslandTEST(7, TowerColor.BLACK);

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

        testGameBoard.addStudentToIsland(Color.RED, 8);
        testGameBoard.addStudentToIsland(Color.BLUE, 8);

        /*test merging first and the last island*/
        testGameBoard.conquerIslandTEST(8, TowerColor.BLACK);

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

    @Test
    void refillCloudsTest(){
        GameBoard testGameBoard2 = GameBoard.getInstanceOfGameBoard();
        testGameBoard2.init(null,2);

        assertEquals(2, testGameBoard2.getNumOfClouds());

        /*maxNumOfStudents on cloud is 3*/
        testGameBoard2.refillClouds();

        for (int i = 0; i < testGameBoard2.getNumOfClouds(); i++)
            assertEquals(3, testGameBoard2.getNumOfStudentsOnCloud(i));


        GameBoard testGameBoard3 = GameBoard.getInstanceOfGameBoard();
        testGameBoard3.init(null,3);

        assertEquals(3, testGameBoard3.getNumOfClouds());

        /*maxNumOfStudents on cloud is 4*/
        testGameBoard3.refillClouds();

        for (int i = 0; i < testGameBoard3.getNumOfClouds(); i++)
            assertEquals(4, testGameBoard3.getNumOfStudentsOnCloud(i));


        GameBoard testGameBoard4 = GameBoard.getInstanceOfGameBoard();
        testGameBoard4.init(null,4);

        assertEquals(4, testGameBoard4.getNumOfClouds());

        /*maxNumOfStudents on cloud is 3*/
        testGameBoard4.refillClouds();

        for (int i = 0; i < testGameBoard4.getNumOfClouds(); i++)
            assertEquals(3, testGameBoard4.getNumOfStudentsOnCloud(i));

    }

    @Test
    void addAndRemoveProfessorTest(){
        GameBoard testGameBoard = GameBoard.getInstanceOfGameBoard();
        testGameBoard.init(null,2);

        Player testPlayer = new Player("Test", TowerColor.BLACK, AssistantType.ONE, 8);

        /*precedent list of professors*/
        ArrayList<Color> oldProfessors = testPlayer.getProfessorsColor();
        assertTrue(oldProfessors.isEmpty());

        /*add a professor*/
        testGameBoard.addProfessor(testPlayer, Color.GREEN);

        /*professor map changed*/
        assertFalse(oldProfessors.equals(testPlayer.getProfessorsColor()));

        /*check size*/
        assertEquals(1, testPlayer.getProfessorsColor().size());

        /*check if green is contained*/
        for (Color c: Color.values()){
            if (c.equals(Color.GREEN))
                assertTrue(testPlayer.getProfessorsColor().contains(c));
            else
                assertFalse(testPlayer.getProfessorsColor().contains(c));
        }


        oldProfessors = testPlayer.getProfessorsColor();

        /*add a professor*/
        testGameBoard.addProfessor(testPlayer, Color.BLUE);

        /*professor map changed*/
        assertTrue(!oldProfessors.equals(testPlayer.getProfessorsColor()));

        /*check size*/
        assertEquals(2, testPlayer.getProfessorsColor().size());

        /*check if green and blue are contained*/
        for (Color c: Color.values()){
            if (c.equals(Color.GREEN) || c.equals(Color.BLUE))
                assertTrue(testPlayer.getProfessorsColor().contains(c));
            else
                assertFalse(testPlayer.getProfessorsColor().contains(c));
        }


        oldProfessors = testPlayer.getProfessorsColor();

        /*add an existing professor*/
        testGameBoard.addProfessor(testPlayer, Color.BLUE);

        /*professor map has not changed*/
        assertTrue(oldProfessors.equals(testPlayer.getProfessorsColor()));

        /*check size*/
        assertEquals(2, testPlayer.getProfessorsColor().size());

        /*check that there are no duplicates*/
        for (int i = 0; i < testPlayer.getProfessorsColor().size(); i++){
            for (int j = i+1; j < testPlayer.getProfessorsColor().size(); j++){
                assertFalse(testPlayer.getProfessorsColor().get(i).equals(testPlayer.getProfessorsColor().get(j)));
            }
        }

        /*test removeProfessor*/

        /*remove one professor*/
        testPlayer.removeProfessor(Color.BLUE);

        /*professor map has been changed*/
        assertFalse(oldProfessors.equals(testPlayer.getProfessorsColor()));

        /*check size*/
        assertEquals(1, testPlayer.getProfessorsColor().size());

        /*check if green is contained*/
        for (Color c: Color.values()){
            if (c.equals(Color.GREEN))
                assertTrue(testPlayer.getProfessorsColor().contains(c));
            else
                assertFalse(testPlayer.getProfessorsColor().contains(c));
        }

        /*remove the last professor*/
        testPlayer.removeProfessor(Color.GREEN);

        /*professor list has become empty*/
        assertTrue(testPlayer.getProfessorsColor().isEmpty());

    }

    @Test
    void addAndGetCoinTest(){
        GameBoard testGameBoard = GameBoard.getInstanceOfGameBoard();
        testGameBoard.init(null,2);
        assertEquals(20, testGameBoard.getNumOfCoins());

        assertThrows(NumOfCoinsExceeded.class, () ->{testGameBoard.addCoin();});

        testGameBoard.getCoin();
        assertEquals(19, testGameBoard.getNumOfCoins());

        for (int i = 0; i < 19; i++)
            testGameBoard.getCoin();

        assertThrows(NoEnoughCoinsException.class, ()->{testGameBoard.getCoin();});

        testGameBoard.addCoin();
        assertEquals(testGameBoard.getNumOfCoins(),1);

    }

    @Test
    void setAndGetCurrentCharactersTest(){
        GameBoard testGameBoard = GameBoard.getInstanceOfGameBoard();
        testGameBoard.init(null,2);

        testGameBoard.setCurrentCharacterToDefault(new Character1());
        assertTrue(testGameBoard.getCurrentCharacter() instanceof Character1);
    }

    @Test
    void setAndBuyCharacters(){
        Game gametest = Game.getInstanceOfGame();
        ArrayList<String> playersNames = new ArrayList<String>();
        playersNames.add("a");
        playersNames.add("b");
        CharacterDeck characterDeck = new CharacterDeck();

        //init must be run on separate thread since it waits the call of useAssistant for each player
        gametest.init(playersNames,true, characterDeck);

        gametest.useAssistant(1);
        gametest.useAssistant(2);


        GameBoard testGameBoard = gametest.getGameBoard();

        Character1 character1 = new Character1();
        character1.initialFill(gametest);

        Character2 character2 = new Character2();
        character2.initialFill(gametest);

        Character3 character3 = new Character3();
        character3.initialFill(gametest);

        Character[] characters = new Character[3];
        characters[0] = character1;
        characters[1] = character2;
        characters[2] = character3;
        testGameBoard.setPlayedCharacters(characters);

        //take 10 students from gameBoard to simulate paying to player
        for (int i = 0; i < 10; i++)
            testGameBoard.getCoin();

        testGameBoard.addCoins(gametest.getCurrentPlayer(), 10);

        testGameBoard.buyCharacter(0);

        assertTrue(testGameBoard.getCurrentCharacter() instanceof Character1);

        testGameBoard.buyCharacter(2);

        assertTrue(testGameBoard.getCurrentCharacter() instanceof Character3);
    }

    @Test
    void addAndRemoveStudentInEntranceTest(){
        GameBoard testGameBoard = GameBoard.getInstanceOfGameBoard();
        /*maxNumOfStudents is 7*/
        testGameBoard.init(null,2);

        Player testPlayer = new Player("Test", TowerColor.BLACK, AssistantType.ONE, 8);

        assertEquals(0, testPlayer.getNumOfStudentsInEntrance());

        /*test that size has changed*/
        testGameBoard.addStudentToEntrance(testPlayer, Color.GREEN);
        assertEquals(1, testPlayer.getNumOfStudentsInEntrance());

        /*test exception when entrance has reached maximum*/
        for (int i = 0; i < 6; i++)
            testGameBoard.addStudentToEntrance(testPlayer, Color.GREEN);
        assertThrows(NumOfStudentsExceeded.class, ()->{testGameBoard.addStudentToEntrance(testPlayer, Color.GREEN);});

        /*test colors added*/
        for (Color c: testPlayer.getStudentsInEntrance())
            assertTrue(c.equals(Color.GREEN));

        /*test that size has changed*/
        testGameBoard.removeStudentFromEntrance(testPlayer, Color.GREEN);
        testGameBoard.removeStudentFromEntrance(testPlayer, Color.GREEN);
        testGameBoard.removeStudentFromEntrance(testPlayer, Color.GREEN);
        assertEquals(4, testPlayer.getNumOfStudentsInEntrance());

        /*add different colors with duplicates*/
        testGameBoard.addStudentToEntrance(testPlayer, Color.RED);
        testGameBoard.addStudentToEntrance(testPlayer, Color.RED);
        testGameBoard.addStudentToEntrance(testPlayer, Color.YELLOW);
        for (Color c: testPlayer.getStudentsInEntrance()){
            /*if color is different from those added, stop testing*/
            if (!c.equals(Color.GREEN) && !c.equals(Color.RED) && !c.equals(Color.YELLOW))
                assertTrue(false);
        }

        /*remove only 1 from 2 red colors*/
        testGameBoard.removeStudentFromEntrance(testPlayer, Color.RED);
        assertEquals(6, testPlayer.getNumOfStudentsInEntrance());

        int numOfGreen = 0;
        int numOfRed = 0;
        int numOfYellow = 0;
        for (Color c: testPlayer.getStudentsInEntrance()){
            switch (c){
                case GREEN:
                    numOfGreen++;
                    break;
                case RED:
                    numOfRed++;
                    break;
                case YELLOW:
                    numOfYellow++;
                    break;
                default:/*if color is different from those added, stop testing*/
                    assertTrue(false);
            }
        }

        /*Test that number of other colors is not changed*/
        assertEquals(4, numOfGreen);
        assertEquals(1, numOfRed);
        assertEquals(1, numOfYellow);


        /*test removal exception*/
        for (int i=0; i < 4; i++)
            testGameBoard.removeStudentFromEntrance(testPlayer, Color.GREEN);

        testGameBoard.removeStudentFromEntrance(testPlayer, Color.RED);
        testGameBoard.removeStudentFromEntrance(testPlayer, Color.YELLOW);

        for (Color c: Color.values())
            assertThrows(StudentNotFoundException.class, ()->{testGameBoard.removeStudentFromEntrance(testPlayer, c);});

    }

    @Test
    void addStudentAndRemoveStudentInDining(){
        GameBoard testGameBoard = GameBoard.getInstanceOfGameBoard();
        testGameBoard.init(null,2);
        Player testPlayer = new Player("Test", TowerColor.BLACK, AssistantType.ONE, 8);

        /*test that there are no students at the beginning*/
        for (Color c: Color.values())
            assertEquals(0, testPlayer.getNumOfStudentsInDining(c));

        /*test adding exactly one student*/
        testGameBoard.addStudentToDining(testPlayer, Color.GREEN);
        for (Color c: Color.values()){
            if (c.equals(Color.GREEN))
                assertEquals(1, testPlayer.getNumOfStudentsInDining(c));
            else
                assertEquals(0, testPlayer.getNumOfStudentsInDining(c));
        }

        /*test adding student of different color*/
        testGameBoard.addStudentToDining(testPlayer, Color.BLUE);
        for (Color c: Color.values()){
            if (c.equals(Color.GREEN))
                assertEquals(1, testPlayer.getNumOfStudentsInDining(c));
            else if (c.equals(Color.BLUE))
                assertEquals(1, testPlayer.getNumOfStudentsInDining(c));
            else
                assertEquals(0, testPlayer.getNumOfStudentsInDining(c));
        }

        /*test exception when dining has reached max number of students*/
        for (int i=0; i < 9; i++)
            testGameBoard.addStudentToDining(testPlayer, Color.GREEN);
        assertThrows(NumOfStudentsExceeded.class, ()->{testGameBoard.addStudentToDining(testPlayer, Color.GREEN);});

        /*test student removal*/
        testGameBoard.removeStudentFromDining(testPlayer, Color.BLUE);
        for (Color c: Color.values()){
            if (c.equals(Color.GREEN))
                assertEquals(10, testPlayer.getNumOfStudentsInDining(c));
            else
                assertEquals(0, testPlayer.getNumOfStudentsInDining(c));
        }

        /*test exception when there are no students left in dining*/
        assertThrows(StudentNotFoundException.class ,()->{testGameBoard.removeStudentFromDining(testPlayer, Color.BLUE);});

        for (int i = 0; i < 10; i++)
            testGameBoard.removeStudentFromDining(testPlayer, Color.GREEN);
        
        for (Color c: Color.values())
            assertEquals(0, testPlayer.getNumOfStudentsInDining(c));

        for (Color c: Color.values())
            assertThrows(StudentNotFoundException.class ,()->{testGameBoard.removeStudentFromDining(testPlayer, c);});
    }


    @Test
    void moveStudentToIslandTest(){
        GameBoard testGameBoard = GameBoard.getInstanceOfGameBoard();
        Player testPlayer = new Player("Test", TowerColor.BLACK, AssistantType.ONE, 8);
        testGameBoard.init(null,2);
        testGameBoard.setCurrentPlayer(testPlayer);

        /*moving student from entrance to island*/
        testGameBoard.addStudentToEntrance(testPlayer, Color.GREEN);
        testGameBoard.addStudentToIsland(testPlayer, Color.GREEN, 0);

        assertEquals(1, testGameBoard.getNumOfStudentsOnIsland(0, Color.GREEN));
        assertEquals(0, testPlayer.getNumOfStudentsInEntrance());

        /*adding a student to island*/
        testGameBoard.addStudentToIsland(Color.GREEN, 0);

        assertEquals(2, testGameBoard.getNumOfStudentsOnIsland(0, Color.GREEN));
        assertEquals(0, testPlayer.getNumOfStudentsInEntrance());

        /*exception control*/
        assertThrows(IllegalArgumentException.class, ()->{
            testGameBoard.addStudentToIsland(testPlayer, Color.GREEN, -1);
        } );
        assertThrows(IllegalArgumentException.class, ()->{
            testGameBoard.addStudentToIsland(testPlayer, Color.GREEN, 12);
        } );
        assertThrows(IllegalArgumentException.class, ()->{
            testGameBoard.addStudentToIsland(Color.GREEN, -1);
        } );
        assertThrows(IllegalArgumentException.class, ()->{
            testGameBoard.addStudentToIsland(Color.GREEN, 12);
        } );

        assertThrows(StudentNotFoundException.class, ()->{
            testGameBoard.addStudentToIsland(testPlayer, Color.GREEN, 0);
        } );
        assertThrows(StudentNotFoundException.class, ()->{
            testGameBoard.addStudentToIsland(testPlayer, Color.RED, 0);
        } );
        assertThrows(StudentNotFoundException.class, ()->{
            testGameBoard.addStudentToIsland(testPlayer, Color.BLUE, 0);
        } );
        assertThrows(StudentNotFoundException.class, ()->{
            testGameBoard.addStudentToIsland(testPlayer, Color.PINK, 0);
        } );
        assertThrows(StudentNotFoundException.class, ()->{
            testGameBoard.addStudentToIsland(testPlayer, Color.YELLOW, 0);
        } );
    }

    @Test
    void useCloudTest(){
        GameBoard testGameBoard = GameBoard.getInstanceOfGameBoard();
        Player testPlayer = new Player("Test", TowerColor.BLACK, AssistantType.ONE, 8);
        testGameBoard.init(null,2);
        testGameBoard.setCurrentPlayer(testPlayer);

        testGameBoard.refillClouds();
        testGameBoard.useCloud(0);
        assertEquals(testPlayer.getNumOfStudentsInEntrance(),3);

    }


    @Test
    void addProfessorTest(){
        GameBoard testGameBoard = GameBoard.getInstanceOfGameBoard();
        Player testPlayer = new Player("Test", TowerColor.BLACK, AssistantType.ONE, 8);
        testGameBoard.init(null,2);
        testGameBoard.setCurrentPlayer(testPlayer);

        testGameBoard.addProfessor(testPlayer, Color.RED);
        assertTrue(testPlayer.getProfessorsColor().contains(Color.RED));
    }

    @Test
    void removeProfessorTest(){
        GameBoard testGameBoard = GameBoard.getInstanceOfGameBoard();
        Player testPlayer = new Player("Test", TowerColor.BLACK, AssistantType.ONE, 8);
        testGameBoard.init(null,2);
        testGameBoard.setCurrentPlayer(testPlayer);

        testGameBoard.addProfessor(testPlayer, Color.RED);
        assertTrue(testPlayer.getProfessorsColor().contains(Color.RED));

        testGameBoard.removeProfessor(testPlayer,Color.RED);
        assertTrue(testPlayer.getProfessorsColor().isEmpty());
    }

}
