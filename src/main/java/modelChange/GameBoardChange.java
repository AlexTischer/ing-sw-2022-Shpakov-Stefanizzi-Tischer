package modelChange;

import client.model.ClientGameBoard;
import server.model.*;
import server.model.Character;

import java.util.List;

public class GameBoardChange extends ModelChange{

    private List<Island> islands;
    private List<Cloud> clouds;
    private Character currentCharacter;
    private Character playedCharacters[];
    private AssistantDeck assistantDeck;
    private int positionOfMotherNature;
    private int numOfCoins;
    private Player currentPlayer;

    @Override
    public void execute(ClientGameBoard gameBoard){
        /*TODO gameBoard.setSomeThing(this.someThing)*/
    }

    public GameBoardChange(GameBoard gameBoard){
        /*TODO this.someThing=gameBoard.getSomeThing*/
    }
}
