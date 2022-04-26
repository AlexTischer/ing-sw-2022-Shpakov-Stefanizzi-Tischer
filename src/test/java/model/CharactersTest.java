package model;

import controller.CharacterDeck;
import controller.Game;
import exceptions.NoEnoughCoinsException;
import exceptions.NoEnoughEntryTilesException;
import junit.framework.TestCase;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

public class CharactersTest extends TestCase {
    ArrayList<String> playerNames = new ArrayList<>();
    Player testPlayer = new Player("test", TowerColor.BLACK,AssistantType.ONE,8);
    AssistantDeck assistantDeck = new AssistantDeck();
    CharacterDeck characterDeck = new CharacterDeck();

    @Test
    void Character1Test(){
        playerNames.add("a");
        playerNames.add("b");
        Game gametest = Game.getInstanceOfGame();
        gametest.init(playerNames,true,assistantDeck,characterDeck);
        GameBoard testGameBoard = gametest.getGameBoard();
        testGameBoard.setCurrentPlayer(testPlayer);
        testPlayer.addCoins(5);

        Character1 character1test = new Character1();
        character1test.initialFill(gametest);
        testGameBoard.setCurrentCharacter(character1test);

        character1test.buy();

        ArrayList<Color> testStudents;
        testStudents = character1test.getStudents();
        Color firstStudent = testStudents.get(0);
        gametest.activateCharacter(firstStudent,0);

        assertEquals(testGameBoard.getNumOfStudentsOnIsland(0, firstStudent),1);

    }

    @Test
    void Character2Test(){
        playerNames.add("a");
        playerNames.add("b");
        Game gametest = Game.getInstanceOfGame();
        gametest.init(playerNames,true,assistantDeck,characterDeck);
        GameBoard testGameBoard = gametest.getGameBoard();
        testGameBoard.setCurrentPlayer(testPlayer);
        testPlayer.addCoins(5);
        testPlayer.addProfessor(Color.GREEN);

        Character2 character2test = new Character2();
        character2test.initialFill(gametest);
        testGameBoard.setCurrentCharacter(character2test);

        character2test.buy();

        testGameBoard.addStudentToIsland(Color.GREEN,2);
        testGameBoard.addStudentToIsland(Color.GREEN, 2);
        testGameBoard.addStudentToIsland(Color.BLUE,2);
        testGameBoard.conquerIsland(2,TowerColor.BLACK);

        gametest.calculateInfluence(2);

        assertEquals(gametest.calculateInfluence(2),5);

    }

    @Test
    void Character5Test(){
        playerNames.add("a");
        playerNames.add("b");
        Game gametest = Game.getInstanceOfGame();
        gametest.init(playerNames,true,assistantDeck,characterDeck);
        GameBoard testGameBoard = gametest.getGameBoard();
        testGameBoard.setCurrentPlayer(testPlayer);
        testPlayer.addCoins(1);

        Character5 character5test = new Character5();
        character5test.initialFill(gametest);
        testGameBoard.setCurrentCharacter(character5test);

        try {
            character5test.buy();
            assertTrue("false", false);
        }
        catch (NoEnoughCoinsException e){

        }

        testPlayer.addCoins(3);
        character5test.buy();
        gametest.activateCharacter(3);

        assertEquals(testGameBoard.getNoEntryOnIsland(3), true);

        gametest.activateCharacter(4);
        gametest.activateCharacter(5);
        gametest.activateCharacter(6);

        try {
            gametest.activateCharacter(7);
            assertTrue("false", false);
        }
        catch (NoEnoughEntryTilesException e){

        }
        testGameBoard.addNoEntryTile();
    }

    @Test
    void Character8Test(){
        playerNames.add("a");
        playerNames.add("b");
        Game gametest = Game.getInstanceOfGame();
        gametest.init(playerNames,true,assistantDeck,characterDeck);
        GameBoard testGameBoard = gametest.getGameBoard();
        testGameBoard.setCurrentPlayer(testPlayer);
        testPlayer.addCoins(5);
        testGameBoard.refillAssistants(testPlayer);
        testPlayer.setPlayedAssistantRank(2);

        Character8 character8test = new Character8();
        character8test.initialFill(gametest);
        testGameBoard.setCurrentCharacter(character8test);

        character8test.buy();

        testGameBoard.placeMotherNature(3);
        gametest.moveMotherNature(3);

        assertEquals(testGameBoard.getPositionOfMotherNature(), 6);

        gametest.moveMotherNature(-1);
        assertEquals(testGameBoard.getPositionOfMotherNature(), 5);

    }

    @Test
    void Character12Test() {
        playerNames.add("a");
        playerNames.add("b");
        Game gametest = Game.getInstanceOfGame();
        gametest.init(playerNames, true, assistantDeck, characterDeck);
        GameBoard testGameBoard = gametest.getGameBoard();
        testGameBoard.setCurrentPlayer(testPlayer);
        testPlayer.addCoins(5);
        testPlayer.addProfessor(Color.GREEN);

        Character12 character12test = new Character12();
        character12test.initialFill(gametest);
        testGameBoard.setCurrentCharacter(character12test);

        character12test.buy();

        testGameBoard.addStudentToIsland(Color.GREEN, 2);
        testGameBoard.addStudentToIsland(Color.GREEN, 2);
        testGameBoard.addStudentToIsland(Color.BLUE, 2);
        testGameBoard.conquerIsland(2, TowerColor.BLACK);

        gametest.activateCharacter(2);

        assertEquals(gametest.calculateInfluence(2), 3);
    }

    @Test
    void Character10Test(){
        playerNames.add("a");
        playerNames.add("b");
        Game gametest = Game.getInstanceOfGame();
        gametest.init(playerNames,true,assistantDeck,characterDeck);
        GameBoard testGameBoard = gametest.getGameBoard();
        testGameBoard.setCurrentPlayer(testPlayer);
        testPlayer.addCoins(5);

        Character10 character10test = new Character10();
        character10test.initialFill(gametest);
        testGameBoard.setCurrentCharacter(character10test);

        character10test.buy();

        ArrayList<Color> testStudents;
        testStudents = character10test.getStudents();
        Color firstStudent = testStudents.get(0);
        gametest.activateCharacter(firstStudent);

        assertEquals(testPlayer.getNumOfStudentsInDining(firstStudent), 1);


    }
}
