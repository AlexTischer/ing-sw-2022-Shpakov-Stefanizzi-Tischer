package modelChange;

import client.model.ClientGameBoard;
import server.model.Character;

public class CharacterChange extends ModelChange{

    private Character currentCharacter;

    @Override
    public void execute(ClientGameBoard gameBoard){
        gameBoard.setCurrentCharacter(currentCharacter);
    }

    public CharacterChange(Character currentCharacter){
        this.currentCharacter=currentCharacter;
    }
}
