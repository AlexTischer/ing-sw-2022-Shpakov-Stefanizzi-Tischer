package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.exceptions.EndOfChangesException;
import it.polimi.ingsw.exceptions.WrongActionException;
import it.polimi.ingsw.modelChange.EndOfGameChange;
import it.polimi.ingsw.modelChange.ExceptionChange;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.packets.Packet;
import it.polimi.ingsw.server.model.Character;
import it.polimi.ingsw.exceptions.NoEntryException;
import it.polimi.ingsw.exceptions.RepeatedAssistantRankException;

import java.security.InvalidParameterException;
import java.util.*;
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

    public void init(List<String> playersNames, boolean advancedSettings, CharacterDeck characterDeck){
        players = new ArrayList<Player>();
        switch (playersNames.size()) {
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
        gameBoard.init(this, playersNames.size());

        this.advancedSettings = advancedSettings;
        if (advancedSettings) {
            Character[] playedCharacters = new Character[3];
            for (int i = 0; i < 3; i++) {
                playedCharacters[i] = characterDeck.popCharacter();
                playedCharacters[i].initialFill(this);
                gameBoard.setPlayedCharacters(playedCharacters);
            }
        }

        gameBoard.setCurrentCharacterToDefault(new Character());
        gameBoard.getCurrentCharacter().initialFill(this);

        /*refill assistants of players and distribute 1 coin in case of adv settings*/
        for (Player p : players) {
            gameBoard.refillAssistants(p);
            gameBoard.refillEntrance(p);
            if (advancedSettings)
                p.addCoins(1);
        }

        Collections.shuffle(players);
        gameBoard.setCurrentPlayer(players.get(0));
    }

    public void launchGame(){
        studentMove = 0;
        motherNatureMove = false;
        useCloudMove = false;

        //create separate thread that will wait until all clients insert assistants and execute action phase
        //it will execute until game ends
        new Thread(() -> {
            try {
                playGame();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public Player getCurrentPlayer(){
        return gameBoard.getCurrentPlayer();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public synchronized void moveStudentToIsland(Color studentColor, int islandNumber){
        if (studentMove > (players.size() == 3? 4: 3))
            throw new WrongActionException();

        gameBoard.addStudentToIsland(gameBoard.getCurrentPlayer(), studentColor, islandNumber);
        studentMove++;
        this.notifyAll();
    }

    public synchronized void moveStudentToDining(Color studentColor){
        if (studentMove > (players.size() == 3? 4: 3))
            throw new WrongActionException();

        removeStudentFromEntrance(studentColor);
        addStudentToDining(gameBoard.getCurrentPlayer(), studentColor);
        studentMove++;
        this.notifyAll();
    }

    public void addStudentToDining(Player player, Color studentColor){
        gameBoard.addStudentToDining(player, studentColor);

        /*if this is advanced version of game, I pay 1 coin to player for each 3rd student*/
        if ( advancedSettings && player.getNumOfStudentsInDining(studentColor) % 3==0 ){
            gameBoard.addCoins(player, 1);
        }
        //check if professor gets reassigned
        reassignProfessor();
        gameBoard.notify(new ExceptionChange(new EndOfChangesException()));
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

    //method that reassigns island and puts certain tower on it
    public void reassignIsland(int islandNumber){
        /*no players with 0 towers allowed except in case of 4 player game*/
        Player master = players.get(0);
        Player loser = players.get(0);

        int masterInfluence = 0;
        int influenceToCompare;

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

                // Iterate through players that does not hold any towers
                for (Player q : players.stream().filter((Player pl) -> pl.getNumOfTowers() == 0).toList()){
                    //if players have the same tower color then they are from
                    //the same team and thus their influence should be added
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
            if (!master.equals(loser))
                gameBoard.addTowersToPlayer(gameBoard.getNumOfTowersOnIsland(islandNumber), loser);
        }
        else{
            loser = null;
        }

        /*to avoid useless model change, make a check*/
        if (!master.equals(loser)) {
            gameBoard.addTowersToIsland(islandNumber, master);
            //suppose that the case with the master that has 0 towers is impossible
            //if a player has no enough towers then he puts all towers that he owns and as a result becomes a winner
            //otherwise he puts as many towers as islands inside the island that he wants to conquer
            gameBoard.removeTowersFromPlayer(Math.min(master.getNumOfTowers(), gameBoard.getNumOfMergedIslands(islandNumber)), master);
            gameBoard.mergeIslands();
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
        gameBoard.setNoEntry(islandNumber, noEntry);
    }

    public synchronized void moveMotherNature(int steps){
        if (studentMove != (players.size() == 3? 4: 3) || motherNatureMove) {
            //client can't make this action if students weren't moved or if mother nature was already moved
            throw new WrongActionException();
        }

        gameBoard.moveMotherNature(steps);
        //each time a client moves MN, I need to try to resolve an island because it might be conquered
        reassignIsland(gameBoard.getPositionOfMotherNature());

        //make a control because MN move might have finished the game
        gameBoard.setGameOn(!checkEndGame());
        if(gameBoard.isGameOn()) {
            //let client know that he can move to the next step
            //send endOfChanges only if game was not finished
            ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
            gameBoard.notify(exceptionChange);
        }
        motherNatureMove = true;
        this.notifyAll();
    }

    public void buyCharacter(int characterNumber){
        if (!advancedSettings)
            throw new UnsupportedOperationException("You can't use characters. Advanced settings were set to false");

        gameBoard.buyCharacter(characterNumber);
    }

    public void activateCharacter(int islandNumber){
        if (!advancedSettings)
            throw new UnsupportedOperationException("You can't use characters. Advanced settings were set to false");

        gameBoard.activateCharacter(islandNumber);
    }

    public void activateCharacter(ArrayList<Color> toBeSwappedStudents, ArrayList<Color> selectedStudents){
        if (!advancedSettings)
            throw new UnsupportedOperationException("You can't use characters. Advanced settings were set to false");

        gameBoard.activateCharacter(toBeSwappedStudents, selectedStudents);
    }

    public void activateCharacter(Color color, int islandNumber){
        if (!advancedSettings)
            throw new UnsupportedOperationException("You can't use characters. Advanced settings were set to false");

        gameBoard.activateCharacter(color, islandNumber);
    }

    public void activateCharacter(Color color){
        if (!advancedSettings)
            throw new UnsupportedOperationException("You can't use characters. Advanced settings were set to false");

        gameBoard.activateCharacter(color);
    }

    public synchronized void useCloud(int cloudNumber){
        if (studentMove != (players.size() == 3? 4: 3) || !motherNatureMove || useCloudMove) {
            //client can't make this action if students weren't moved
            //or if mother nature wasn't already moved or cloud was already used
            throw new WrongActionException();
        }
        gameBoard.useCloud(cloudNumber);
        useCloudMove = true;
        this.notifyAll();
    }

    //sets assistant of current player and makes the next player current to let him call use assistant from virtual view
    public synchronized void useAssistant(int assistantRank){

        //checks if player can use this assistant
        if (checkAssistant(assistantRank, gameBoard.getCurrentPlayer())) {
            //sends 1 model change
            gameBoard.setPlayedAssistantRank(assistantRank, gameBoard.getCurrentPlayer());
        }
        else
            throw new RepeatedAssistantRankException();

        //notifies a waiting thread in newRound
        this.notifyAll();
    }

    private synchronized void playGame() throws InterruptedException {
        //let the game start
        gameBoard.setGameOn(true);

        //wake up server thread that waits for refill clouds and set currentPlayer to happen
        //only after that server thread will attach virtual views to gameBoard and send gameBoard change
        //this will happen after current thread enters in wait
        this.notifyAll();

        while(gameBoard.isGameOn()){
            newRound();
        }

        //TODO manage end of game
        //GameBoard might send endOfGameChange
        System.out.println("Game says: The game was finished !");
    }


    private void newRound() throws InterruptedException {
        //if player is not active then skip him until next planning phase
        //in planning phase check isActive variable , in action phase it is enough to check playedAssistant variable


        //checkEndGame() is called inside refillClouds() since this is the action that might finish the game
        gameBoard.refillClouds();

        /*planning phase*/
        //players are sorted in order in which they should play assistant card
        if (gameBoard.isGameOn()) {
            for (Player p : players) {
                //set the next player to chose assistant card
                //do this operation for all players except the last one,
                //because the next current player must be defined based on assistants ranks played
                //sends model change that executes newTurn on a client side
                if(p.isActive())
                    gameBoard.setCurrentPlayer(p);

                while (gameBoard.isGameOn() && p.getPlayedAssistant() == null && p.isActive()) {
                    //waits until client doesn't insert assistant card only if it is active client
                    this.wait();
                    //check if game was ended
                    gameBoard.setGameOn(!checkEndGame());
                }
            }
        }

        /*sorts players based on rank, from lowest to highest so that the next turn
        is started by player that played the card with the lowest rank
        p.s: not active players come last*/
        Collections.sort(players);

        /*action phase*/
        for (Player p : players) {
            //give the turn only if this is not the end of the Game and only
            //the player has playedAssistant which implies that it is active
            if (gameBoard.isGameOn() && p.getPlayedAssistant()!=null) {
                newTurn(p);
            }
        }

        //control if there are not-active players that still need to be assigned a cloud randomly
        List<Cloud> unusedClouds;
        for (Player p: players){
            if ( p.getPlayedAssistant()==null ){
                unusedClouds = gameBoard.getClouds().stream().filter(cloud -> cloud.getStudentsColors().size() > 0).collect(Collectors.toList());
                //chose random cloud from those unused
                int cloudNumber = new Random().nextInt(unusedClouds.size());
                //use the cloud that server chosen for inactive player
                gameBoard.useCloud(gameBoard.getClouds().indexOf(unusedClouds.get(cloudNumber)));
            }
        }

        for (Player p : players) {
            gameBoard.setPlayedAssistantRank(0, p);
        }
    }

    private void newTurn(Player p) throws InterruptedException {
        /*virtual view controls current player before forwarding any method to controller*/
        gameBoard.setCurrentPlayer(p);

        while ( gameBoard.isGameOn() && (studentMove != (players.size() == 3? 4: 3) || !motherNatureMove || !useCloudMove) && p.getPlayedAssistant()!=null ) {
            //need to wait until all 3 conditions are satisfied unless player gets deactivated and unless game is finished
            //thread gets waked up by other threads that invoke moveStudent(), moveMN() and useCloud()
            //or if player gets disconnected inside changePlayerStatus() of VirtualView
            this.wait();
            //endOfGame is sent inside checkEndGame only if game was active previously
            gameBoard.setGameOn(!checkEndGame());
        }

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
    /**checks whether the game is finished and in affermative case sends EndOfGameChange to all active clients
     * and returns true, otherwise just returns false*/
    public boolean checkEndGame(){

        //the game finishes immediately when a player puts his last tower on an island
        for (Player p : players){
            if(p.checkEmptyTowers()){
                if(players.size()>3){
                    for (Player q : players){
                        if (!p.equals(q) && p.getTowerColor().equals(q.getTowerColor()) && q.checkEmptyTowers()) {
                            if (gameBoard.isGameOn())
                                gameBoard.notify(new EndOfGameChange(p.getName()));

                            return true;
                        }
                    }
                }
                else {
                    //the first player that has emptied his towers is a winner
                    if(gameBoard.isGameOn())
                        gameBoard.notify(new EndOfGameChange(p.getName()));

                    return true;
                }
            }
        }

        //search for the player that has fewer towers on SchoolBoard since this is the one that has conquered more islands
        Player leader = players.get(0);

        for(Player p : players) {
            if (!p.equals(leader) && p.getTowerColor().equals(leader.getTowerColor()) && p.getNumOfTowers() > 0) {
                //do this in case players[0] is the one that doesn't hold any towers
                leader = p;
            } else if (!p.equals(leader) && p.getNumOfTowers() < leader.getNumOfTowers()) {
                leader = p;
            } else if (!p.equals(leader) && p.getNumOfTowers() == leader.getNumOfTowers() && p.getNumOfTowers() > 0) {
                //in case of draw compare number of professors
                if (p.getProfessorsColor().size() > leader.getProfessorsColor().size()) {
                    leader = p;
                }
            }
        }

        boolean foundPlayerNoAssistants = false;
        for (Player p: players){
            if(p.getAssistantsRanks().isEmpty()){
                //control who is the winner
                foundPlayerNoAssistants = true;
                break;
            }
        }

        if (foundPlayerNoAssistants || gameBoard.checkBagEmpty() || gameBoard.getNumOfIslands()<=3) {
            if(gameBoard.isGameOn())
                gameBoard.notify(new EndOfGameChange(leader.getName()));

            return true;
        }

        //if there is only 1 active player then the game is finished and the remained player is the winner
        if (players.stream().filter(p -> p.isActive()).collect(Collectors.toList()).size() == 1) {
            if (gameBoard.isGameOn()) {
                gameBoard.notify(new EndOfGameChange(players.stream().filter(p -> p.isActive()).collect(Collectors.toList()).get(0).getName()));
            }
            return true;
        }

        //if nothing from above is true then this is not yet the end of the game
        return false;
    }

    /*the packet received executes certain methods on Game*/
    public void usePacket(Packet packet){
        packet.execute(this);
    }

    public boolean getAdvancedSettings() {
        return advancedSettings;
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

    public void setStudentMove(int studentMove){
        this.studentMove = studentMove;
    }

    public void setMotherNatureMove(boolean motherNatureMove){
        this.motherNatureMove=motherNatureMove;
    }


}