package modelChange;

import client.model.*;
import server.model.*;
import server.model.Character;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameBoardChange extends ModelChange{

    private ArrayList<ClientIsland> islands = new ArrayList<ClientIsland>();
    private ArrayList<ClientCloud> clouds = new ArrayList<ClientCloud>();
    private int currentCharacter;
    private ClientCharacter[] playedCharacters = new ClientCharacter[3];
    private int positionOfMotherNature;
    private int numOfCoins;
    private String currentPlayerName;
    private ArrayList<ClientPlayer> clientPlayers = new ArrayList<ClientPlayer>();

    @Override
    public void execute(ClientGameBoard gameBoard){
        gameBoard.setIslands(islands);
        gameBoard.setClouds(clouds);

        gameBoard.setCurrentCharacter(currentCharacter);
        gameBoard.setPlayedCharacters(playedCharacters);

        gameBoard.setPositionOfMotherNature(positionOfMotherNature);
        gameBoard.setNumOfCoins(numOfCoins);

        //players has to be set on ClientGameBoard always with the same order, whilst on server the arraylist gets reordered round by round (based on played Assistant rank).

        if (gameBoard.getPlayers()!=null) {
            ArrayList<ClientPlayer> tempPlayers = new ArrayList<ClientPlayer>();
            for (ClientPlayer p : gameBoard.getPlayers()) {
                for (ClientPlayer q : clientPlayers) {
                    if (p.getName().equals(q.getName())) {
                        tempPlayers.add(q);
                        break;
                    }
                }
            }

            gameBoard.setPlayers(tempPlayers);
        }
        else {
            gameBoard.setPlayers(clientPlayers);
        }

        //tells that game is on
        gameBoard.setGameOn(true);
        gameBoard.setCurrentPlayerName(currentPlayerName);
    }

    public GameBoardChange(GameBoard gameBoard, ArrayList<Player> players){

        //setting islands
        ArrayList<ClientIsland> clientIslands = new ArrayList<>();

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
        ArrayList<ClientCloud> clientClouds = new ArrayList<ClientCloud>();

        for(Cloud c : gameBoard.getClouds()){
            ClientCloud clientCloud = new ClientCloud();
            clientCloud.setStudents(c.getStudentsColors());
            clientClouds.add(clientCloud);
        }
        this.clouds = clientClouds;

        //setting playedCharacters
        Character[] serverCharacters = gameBoard.getPlayedCharacters();
        //TODO: if advanced settings are not selected, serverCharacters[i] is null

        for(int i=0; i<3; i++){
            ClientCharacter character = new ClientCharacter();

            character.setCost(serverCharacters[i].getCost());
            character.setId(serverCharacters[i].getId());

            if(serverCharacters[i].getNoEntryTiles()!=-1)
                character.setNoEntryTiles(serverCharacters[i].getNoEntryTiles());


            character.setStudents(serverCharacters[i].getStudentsSlot());

            playedCharacters[i] = character;
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

        for(Player p : players){

            ClientPlayer clientPlayer = new ClientPlayer();

            /*setting ClientSchoolBoard*/
            ClientSchoolBoard clientSchoolBoard = new ClientSchoolBoard();

            //entrance
            clientSchoolBoard.setEntrance(p.getSchoolBoard().getStudentsInEntrance());

            //diningRoom
            Map<Color, Integer> diningRoom = new HashMap<>();
            for(Color c : Color.values()){
                diningRoom.put(c,p.getSchoolBoard().getNumOfStudentsInDining(c));
            }
            clientSchoolBoard.setDiningRoom(diningRoom);

            //professors
            Map<Color, Integer> professors = new HashMap<>();
            for(Color c : Color.values()){
                professors.put(c,0);
            }
            for(Color c : p.getSchoolBoard().getProfessorsColor()){
                professors.put(c,1);
            }

            clientSchoolBoard.setProfessors(professors);

            //numOfTowers
            clientSchoolBoard.setNumOfTowers(p.getSchoolBoard().getNumOfTowers());

            //towersColor
            clientSchoolBoard.setTowersColor(p.getSchoolBoard().getTowersColor());


            /*setting other player attributes*/
            clientPlayer.setName(p.getName());
            clientPlayer.setCoins(p.getCoins());
            clientPlayer.setTowerColor(p.getTowerColor());
            clientPlayer.setAssistantType(p.getAssistantType());
            clientPlayer.setPlayedAssistant(p.getPlayedAssistant());
            clientPlayer.setAssistants(p.getAssistants());
            clientPlayer.setSchoolBoard(clientSchoolBoard);

            clientPlayers.add(clientPlayer);
        }
    }
}
