package modelChange;

import client.model.ClientCharacter;
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
    private int currentCharacter;
    private ClientCharacter[] playedCharacters = new ClientCharacter[3];
    private int positionOfMotherNature;
    private int numOfCoins;
    private String currentPlayerName;
    private ArrayList<PlayerChange> players;

    @Override
    public void execute(ClientGameBoard gameBoard){
        gameBoard.setIslands(islands);
        gameBoard.setClouds(clouds);

        gameBoard.setCurrentCharacter(currentCharacter);
        gameBoard.setPlayedCharacters(playedCharacters);

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

        //setting playedCharacters
        Character[] serverCharacters = gameBoard.getPlayedCharacters();

        for(int i=0; i<3; i++){
            playedCharacters[i].setCost(serverCharacters[i].getCost());

            if(serverCharacters[i].getNoEntryTiles()!=-1)
                playedCharacters[i].setNoEntryTiles(serverCharacters[i].getNoEntryTiles());

            if(serverCharacters[i].getStudentsSlot()!=null)
                playedCharacters[i].setStudents(serverCharacters[i].getStudentsSlot());
        }

        //TODO check if it's all right
        //setting currentCharacter
        int cnt = 0;
        for(int i=0;i<3;i++){
            if(serverCharacters[i].equals(gameBoard.getCurrentCharacter())) {
                this.currentCharacter = i;
                cnt = 1;
            }
        }
        if(cnt == 0)
            this.currentCharacter = -1; //-1 is the index of DefaultCharacter

        //setting others attributes
        this.positionOfMotherNature = gameBoard.getPositionOfMotherNature();
        this.numOfCoins = gameBoard.getNumOfCoins();
        this.currentPlayerName = gameBoard.getCurrentPlayer().getName();

    }
}
