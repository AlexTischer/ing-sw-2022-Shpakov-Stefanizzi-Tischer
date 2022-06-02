package it.polimi.ingsw.modelChange;

import it.polimi.ingsw.client.model.ClientCharacter;
import it.polimi.ingsw.client.model.ClientGameBoard;
import it.polimi.ingsw.server.model.Character;

public class CharactersChange extends ModelChange{

    private ClientCharacter[] clientCharacters = new ClientCharacter[3];

    @Override
    public void execute(ClientGameBoard gameBoard){
        gameBoard.setPlayedCharacters(clientCharacters);
    }

    public CharactersChange(Character[] playedCharacters){

        for(int i=0; i<3; i++){
            ClientCharacter character = new ClientCharacter();
            character.setCost(playedCharacters[i].getCost());
            character.setId(playedCharacters[i].getCost());

            if(playedCharacters[i].getNoEntryTiles()!=-1)
                character.setNoEntryTiles(playedCharacters[i].getNoEntryTiles());

            if(playedCharacters[i].getStudentsSlot()!=null)
                character.setStudents(playedCharacters[i].getStudentsSlot());

            clientCharacters[i] = character;
        }
    }
}
