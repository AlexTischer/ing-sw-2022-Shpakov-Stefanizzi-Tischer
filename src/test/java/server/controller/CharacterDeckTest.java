package server.controller;

import it.polimi.ingsw.server.controller.CharacterDeck;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterDeckTest extends TestCase {

    @Test
    void popCharacterTest(){
        CharacterDeck testDeck = new CharacterDeck();

        for (int i = 0; i < 12; i++)
            testDeck.popCharacter();

        assertThrows(IndexOutOfBoundsException.class, () -> testDeck.popCharacter());
    }
}