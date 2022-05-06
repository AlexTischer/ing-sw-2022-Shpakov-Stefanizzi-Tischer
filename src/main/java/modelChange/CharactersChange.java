package modelChange;

import client.model.ClientGameBoard;

public class CharactersChange extends ModelChange{

    private Character[] playedCharacters;

    @Override
    public void execute(ClientGameBoard gameBoard){
        gameBoard.setPlayedCharacters(playedCharacters);
    }

    public CharactersChange(java.lang.Character[] playedCharacters){
        this.playedCharacters=playedCharacters;
    }
}
