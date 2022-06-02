package it.polimi.ingsw.modelChange;

import it.polimi.ingsw.client.model.ClientGameBoard;
import it.polimi.ingsw.client.model.ClientSchoolBoard;
import it.polimi.ingsw.server.model.*;

import java.util.HashMap;
import java.util.Map;

public class PlayerChange extends ModelChange{

    private String name;
    private ClientSchoolBoard schoolBoard;
    private int coins;
    private TowerColor towerColor;
    private AssistantType assistantType;
    private Assistant playedAssistant;
    private Assistant[] assistants;

    @Override
    public void execute(ClientGameBoard gameBoard){
        gameBoard.getPlayer(name).setSchoolBoard(schoolBoard);
        gameBoard.getPlayer(name).setCoins(coins);
        gameBoard.getPlayer(name).setTowerColor(towerColor);
        gameBoard.getPlayer(name).setAssistantType(assistantType);
        gameBoard.getPlayer(name).setPlayedAssistant(playedAssistant);
        gameBoard.getPlayer(name).setAssistants(assistants);
    }

    public PlayerChange(Player player){

        this.name = player.getName();

        //setting schoolBoard attribute
        ClientSchoolBoard clientSchoolBoard = new ClientSchoolBoard();

        clientSchoolBoard.setEntrance(player.getSchoolBoard().getStudentsInEntrance());
        clientSchoolBoard.setNumOfTowers(player.getSchoolBoard().getNumOfTowers());
        clientSchoolBoard.setTowersColor(player.getSchoolBoard().getTowersColor());

        Map<Color, Integer> diningRoom = new HashMap<>();
        for(Color c : Color.values()){
            diningRoom.put(c,player.getSchoolBoard().getNumOfStudentsInDining(c));
        }
        clientSchoolBoard.setDiningRoom(diningRoom);

        Map<Color, Integer> professors = new HashMap<>();
        for(Color c : Color.values()){
            professors.put(c,0);
        }
        for(Color c : player.getSchoolBoard().getProfessorsColor()){
            professors.put(c,1);
        }

        clientSchoolBoard.setProfessors(professors);

        //setting others player attributes
        this.coins = player.getCoins();
        this.towerColor = player.getTowerColor();
        this.assistantType = player.getAssistantType();
        this.playedAssistant = player.getPlayedAssistant();
        this.assistants = player.getAssistants();
    }
}
