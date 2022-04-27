package controller;

import controller.CharacterDeck;
import controller.Game;
import exceptions.NoEntryException;
import junit.framework.TestCase;
import model.*;
import model.Character;
import org.junit.jupiter.api.Test;
import org.junit.runners.model.TestClass;

import java.util.ArrayList;

public class GameTest extends TestCase {
    Game game = Game.getInstanceOfGame();
    AssistantDeck assistantDeck = new AssistantDeck();
    CharacterDeck characterDeck = new CharacterDeck();
    ArrayList<String> playersNames = new ArrayList<String>();

    @Test
    void twoPlayersInitTest(){
        playersNames.add("Pippo");
        playersNames.add("Pluto");

        game.init(playersNames, true, assistantDeck, characterDeck);

        ArrayList<Player> playersNamesTest = new ArrayList<>();
        playersNamesTest = game.getPlayers();

        assertEquals(playersNames.size(), playersNamesTest.size());
        for(Player p : playersNamesTest){
            assertTrue("true", playersNames.contains(p.getName()));
        }
    }

    @Test
    void threePlayersInitTest(){
        playersNames.add("Pippo");
        playersNames.add("Pluto");
        playersNames.add("Paperino");

        game.init(playersNames, true, assistantDeck, characterDeck);

        ArrayList<Player> playersNamesTest = new ArrayList<>();
        playersNamesTest = game.getPlayers();

        assertEquals(playersNames.size(), playersNamesTest.size());
        for(Player p : playersNamesTest){
            assertTrue("true", playersNames.contains(p.getName()));
        }
    }

    @Test
    void fourPlayersInitTest(){
        playersNames.add("Pippo");
        playersNames.add("Pluto");
        playersNames.add("Paperino");
        playersNames.add("Topolino");

        game.init(playersNames, true, assistantDeck, characterDeck);

        ArrayList<Player> playersNamesTest = new ArrayList<>();
        playersNamesTest = game.getPlayers();

        assertEquals(playersNames.size(), playersNamesTest.size());
        for(Player p : playersNamesTest){
            assertTrue("true", playersNames.contains(p.getName()));
        }
    }

    @Test
    void fivePlayersInitTest(){
        playersNames.add("Pippo");
        playersNames.add("Pluto");
        playersNames.add("Paperino");
        playersNames.add("Topolino");
        playersNames.add("Minnie");
        try{
            game.init(playersNames, true, assistantDeck, characterDeck);
        }
        catch (Exception e){
            assertTrue(true);
        }
    }
    @Test
    void calculateInfluenceTest(){
        Player testplayer = new Player("Test", TowerColor.BLACK, AssistantType.ONE,8);
        playersNames.add("a");
        playersNames.add("b");
        game.init(playersNames,true,assistantDeck,characterDeck);
        GameBoard testGameBoard = game.getGameBoard();
        testGameBoard.setCurrentPlayer(testplayer);
        testplayer.addStudentToEntrance(Color.GREEN);
        testplayer.addStudentToEntrance(Color.GREEN);
        testplayer.addStudentToEntrance(Color.BLUE);
        testplayer.addProfessor(Color.GREEN);

        assertEquals(game.calculateInfluence(0), 0);

        game.moveStudentToIsland(Color.GREEN, 0);
        game.moveStudentToIsland(Color.GREEN, 0);
        game.moveStudentToIsland(Color.BLUE, 0);

        assertEquals(game.calculateInfluence(0), 2);
    }

    @Test
    void moveStudentToDiningTest(){
        playersNames.add("a");
        playersNames.add("b");
        game.init(playersNames,true, assistantDeck, characterDeck);
        Player testplayer = new Player("Test", TowerColor.BLACK, AssistantType.ONE,8);
        GameBoard testGameBoard = game.getGameBoard();
        testGameBoard.setCurrentPlayer(testplayer);

        testplayer.addStudentToEntrance(Color.GREEN);
        game.moveStudentToDining(Color.GREEN);

        assertEquals(testplayer.getNumOfStudentsInDining(Color.GREEN), 1);

    }

    @Test
    void addStudentToEntranceTest(){
        playersNames.add("a");
        playersNames.add("b");
        game.init(playersNames,true, assistantDeck, characterDeck);
        Player testplayer = new Player("Test", TowerColor.BLACK, AssistantType.ONE,8);
        GameBoard testGameBoard = game.getGameBoard();
        testGameBoard.setCurrentPlayer(testplayer);

        game.addStudentToEntrance(testplayer, Color.BLUE);
        assertEquals(testplayer.getNumOfStudentsInEntrance(),1);
        assertTrue(testplayer.getStudentsInEntrance().contains(Color.BLUE));

        game.addStudentToEntrance(testplayer);
        game.addStudentToEntrance(testplayer);
        assertEquals(testplayer.getNumOfStudentsInEntrance(),3);

    }

    @Test
    void removeStudentFromDiningTest(){
        playersNames.add("a");
        playersNames.add("b");
        game.init(playersNames,true, assistantDeck, characterDeck);
        Player testplayer = new Player("Test", TowerColor.BLACK, AssistantType.ONE,8);
        GameBoard testGameBoard = game.getGameBoard();
        testGameBoard.setCurrentPlayer(testplayer);

        game.addStudentToEntrance(testplayer,Color.RED);
        game.moveStudentToDining(Color.RED);

        assertEquals(testplayer.getNumOfStudentsInDining(Color.RED),1);

        game.removeStudentFromDining(testplayer,Color.RED);
        assertEquals(testplayer.getNumOfStudentsInDining(Color.RED),0);
    }

    @Test
    void setNoEntryTest(){
        playersNames.add("a");
        playersNames.add("b");
        game.init(playersNames,true, assistantDeck, characterDeck);
        GameBoard testGameBoard = game.getGameBoard();

        assertEquals(testGameBoard.getNoEntryOnIsland(0 ), false);

        game.setNoEntry(0,true);
        assertEquals(testGameBoard.getNoEntryOnIsland(0 ),true);

    }

    @Test
    void noEntryExceptionTest(){
        playersNames.add("a");
        playersNames.add("b");
        game.init(playersNames,true, assistantDeck, characterDeck);
        GameBoard testGameBoard = game.getGameBoard();



        game.setNoEntry(0,true);

        try {
            game.setNoEntry(0, true);
        }
        catch (NoEntryException e){

        }
        try {
            testGameBoard.calculateInfluence(0);
            assertTrue("false", false);
        }
        catch (NoEntryException e){
        }

        game.setNoEntry(0,true);
        game.calculateInfluence(0);


    }

    @Test
    void moveMotherNatureTest(){
        playersNames.add("a");
        playersNames.add("b");
        game.init(playersNames,true, assistantDeck, characterDeck);
        Player testplayer = new Player("Test", TowerColor.BLACK, AssistantType.ONE,8);
        GameBoard testGameBoard = game.getGameBoard();
        testGameBoard.setCurrentPlayer(testplayer);




        testGameBoard.refillAssistants(testplayer);
        //character with rank 8 has 4 steps
        testplayer.setPlayedAssistantRank(8);
        testGameBoard.placeMotherNature(2);
        game.moveMotherNature(3);
        assertEquals(testGameBoard.getPositionOfMotherNature(),5);

        testplayer.setPlayedAssistantRank(6);
        testGameBoard.placeMotherNature(1);
        game.moveMotherNature(-2);
        assertEquals(testGameBoard.getPositionOfMotherNature(),11);


    }
}
