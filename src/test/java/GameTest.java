import controller.CharacterDeck;
import controller.Game;
import junit.framework.TestCase;
import model.AssistantDeck;
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
    }

    @Test
    void threePlayersInitTest(){
        playersNames.add("Pippo");
        playersNames.add("Pluto");
        playersNames.add("Paperino");

        game.init(playersNames, true, assistantDeck, characterDeck);
    }

    @Test
    void fourPlayersInitTest(){
        playersNames.add("Pippo");
        playersNames.add("Pluto");
        playersNames.add("Paperino");
        playersNames.add("Topolino");

        game.init(playersNames, true, assistantDeck, characterDeck);
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
}
