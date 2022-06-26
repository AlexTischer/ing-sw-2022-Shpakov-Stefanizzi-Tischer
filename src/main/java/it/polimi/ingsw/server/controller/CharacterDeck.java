package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.Character;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**This class represents a deck of characters
 * <p>Characters are used only when advancedSettings attribute of {@link Game#init} method is set to true</p>
 * */
public class CharacterDeck {
    private List<Character> characters;

    /**Creates a deck of characters by creating each one and adding it to the list
     * <p>Shuffles the list so that {@link #popCharacter()} returns random character</p>
     */
    public CharacterDeck() {

        this.characters = new ArrayList<Character>(12);
        characters.add(new Character1());
        characters.add(new Character2());
        characters.add(new Character3());
        characters.add(new Character4());
        characters.add(new Character5());
        characters.add(new Character6());
        characters.add(new Character7());
        characters.add(new Character8());
        characters.add(new Character9());
        characters.add(new Character10());
        characters.add(new Character11());
        characters.add(new Character12());
        Collections.shuffle(characters);
    }

    /**
     * @return random character from deck
     */
    public Character popCharacter(){
        return characters.remove(0);
    }
}
