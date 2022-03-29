package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CharacterDeck {
    private List<DefaultCharacter> characters;

    public CharacterDeck(int size) {

        if( size != 12 && size != 8 )
            throw new IllegalArgumentException("Error: only 12 or 8 characters are allowed");

        this.characters = new ArrayList<DefaultCharacter>(size);

        //Creates 12 or 8 characters and adds them to characters List

        /*shuffles the order of characters*/
        Collections.shuffle(characters);
    }

    //returns a random model.Character from characters
    public DefaultCharacter popCharacter(){

        return characters.remove(0);
    }
}
