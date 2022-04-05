package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CharacterDeck {
    private List<Character> characters;

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

    //returns a random model.Character from characters
    public Character popCharacter(){
        return characters.remove(0);
    }
}
