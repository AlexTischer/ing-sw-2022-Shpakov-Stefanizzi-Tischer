package controller;

import exceptions.NoEntryException;
import exceptions.RepeatedAssistantRankException;
import model.*;
import model.Character;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;

public class Game implements GameForClient{
    private static Game instanceOfGame;
    private GameBoard gameBoard;
    private ArrayList<Player> players;
    private boolean advancedSettings;
    private Game(){}

    public static Game getInstanceOfGame() {
        if(instanceOfGame==null){
            instanceOfGame = new Game();
        }
        return instanceOfGame;
    }

    public void init(ArrayList<String> playersNames, boolean advancedSettings, AssistantDeck assistantDeck, CharacterDeck characterDeck){
        players = new ArrayList<Player>();
        switch (playersNames.size()){
            case 2:
                this.players.add(new Player(playersNames.get(0), TowerColor.WHITE, AssistantType.ONE, 8));
                this.players.add(new Player(playersNames.get(1), TowerColor.BLACK, AssistantType.TWO, 8));
                break;
            case 3:
                this.players.add(new Player(playersNames.get(0), TowerColor.WHITE, AssistantType.ONE, 6));
                this.players.add(new Player(playersNames.get(1), TowerColor.BLACK, AssistantType.TWO, 6));
                this.players.add(new Player(playersNames.get(2), TowerColor.GREY, AssistantType.THREE, 6));
                break;
            case 4:
                this.players.add(new Player(playersNames.get(0), TowerColor.WHITE, AssistantType.ONE, 8));
                this.players.add(new Player(playersNames.get(1), TowerColor.BLACK, AssistantType.TWO, 8));
                this.players.add(new Player(playersNames.get(2), TowerColor.WHITE, AssistantType.THREE, 0));
                this.players.add(new Player(playersNames.get(3), TowerColor.BLACK, AssistantType.FOUR, 0));
                break;
            default:
                throw new InvalidParameterException();
        }
        gameBoard = GameBoard.getInstanceOfGameBoard();
        gameBoard.init(playersNames.size());

        this.advancedSettings=advancedSettings;
        if (advancedSettings) {
            for (int i = 0; i < 3; i++) {
                Character c = characterDeck.popCharacter();
                c.initialFill(this);
                gameBoard.setPlayedCharacters(i, c);
            }
        }

        gameBoard.setCurrentCharacter(new Character());
        gameBoard.getCurrentCharacter().initialFill(this);

        new Thread(() -> {
            try {
                playGame();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public Player getCurrentPlayer(){
        return gameBoard.getCurrentPlayer();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void moveStudentToIsland(Color studentColor, int islandNumber){
        gameBoard.moveStudentToIsland(gameBoard.getCurrentPlayer(), studentColor, islandNumber);
    }

    public void moveStudentToDining(Color studentColor){
        removeStudentFromEntrance(studentColor);
        addStudentToDining(gameBoard.getCurrentPlayer(), studentColor);
    }

    public void addStudentToEntrance(Player player){
        gameBoard.addStudentToEntrance(player, getStudent());
    }

    public void addStudentToEntrance(Player player, Color studentColor){
        gameBoard.addStudentToEntrance(player, studentColor);
    }

    public void addStudentToDining(Player player, Color studentColor){
        gameBoard.addStudentToDining(player, studentColor);
        reassignProfessor();
    }

    public void addStudentToIsland(Color studentColor, int islandNumber){
        gameBoard.addStudentToIsland(studentColor, islandNumber);
    }

    public void removeStudentFromEntrance(Color studentColor){
        gameBoard.removeStudentFromEntrance(gameBoard.getCurrentPlayer(), studentColor);
    }

    public void removeStudentFromDining(Player player, Color studentColor){
        gameBoard.removeStudentFromDining(player, studentColor);
    }

    public Color getStudent(){
        return gameBoard.getStudentFromBag();
    }

    public int calculateInfluence(int islandNumber){
        int influence = -1;
        try {
            influence = gameBoard.calculateInfluence(islandNumber);
        }
        catch (NoEntryException e){
            gameBoard.addNoEntryTile();
        }
        return influence;
    }

    public void reassignProfessor(){
        gameBoard.getCurrentCharacter().reassignProfessor();
    }

    public void setNoEntry(int islandNumber, boolean noEntry){
        try {
            gameBoard.setNoEntry(islandNumber, noEntry);
        }

        catch (NoEntryException e){
            }
    }

    public void moveMotherNature(int steps){
        gameBoard.moveMotherNature(steps);
    }

    public void buyCharacter(int characterNumber){
            gameBoard.buyCharacter(characterNumber);
    }

    public void activateCharacter(int islandNumber){
        gameBoard.activateCharacter(islandNumber);
    }

    public void activateCharacter(ArrayList<Color> toBeSwappedStudents, ArrayList<Color> selectedStudents){
        gameBoard.activateCharacter(toBeSwappedStudents, selectedStudents);
    }

    public void activateCharacter(Color color, int islandNumber){
        gameBoard.activateCharacter(color, islandNumber);
    }

    public void activateCharacter(Color color){
        gameBoard.activateCharacter(color);
    }

    public void useCloud(int cloudNumber){
        gameBoard.useCloud(cloudNumber);
    }

    public void useAssistant(int assistantRank, Player player){
        if(true){
            player.setPlayedAssistantRank(assistantRank);
            notifyAll();
        }else throw new RepeatedAssistantRankException();
    }

    private void playGame() throws InterruptedException {
        while(!checkEndGame()){
            newRound();
        }
    }

    private void newRound() throws InterruptedException {
        gameBoard.refillClouds();
        for (Player p : players){
            while(p.getPlayedAssistant()==null){
                wait();
            }
        }

        Collections.sort(players); /*If two ranks are the same, the second selected plays second!!*/

        for(Player p : players){
            if(!checkEndGame()){
                newTurn(p);
            }
        }

        for (Player p : players){
            p.setPlayedAssistantRank(0);
        }
    }

    private boolean checkEndGame(){
        return false;
    }

    private void newTurn(Player p) throws InterruptedException {
        gameBoard.setCurrentPlayer(p);
        wait();
    }

    /*TEST METHODS*/

    public GameBoard getGameBoard() {
        return gameBoard;
    }
}