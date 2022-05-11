package modelChange;

import client.model.ClientGameBoard;
import server.model.Character;

public class CharactersChange extends ModelChange{

    private Character[] playedCharacters;

    @Override
    public void execute(ClientGameBoard gameBoard){
        gameBoard.setPlayedCharacters(playedCharacters);
    }

    public CharactersChange(Character[] playedCharacters){
        this.playedCharacters=playedCharacters;
    }
}
