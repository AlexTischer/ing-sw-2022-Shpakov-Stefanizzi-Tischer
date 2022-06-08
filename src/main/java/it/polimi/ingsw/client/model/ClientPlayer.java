package it.polimi.ingsw.client.model;

import it.polimi.ingsw.server.model.Assistant;
import it.polimi.ingsw.server.model.AssistantType;
import it.polimi.ingsw.server.model.TowerColor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class ClientPlayer implements Serializable {

    private String name;
    private ClientSchoolBoard schoolBoard;
    private int coins;
    private TowerColor towerColor;
    private AssistantType assistantType;
    private Assistant playedAssistant;
    private Assistant[] assistants;
    private boolean connectionStatus;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClientSchoolBoard getSchoolBoard() {
        return schoolBoard;
    }

    public void setSchoolBoard(ClientSchoolBoard schoolBoard) {
        this.schoolBoard = schoolBoard;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public TowerColor getTowerColor() {
        return towerColor;
    }

    public void setTowerColor(TowerColor towerColor) {
        this.towerColor = towerColor;
    }

    public AssistantType getAssistantType() {
        return assistantType;
    }

    public void setAssistantType(AssistantType assistantType) {
        this.assistantType = assistantType;
    }

    public Assistant getPlayedAssistant() {
        return playedAssistant;
    }

    public void setPlayedAssistant(Assistant playedAssistant) {
        this.playedAssistant = playedAssistant;
        if(playedAssistant!=null) {
            for (int i = 0; i < 9; i++) {
                if(assistants[i]!=null) {
                    if (assistants[i].getRank() == (playedAssistant).getRank()) {
                        assistants[i] = null;
                    }
                }
            }
        }
    }

    public Assistant[] getAssistants() {
        return assistants;
    }

    public void setAssistants(Assistant[] assistants) {
        this.assistants = assistants;
    }

    public void setConnectionStatus(boolean connectionStatus) {
        //TODO show somehow to model that this player is not active
        this.connectionStatus = connectionStatus;
        System.out.println("Player " + name + " changed it's status to " + connectionStatus);
    }

    public List<Integer> getAssistantsRanks(){
        return Arrays.stream(assistants).filter(a -> a!=null).map(a -> a.getRank()).toList();
    }
}
