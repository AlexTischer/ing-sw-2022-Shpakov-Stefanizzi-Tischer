package client.model;

import server.model.Cloud;
import server.model.Island;
import server.model.Player;

import java.util.ArrayList;
import java.util.List;

public class ClientGameBoard {
    private List<Island> islands;
    private List<Cloud> clouds;
    private Character currentCharacter;
    private Character playedCharacters[];
    private int positionOfMotherNature;
    private int numOfCoins;
    private Player currentPlayer;
    private ArrayList<Player> players;

    public List<Island> getIslands() {
        return islands;
    }
    public void setIslands(List<Island> islands) {
        this.islands = islands;
    }

    public List<Cloud> getClouds() {
        return clouds;
    }

    public void setClouds(List<Cloud> clouds) {
        this.clouds = clouds;
    }

    public Character getCurrentCharacter() {
        return currentCharacter;
    }
    public void setCurrentCharacter(Character currentCharacter) {
        this.currentCharacter = currentCharacter;
    }

    public Character[] getPlayedCharacters() {
        return playedCharacters;
    }
    public void setPlayedCharacters(Character[] playedCharacters) {
        this.playedCharacters = playedCharacters;
    }

    public int getPositionOfMotherNature() {
        return positionOfMotherNature;
    }
    public void setPositionOfMotherNature(int positionOfMotherNature) {
        this.positionOfMotherNature = positionOfMotherNature;
    }

    public int getNumOfCoins() {
        return numOfCoins;
    }
    public void setNumOfCoins(int numOfCoins) {
        this.numOfCoins = numOfCoins;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayerNumber) {
        this.currentPlayer = players.get(0);} /*TODO*/

    public Player getPlayer(String playerName) {
        return players.get(0);
    } /*TODO*/

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }
}
