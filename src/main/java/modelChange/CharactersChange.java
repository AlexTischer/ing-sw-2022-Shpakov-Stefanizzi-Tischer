package modelChange;

import client.model.ClientCharacter;
import client.model.ClientGameBoard;
import server.model.Character;

public class CharactersChange extends ModelChange{

    private ClientCharacter[] clientCharacters = new ClientCharacter[3];

    @Override
    public void execute(ClientGameBoard gameBoard){
        gameBoard.setPlayedCharacters(clientCharacters);
    }

    public CharactersChange(Character[] playedCharacters){

        for(int i=0; i<3; i++){
            clientCharacters[i].setCost(playedCharacters[i].getCost());

            if(playedCharacters[i].getNoEntryTiles()!=-1)
                clientCharacters[i].setNoEntryTiles(playedCharacters[i].getNoEntryTiles());

            if(playedCharacters[i].getStudentsSlot()!=null)
                clientCharacters[i].setStudents(playedCharacters[i].getStudentsSlot());
        }
    }
}
