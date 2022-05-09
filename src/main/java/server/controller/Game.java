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

    public void reassignIsland(int islandNumber){
        /*no players with 0 towers allowed except in case of 4 player game*/
        Player master = players.get(0);
        Player loser = players.get(0);

        int masterInfluence = 0;
        int influenceToCompare = 0;

        /*if an island has noEntry tile on it, then the island does not get conquered*/
        if (calculateInfluence(islandNumber, master) == -1)
            return;

        // Looking for a player ( or team ) with the highest influence
        // Iterate through players that hold towers
        for (Player p : players.stream().filter((Player pl) -> pl.getNumOfTowers() > 0).toList()){
            influenceToCompare = calculateInfluence(islandNumber, p);

            // Iterate through players that does not hold any towers
            for (Player q : players.stream().filter((Player pl) -> pl.getNumOfTowers() == 0).toList()){
                //if players have the same tower color then they are from
                //the same team and thus their influence should be added
                if (p.getTowerColor().equals(q.getTowerColor()))
                    influenceToCompare += calculateInfluence(islandNumber, q);
            }


            if(influenceToCompare > masterInfluence || (influenceToCompare == masterInfluence &&
                    p.getTowerColor().equals(gameBoard.getTowersColorOnIsland(islandNumber)))) {
                /*assign to master the player from the team that holds towers*/
                master = p;
                masterInfluence = influenceToCompare;
            }
        }

        /*if there are 2 players ( or 2 teams ) with the same maximum influence score then nobody conquers an island*/
        for (Player p : players.stream().filter((Player pl) -> pl.getNumOfTowers() > 0).toList()){

            if (!p.getTowerColor().equals(master.getTowerColor())){
                influenceToCompare = calculateInfluence(islandNumber, p);

                for (Player q : players.stream().filter((Player pl) -> pl.getNumOfTowers() == 0).toList()){
                    if (p.getTowerColor().equals(q.getTowerColor()))
                        influenceToCompare += calculateInfluence(islandNumber, q);
                }

                /*found a player or a team that has the same influence score*/
                if (masterInfluence == influenceToCompare)
                    return;
            }
        }

        /*the code below works in case of teams as well*/

        //Search for the player to take his towers back
        //check whether an island has towers
        if (gameBoard.getNumOfTowersOnIsland(islandNumber) > 0) {

            /*return towers to the player that holds them for the entire team*/
            for (Player p : players.stream().filter((Player pl) -> pl.getNumOfTowers() > 0).toList()) {
                if (p.getTowerColor().equals(gameBoard.getTowersColorOnIsland(islandNumber)))
                    loser = p;

            }
            /*to avoid useless model change, make a check*/
            if (master != loser)
                gameBoard.addTowersToPlayer(gameBoard.getNumOfTowersOnIsland(islandNumber), loser);

        }

        /*to avoid useless model change, make a check*/
        if (master != loser) {
            gameBoard.addTowersToIsland(islandNumber, master);
            //suppose that the case with the master that has 0 towers is impossible
            //if a player has no enough towers then he puts all towers that he owns and as a result becomes a winner
            //otherwise he puts as many towers as islands inside the island that he wants to conquer
            gameBoard.removeTowersFromPlayer(Math.min(master.getNumOfTowers(), gameBoard.getNumOfMergedIslands(islandNumber)), master);
        }

    }

    /*calculates influence score on specified Island*/
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
        /*set the next player to chose assistant card*/
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

        /*sorts players based on rank, from lowest to highest so that the next turn
        is started by player that played the card with the lowest rank*/
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

        /*if a player wants to play rank not contained in his hand, then return false*/
        if (!player.getAssistantsRanks().contains(assistantRank))
            return false;

        /*if a player decides to play an assistant with rank already played by someone,
        it is allowable only when player has no other options*/
        if (playedAssistantsRanks.contains(assistantRank)){
            for (int rank: player.getAssistantsRanks()) {
                /*if player has an assistant with rank not yet played*/
                if (!playedAssistantsRanks.contains(rank))
                    return false;
            }
        }

        return true;
    }

    /*the game is finished when there is a player that has no assistants or
    has no towers or there is a team that has no towers (in case of 4 players) or
    the bag is empty or the number of islands is less than 3*/
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

    /*the packet received executes certain methods on Game*/
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