package modelChange;

import client.model.ClientGameBoard;
import client.model.ClientPlayer;
import client.model.ClientSchoolBoard;
import server.model.Color;
import server.model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlayersChange extends ModelChange{
    private ArrayList<ClientPlayer> clientPlayers;

    @Override
    public void execute(ClientGameBoard gameBoard){
        gameBoard.setPlayers(clientPlayers);
    }

    public PlayersChange(ArrayList<Player> players){

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

            clientPlayers.add(clientPlayer);
        }
    }
}