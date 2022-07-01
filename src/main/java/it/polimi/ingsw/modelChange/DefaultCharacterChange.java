package it.polimi.ingsw.modelChange;

import it.polimi.ingsw.client.model.ClientGameBoard;
import it.polimi.ingsw.server.model.Color;

public class DefaultCharacterChange extends ModelChange{

    @Override
    public void execute(ClientGameBoard gameBoard){
        gameBoard.setCurrentCharacter(-1);
    }

    public DefaultCharacterChange(){ //index must be -1 for defaultCharacter

    }
}
