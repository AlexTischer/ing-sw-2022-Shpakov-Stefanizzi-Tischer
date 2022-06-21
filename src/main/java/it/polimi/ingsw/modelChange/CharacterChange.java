package it.polimi.ingsw.modelChange;

import it.polimi.ingsw.client.model.ClientGameBoard;
import it.polimi.ingsw.server.model.Character;
import it.polimi.ingsw.server.model.Color;

public class CharacterChange extends ModelChange{

    private int id;
    private int cost;
    private int noEntryTiles;
    private Color[] students;
    private int characterIndex;

    @Override
    public void execute(ClientGameBoard gameBoard){
        gameBoard.setCurrentCharacter(characterIndex);

        gameBoard.getCurrentCharacter().setId(id);
        gameBoard.getCurrentCharacter().setCost(cost);

        if(noEntryTiles!=-1)
            gameBoard.getCurrentCharacter().setNoEntryTiles(noEntryTiles);
        if(students!=null)
            gameBoard.getCurrentCharacter().setStudents(students);

    }

    public CharacterChange(Character character, int index){ //index must be -1 for defaultCharacter
        id = character.getId();
        cost = character.getCost();
        noEntryTiles = character.getNoEntryTiles();
        students = character.getStudentsSlot();
        characterIndex = index;
    }
}
