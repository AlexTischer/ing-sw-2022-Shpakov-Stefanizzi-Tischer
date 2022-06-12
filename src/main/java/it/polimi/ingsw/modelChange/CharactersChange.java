package it.polimi.ingsw.modelChange;

import it.polimi.ingsw.client.model.ClientCharacter;
import it.polimi.ingsw.client.model.ClientGameBoard;
import it.polimi.ingsw.server.model.Character;

public class CharactersChange extends ModelChange{

    private ClientCharacter[] clientCharacters = new ClientCharacter[3];

    @Override
    public void execute(ClientGameBoard gameBoard){
        if (gameBoard.getAdvancedSettings())
            gameBoard.setPlayedCharacters(clientCharacters);
    }

    public CharactersChange(Character[] playedCharacters){

        for(int i=0; i<3; i++){

            ClientCharacter character = playedCharacters[i].createClientCharacter();

            clientCharacters[i] = character;
        }




    }
}
