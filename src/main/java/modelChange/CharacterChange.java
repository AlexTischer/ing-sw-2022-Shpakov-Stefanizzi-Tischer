package modelChange;

import client.model.ClientCharacter;
import client.model.ClientGameBoard;
import server.model.Character;
import server.model.Color;

import java.util.ArrayList;

public class CharacterChange extends ModelChange{

    private int cost;
    private int noEntryTiles;
    private Color[] students;
    private int characterIndex;

    @Override
    public void execute(ClientGameBoard gameBoard){
        gameBoard.setCurrentCharacter(characterIndex);

        gameBoard.getCurrentCharacter().setCost(cost);
        if(noEntryTiles!=-1)
            gameBoard.getCurrentCharacter().setNoEntryTiles(noEntryTiles);
        if(students!=null)
            gameBoard.getCurrentCharacter().setStudents(students);

    }

    public CharacterChange(Character character, int index){ //index must be -1 for defaultCharacter
        cost = character.getCost();
        noEntryTiles = character.getNoEntryTiles();
        students = character.getStudentsSlot();

        characterIndex = index;
    }
}
