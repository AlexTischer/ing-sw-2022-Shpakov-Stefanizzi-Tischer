package modelChange;

import client.model.ClientCloud;
import client.model.ClientGameBoard;
import client.model.ClientIsland;
import server.model.*;
import server.model.Character;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameBoardChange extends ModelChange{

    private List<ClientIsland> islands;
    private List<ClientCloud> clouds;
    private Character currentCharacter;
    private Character[] playedCharacters;
    private AssistantDeck assistantDeck;
    private int positionOfMotherNature;
    private int numOfCoins;
    private String currentPlayerName;

    @Override
    public void execute(ClientGameBoard gameBoard){
        gameBoard.setIslands(islands);
        gameBoard.setClouds(clouds);
        /* TODO fix setCharacter
        gameBoard.setCurrentCharacter(currentCharacter);
        gameBoard.setPlayedCharacters(playedCharacters);
        */
        gameBoard.setPositionOfMotherNature(positionOfMotherNature);
        gameBoard.setNumOfCoins(numOfCoins);
        gameBoard.setCurrentPlayerName(currentPlayerName);
    }

    public GameBoardChange(GameBoard gameBoard){

        //setting islands
        List<ClientIsland> clientIslands = new ArrayList<>();

        for(Island i : gameBoard.getIslands()){
            ClientIsland clientIsland = new ClientIsland();
            clientIsland.setNumOfIslands(i.getNumOfIslands());

            Map<Color,Integer> students = new HashMap<>();
            for(Color c : Color.values()){
                students.put(c,i.getNumOfStudents(c));
            }
            clientIsland.setStudents(students);

            clientIsland.setNumOfTowers(i.getNumOfTowers());
            clientIsland.setTowersColor(i.getTowersColor());
            clientIsland.setNoEntry(i.getNoEntry());
            clientIslands.add(clientIsland);
        }
        this.islands = clientIslands;

        //setting clouds
        List<ClientCloud> clientClouds = new ArrayList<ClientCloud>();

        for(Cloud c : gameBoard.getClouds()){
            ClientCloud clientCloud = new ClientCloud();
            clientCloud.setStudents(c.getStudentsColors());
            clientClouds.add(clientCloud);
        }
        this.clouds = clientClouds;

        //setting others attributes
        this.currentCharacter = gameBoard.getCurrentCharacter();
        this.playedCharacters = gameBoard.getPlayedCharacters();
        this.positionOfMotherNature = gameBoard.getPositionOfMotherNature();
        this.numOfCoins = gameBoard.getNumOfCoins();
        this.currentPlayerName = gameBoard.getCurrentPlayer().getName();
    }
}
