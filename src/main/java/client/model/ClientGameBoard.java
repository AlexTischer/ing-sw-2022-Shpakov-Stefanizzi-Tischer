package client.model;


import java.util.ArrayList;
import java.util.List;
import client.view.View;
public class ClientGameBoard {
    private View view;
    private List<ClientIsland> islands;
    private List<ClientCloud> clouds;
    private int currentCharacter;
    private ClientCharacter playedCharacters[];
    private int positionOfMotherNature;
    private int numOfCoins;
    private String currentPlayerName;
    private ArrayList<ClientPlayer> players;
    private String message;
    private List<String> userNames;
    private String clientName;

    /*Mike: inside each set method we need to update the view*/
    public List<ClientIsland> getIslands() {
        return islands;
    }

    public void setIslands(List<ClientIsland> islands) {
        this.islands = islands;
    }

    public List<ClientCloud> getClouds() {
        return clouds;
    }

    public ClientCloud getCloud(int numOfCloud) { return clouds.get(numOfCloud); }

    public void setClouds(List<ClientCloud> clouds) {
        this.clouds = clouds;
    }

    public ClientCharacter getCurrentCharacter() {
        return playedCharacters[currentCharacter];
    }

    public void setCurrentCharacter(int currentCharacter) {
        this.currentCharacter = currentCharacter;
    }

    public ClientCharacter[] getPlayedCharacters() {
        return playedCharacters;
    }

    public void setPlayedCharacters(ClientCharacter[] playedCharacters) {
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

    public String getCurrentPlayerName() {
        return currentPlayerName;
    }

    public void setCurrentPlayerName(String currentPlayerName) {
        this.currentPlayerName = currentPlayerName;
        if(currentPlayerName.equals(clientName)){
            if(getPlayer(currentPlayerName).getPlayedAssistant()==null){
                view.pianificationPhase();
            }
            else {
                view.actionPhase();
            }
        }
    }

    public ClientPlayer getPlayer(String playerName) {
        for(ClientPlayer p : players){
            if(p.getName().equals(playerName)){
                return p;
            }
        }
        throw new IllegalArgumentException();
    }

    public void setPlayers(ArrayList<ClientPlayer> players) {
        this.players = players;
    }

    public ArrayList<ClientPlayer> getPlayers() {
        return players;
    }

    public void setUserNames(List<String> userNames) {
        this.userNames = userNames;
        System.out.println(userNames.toString());
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void attachView(View view){
        this.view = view;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
