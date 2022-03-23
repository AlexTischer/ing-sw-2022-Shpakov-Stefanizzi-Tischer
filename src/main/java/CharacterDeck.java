import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CharacterDeck {
    private List<Character> characters;

    public CharacterDeck(int size) {

        if( size != 12 && size != 8 )
            throw new IllegalArgumentException("Error: only 12 or 8 characters are allowed");

        this.characters = new ArrayList<Character>(size);

        //Creates 12 or 8 characters and adds them to characters List

        /*shuffles the order of characters*/
        Collections.shuffle(characters);
    }

    //returns a random Character from characters
    public Character popCharacter(){

        return characters.remove(0);
    }
}
