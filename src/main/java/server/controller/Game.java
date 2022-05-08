package server.controller;

import packets.Packet;
import server.model.*;
import server.model.Character;
import exceptions.NoEntryException;
import exceptions.RepeatedAssistantRankException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class Game implements GameForClient{
    private static Game instanceOfGame;
    private GameBoard gameBoard;
    private ArrayList<Player> players;
    private boolean advancedSettings;
    private Game(){}
    private int studentMove;
    private boolean motherNatureMove;
    private boolean useCloudMove;

    public static Game getInstanceOfGame() {
        if(instanceOfGame==null){
            instanceOfGame = new Game();
        }
        return instanceOfGame;
    }

    public void init(ArrayList<String> playersNames, boolean advancedSettings, CharacterDeck characterDeck){
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

        /*refill assistants of player*/
        for(Player p: players){
            gameBoard.refillAssistants(p);
            gameBoard.refillEntrance(p);
        }

        studentMove = 0;
        motherNatureMove = false;
        useCloudMove = false;

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
        if (studentMove > (players.size() == 3? 4: 3))
            throw new RuntimeException();

        gameBoard.moveStudentToIsland(gameBoard.getCurrentPlayer(), studentColor, islandNumber);
        studentMove++;
    }

    public void moveStudentToDining(Color studentColor){
        if (studentMove > (players.size() == 3? 4: 3))
            throw new RuntimeException();

        removeStudentFromEntrance(studentColor);
        addStudentToDining(gameBoard.getCurrentPlayer(), studentColor);
        studentMove++;
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

    public void reassignIsland(int islandNumber){ //TODO 4 Players game
        Player master = players.get(0);
        Player loser = players.get(0);
        int masterInfluence = 0;
        // Looking for player with highest influence
        for (Player p : players){
            if(calculateInfluence(islandNumber, p)>masterInfluence ||
                    (calculateInfluence(islandNumber, p)==masterInfluence &&
                            p.getTowerColor().equals(gameBoard.getTowersColorOnIsland(islandNumber))))
            {master=p;}
        }
        for (Player p : players){
            if(p!=master && masterInfluence==calculateInfluence(islandNumber, p)){
                return;
            }
        }
        if(master.getNumOfTowers()>=gameBoard.getNumOfMergedIslands(islandNumber)) {
            // Looking for player to return towers to if there were towers on island
            if (gameBoard.getNumOfTowersOnIsland(islandNumber) > 0) {
                for (Player p : players) {
                    if (p.getTowerColor() == gameBoard.getTowersColorOnIsland(islandNumber) && p.getNumOfTowers() > 0) {
                        loser = p;
                    }
                }
                if (master != loser) {
                    gameBoard.addTowersToPlayer(gameBoard.getNumOfTowersOnIsland(islandNumber), loser);
                }
            }
            if (master != loser) {
                gameBoard.addTowersToIsland(islandNumber, master);
                gameBoard.removeTowersFromPlayer(islandNumber, master);
            }
        }
    }

    /*calculates influence score on specified Island*/ //TODO review
    public int calculateInfluence(int islandNumber, Player player){
        int influence = -1; //return -1 when NoEntry Tile is on selected island
        try {
            influence = gameBoard.calculateInfluence(islandNumber, player);
        }
        catch (NoEntryException e){
            /*returns noEntry to the character card*/
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
        if (studentMove != (players.size() == 3? 4: 3) || motherNatureMove)
            throw new RuntimeException();

        gameBoard.moveMotherNature(steps);
        motherNatureMove = true;
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
        if (studentMove != (players.size() == 3? 4: 3) || !motherNatureMove)
            throw new RuntimeException();

        gameBoard.useCloud(cloudNumber);
        useCloudMove = true;
    }

    public synchronized void useAssistant(int assistantRank){
        if(checkAssistant(assistantRank, gameBoard.getCurrentPlayer())){
            gameBoard.getCurrentPlayer().setPlayedAssistantRank(assistantRank);
            notifyAll();
        }else throw new RepeatedAssistantRankException();
        gameBoard.setCurrentPlayer(players.get((players.indexOf(gameBoard.getCurrentPlayer())+1)%players.size()));
    }

    private void playGame() throws InterruptedException {
        while(!checkEndGame()){
            newRound();
        }
    }


    /*TODO need to control newRound(), i suppose there is a bug*/
    private synchronized void newRound() throws InterruptedException {
        gameBoard.refillClouds();
        gameBoard.setCurrentPlayer(players.get(0));

        for (Player p : players){
            while(p.getPlayedAssistant()==null){
                wait();
            }
        }

        Collections.sort(players);

        for(Player p : players){
            if(!checkEndGame()){
                newTurn(p);
            }
        }

        for (Player p : players){
            p.setPlayedAssistantRank(0);
        }
    }

    private void newTurn(Player p) throws InterruptedException {
        /*virtual view controls current player before forwarding any method to controller*/
        gameBoard.setCurrentPlayer(p);

        while (studentMove != (players.size() == 3? 4: 3) || !motherNatureMove || !useCloudMove);

        studentMove = 0;
        motherNatureMove = false;
        useCloudMove = false;

    }

    /*check whether a player can play an assistant with a certain rank*/
    private boolean checkAssistant(int assistantRank, Player player){
        Set<Integer> playedAssistantsRanks = players.stream().filter(p -> p!=player && p.getPlayedAssistant()!=null).
                map(p -> p.getPlayedAssistant().getRank()).collect(Collectors.toSet());

        /*if a player wants to play rank not contained, then return false*/
        if (!player.getAssistantsRanks().contains(assistantRank))
            return false;

        /*if a player decides to play an assistant with rank already played by smbd else,
        it is allowable only when player has no other option*/
        if (playedAssistantsRanks.contains(assistantRank)){
            for (int rank: player.getAssistantsRanks()) {
                /*if player has an assistant with rank not yet played*/
                if (!playedAssistantsRanks.contains(rank))
                    return false;
            }
        }

        return true;
    }

    private boolean checkEndGame(){
        for(Player p : players){
            if(p.getAssistantsRanks().isEmpty()){
                return true;
            }
        }
        for (Player p : players){
            if(p.checkEmptyTowers()){
                if(players.size()>3){
                    for (Player q : players){
                        if (!p.equals(q) && p.getTowerColor()==q.getTowerColor() && q.checkEmptyTowers())
                            return true;
                    }
                } else return true;
            }
        }
        return gameBoard.checkBagEmpty() || gameBoard.getNumOfIslands()<=3;
    }

    public void update(Packet packet){
        packet.execute(this);
    }

    /*TEST METHODS*/

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public void addStudentToEntrance(Player player){
        gameBoard.addStudentToEntrance(player, getStudent());
    }

    public void addStudentToEntrance(Player player, Color studentColor){
        gameBoard.addStudentToEntrance(player, studentColor);
    }

}