package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.modelChange.EndOfGameChange;
import it.polimi.ingsw.modelChange.ExceptionChange;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.packets.Packet;
import it.polimi.ingsw.server.model.Character;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

/**<p>This class represents the game controller.</p>
 * <p>The game controller executes any valid action requested by client or throws an exception in case of errors in client request</p>
 * Also game controller responds for game flow and order of client actions
 * <ul>
 *     Contains
 *     <li>game board</li>
 *     <li>players list</li>
 *     <li>counters of client actions</li>
 * </ul>
 * */
public class Game implements GameForClient{
    private static Game instanceOfGame;
    private GameBoard gameBoard;
    private ArrayList<Player> players;
    private boolean advancedSettings;
    private Game(){}
    private int studentMove;
    private boolean motherNatureMove;
    private boolean useCloudMove;
    private boolean characterUsed;
    private boolean suspended;
    private Character defaultCharacter;

    /**Returns or creates, if not already, the instance of Game
     * @return instanceOfGame the instance of Game
     * */
    public static Game getInstanceOfGame() {
        if(instanceOfGame==null){
            instanceOfGame = new Game();
        }
        return instanceOfGame;
    }

    /**Initializes the game:
     * <ul>
     *     <li>Creates {@link Player} instances, refills {@link Game#players} list, shuffles it and assigns random player as a current</li>
     *     <li>Creates and initializes {@link GameBoard} instance</li>
     *     <li>If {@link Game#advancedSettings} is equal to true, pops 3 characters from characterDeck,
     *     calls {@link Character#initialFill} and sets characters in GameBoard using {@link GameBoard#setPlayedCharacters}</li>
     *     <li>Sets default character {@link Character} as a current character</li>
     *     <li>Refills assistants hand and {@link SchoolBoard} entrance of each player and adds 1 coin to each player in case of expert mode</li>
     * </ul>
     * @param playersNames  names of players that want to play
     * @param advancedSettings  true if players decided to play in expert mode, false otherwise
     * @param characterDeck  instance of {@link CharacterDeck}
     * @throws InvalidParameterException  if playerNames.size() is not equal to 2 or 3 or 4
     */
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

        defaultCharacter=new Character();
        defaultCharacter.initialFill(this);

        gameBoard.setCurrentCharacterToDefault(defaultCharacter);


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
        characterUsed = false;

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

    private synchronized void playGame() throws InterruptedException {
        //let the game start
        gameBoard.setGameOn(true);

        //unsuspend the game
        this.proceed();

        //wake up server thread that waits for refill clouds and set currentPlayer to happen
        //only after that server thread will attach virtual views to gameBoard and send gameBoard change
        //this will happen after current thread enters in wait
        this.notifyAll();

        while(gameBoard.isGameOn()){
            newRound();
        }

        System.out.println("Game says: The game was finished !");
        System.exit(0);
    }


    /**Starts a new round. New round is a sequence of actions consisting of
     * refillClouds(), useAssistant() for each active player, sorting of players based on assistants played,
     * newTurn() for each active player, eventually assigning unused clouds for not active players and reset of playedAssistant for all players*/
    private void newRound() throws InterruptedException {
        //if player is not active then skip him until next planning phase
        //in planning phase check isActive variable , in action phase it is enough to check playedAssistant variable

        //checkEndGame() is called inside refillClouds() since this is the action that might finish the game
        gameBoard.refillClouds();

        /*planning phase*/
        //players are sorted in order in which they should play assistant card
        boolean foundPlayerWOAssistant = true;
        while(gameBoard.isGameOn() && foundPlayerWOAssistant) {
            for (Player p : players) {
                //set the next player to chose assistant card
                //do this operation for all players except the last one,
                //because the next current player must be defined based on assistants ranks played
                //sends model change that executes newTurn on a client side
                if(p.isActive() && p.getPlayedAssistant()==null)
                    gameBoard.setCurrentPlayer(p);

                while (gameBoard.isGameOn() && p.getPlayedAssistant() == null && p.isActive()) {
                    //waits until client doesn't insert assistant card only if it is active client
                    this.wait();
                    //check if game was ended
                    gameBoard.setGameOn(!checkEndGame());
                }
            }
            foundPlayerWOAssistant=false;
            for(Player p : players){
                if(p.getPlayedAssistant()==null && p.isActive()){
                    foundPlayerWOAssistant=true;
                }
            }
        }

        /*sorts players based on rank, from lowest to highest so that the next turn
        is started by player that played the card with the lowest rank
        p.s: not active players come last*/
        Collections.sort(players);

        /*action phase*/
        for (Player p : players) {
            //give the turn only if this is not the end of the Game and only to
            //the player that has playedAssistant which implies that it is active
            if (gameBoard.isGameOn() && p.getPlayedAssistant()!=null) {
                gameBoard.setCurrentCharacterToDefault(defaultCharacter);
                newTurn(p);
            }
        }

        //control if there are not-active players that still need to be assigned a cloud randomly
        List<Cloud> unusedClouds;
        for (Player p: players){
            //refill only those players that are disconnected and haven't yet used any cloud
            if ( p.getPlayedAssistant()==null && p.getNumOfStudentsInEntrance() < gameBoard.getMaxNumOfStudentsInEntrance() ){
                unusedClouds = gameBoard.getClouds().stream().filter(cloud -> cloud.getStudentsColors().size() > 0).collect(Collectors.toList());
                //chose random cloud from those unused
                int cloudNumber = new Random().nextInt(unusedClouds.size());
                //use the cloud that server chosen for inactive player
                try {
                    gameBoard.useCloud(gameBoard.getClouds().indexOf(unusedClouds.get(cloudNumber)), p);
                }
                catch (NumOfStudentsExceeded e){

                }
            }
        }


        for (Player p : players) {
            gameBoard.setPlayedAssistantRank(0, p);
        }
    }

    /**Starts a new turn for a player which consists of
     * Setting this player as a current
     * Accepting actions related to studentMove, motherNatureMove, useCloudMove and eventually characterUsed*/
    private void newTurn(Player p) throws InterruptedException {
        /*virtual view controls current player before forwarding any method to controller*/
        gameBoard.setCurrentPlayer(p);
        //need to wait until all 3 conditions are satisfied unless player gets deactivated and unless game is finished
        //thread gets waked up by other threads that invoke moveStudent(), moveMN(), useCloud() and buy/activateCharacter()
        //or if player gets disconnected or reconnected inside changePlayerStatus() of VirtualView
        while ( gameBoard.isGameOn() && (studentMove != (players.size() == 3? 4: 3) || !motherNatureMove || !useCloudMove) && p.getPlayedAssistant()!=null ) {
            this.wait();
            //endOfGame is sent inside checkEndGame only if game was active previously
            gameBoard.setGameOn(!checkEndGame());
        }

        studentMove = 0;
        motherNatureMove = false;
        useCloudMove = false;
        characterUsed = false;
    }

    public Player getCurrentPlayer(){
        return gameBoard.getCurrentPlayer();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public synchronized void useAssistant(int assistantRank){
        if (assistantRank < 1 || assistantRank > 10){
            throw new IllegalArgumentException("Assistant rank value is wrong");
        }
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

    public synchronized void moveStudentToIsland(Color studentColor, int islandNumber){
        if (studentMove > (players.size() == 3? 4: 3))
            throw new WrongActionException("You have already moved all students");

        gameBoard.addStudentToIsland(gameBoard.getCurrentPlayer(), studentColor, islandNumber);
        studentMove++;
        this.notifyAll();
    }

    public synchronized void moveStudentToDining(Color studentColor){
        if (studentMove > (players.size() == 3? 4: 3))
            throw new WrongActionException("You have already moved all students");

        removeStudentFromEntrance(studentColor);
        addStudentToDining(gameBoard.getCurrentPlayer(), studentColor);
        studentMove++;
        this.notifyAll();
    }

    public void addStudentToDining(Player player, Color studentColor){
        gameBoard.addStudentToDining(player, studentColor);

        /*if this is advanced version of game, I pay 1 coin to player for each 3rd student*/
        if ( advancedSettings && player.getNumOfStudentsInDining(studentColor) % 3==0 ){
            gameBoard.addCoinsToPlayer(player, 1);
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
        //client can't make this action if students weren't moved or if mother nature was already moved
        if (studentMove != (players.size() == 3? 4: 3) ) {
            throw new WrongActionException("You have not moved all students yet!");
        }
        if (motherNatureMove){
            throw new WrongActionException("You have already moved mother nature!");
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

    public synchronized void useCloud(int cloudNumber){
        if (studentMove != (players.size() == 3? 4: 3) || !motherNatureMove || useCloudMove) {
            //client can't make this action if students weren't moved
            //or if mother nature wasn't already moved or cloud was already used
            throw new WrongActionException("You cannot use cloud right now!");
        }
        gameBoard.useCloud(cloudNumber);
        useCloudMove = true;
        this.notifyAll();
    }

    public synchronized void buyCharacter(int characterNumber){
        if (characterUsed){
            throw new WrongActionException("You have already used character in this turn!");
        }
        if (!advancedSettings) {
            throw new WrongActionException("You can't use characters. Advanced settings were set to false!");
        }

        gameBoard.buyCharacter(characterNumber);
        characterUsed = true;

        //don't notify game thread since only the characters that need activation may end the game
    }

    public synchronized void activateCharacter(int islandNumber){
        if (!advancedSettings)
            throw new WrongActionException("You can't use characters. Advanced settings were set to false");

        gameBoard.activateCharacter(islandNumber);

        //wake up the game thread that waits for action
        //check end game will be done in newTurn()
        this.notifyAll();
    }

    public synchronized void activateCharacter(ArrayList<Color> toBeSwappedStudents, ArrayList<Color> selectedStudents){
        if (!advancedSettings)
            throw new WrongActionException("You can't use characters. Advanced settings were set to false");

        gameBoard.activateCharacter(toBeSwappedStudents, selectedStudents);

        //wake up the game thread that waits for action
        //check end game will be done in newTurn()
        this.notifyAll();
    }

    public synchronized void activateCharacter(Color color, int islandNumber){
        if (!advancedSettings)
            throw new WrongActionException("You can't use characters. Advanced settings were set to false");

        gameBoard.activateCharacter(color, islandNumber);

        //wake up the game thread that waits for action
        //check end game will be done in newTurn()
        this.notifyAll();
    }
    public synchronized void activateCharacter(Color color){
        if (!advancedSettings)
            throw new WrongActionException("You can't use characters. Advanced settings were set to false");

        gameBoard.activateCharacter(color);

        //wake up the game thread that waits for action
        //check end game will be done in newTurn()
        this.notifyAll();
    }

    /**Checks whether the game is finished and in affermative case sends {@link EndOfGameChange} to all active clients
     * and returns true, otherwise just returns false.
     * <ul>The game is finished when one of the following conditions verifies:
     *      <li>A player puts his last tower on an island ( that particular player becomes the winner )</li>
     *      <li>A player has no assistants ( the player that has fewer towers becomes the winner )</li>
     *      <li>The bag is empty ( the player that has fewer towers becomes the winner )</li>
     *      <li>Number of islands is less or equal than 3 ( the player that has fewer towers becomes the winner )</li>
     *      <li>Only one active player or active team has remained connected
     *      ( The timer for reconnection gets started. If time expires then that player or team becomes the winner )</li>
     * </ul>
     * */
    public boolean checkEndGame(){

        //the game finishes immediately when a player puts his last tower on an island
        for (Player p : players){
            if(p.checkEmptyTowers()){
                if(players.size()>3){
                    for (Player q : players){
                        //check that a teammate has no towers as well
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
        /*List<Player> towersPlayers = players.stream().filter((p)->p.getNumOfTowers() > 0).sorted((p1, p2) -> {
            if (p1.getNumOfTowers() == p2.getNumOfTowers()){
                return p2.getProfessorsColor().size() - p1.getProfessorsColor().size();
            }
            else{
                return p1.getNumOfTowers() - p2.getNumOfTowers();
            }
        }).collect(Collectors.toList());*/

        Player leader = players.stream().filter((p)->p.getNumOfTowers() > 0).sorted((p1, p2) -> {
            if (p1.getNumOfTowers() == p2.getNumOfTowers()){
                return p2.getProfessorsColor().size() - p1.getProfessorsColor().size();
            }
            else{
                return p1.getNumOfTowers() - p2.getNumOfTowers();
            }
        }).findFirst().get();


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

        //if there is only 1 active player in case of 2-3 players then the game gets suspended for 60 sec
        List<Player> activePlayers = players.stream().filter(p -> p.isActive()).collect(Collectors.toList());
        while (activePlayers.size() == 1) {
            if (gameBoard.isGameOn()) {
                Timer timer = new Timer();
                try {
                    suspended = true;
                    final int[] secondsToWait = {60};
                    //TODO send GameSuspendedException each second
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            if(secondsToWait[0]>=0) {
                                gameBoard.notify(new ExceptionChange(
                                        new GameSuspendedException("Wait for other players reconnection.\n" + secondsToWait[0] + "seconds remained")
                                ));
                                System.out.println("I have sent GameSuspendedException. Seconds to wait " + secondsToWait[0]);
                                secondsToWait[0]--;
                            }
                        }
                    }, 0, 1000);

                    this.wait(60*1000);
                } catch (InterruptedException e) {
                    //if something went wrong then finish the game
                    System.out.println("I am in checkEndGame() of Game in active players control. The thread was interrupted");
                    e.printStackTrace();
                    return true;
                }
                //if no client has been reconnected in 60 sec, then the game is finished
                if (suspended) {
                    timer.cancel();
                    timer.purge();
                    gameBoard.notify(new EndOfGameChange(activePlayers.get(0).getName()));
                    return true;
                }
                else{
                    timer.cancel();
                    timer.purge();
                    System.out.println("A player was reconnected");
                }
            }
            activePlayers = players.stream().filter(p -> p.isActive()).collect(Collectors.toList());
        }

        //if there are 2 active players from the same team then the game gets suspended for 60 sec
        while (activePlayers.size() == 2 && players.size() == 4) {
            if (activePlayers.get(0).getTowerColor().equals(activePlayers.get(1).getTowerColor())){
                if (gameBoard.isGameOn()) {
                    Timer timer = new Timer();
                    try {
                        suspended = true;
                        final int[] secondsToWait = {60};
                        //TODO send GameSuspendedException each second
                        //schedule a thread to send GameSuspendedException each second
                        timer.scheduleAtFixedRate(new TimerTask() {
                            @Override
                            public void run() {
                                if(secondsToWait[0]>=0) {
                                    gameBoard.notify(new ExceptionChange(
                                            new GameSuspendedException("Wait for other players reconnection.\n" + secondsToWait[0] + "seconds remained")
                                    ));
                                    secondsToWait[0]--;
                                    System.out.println("I have sent GameSuspendedException. Seconds to wait " + secondsToWait[0]);
                                }
                            }
                        }, 0, 1000);

                        this.wait(60*1000);
                    } catch (InterruptedException e) {
                        //if something went wrong then finish the game
                        System.out.println("I am in checkEndGame() of Game in active players control. The thread was interrupted");
                        e.printStackTrace();
                        return true;
                    }
                    //if no client has been reconnected in 60 sec, then the game is finished
                    if (suspended) {
                        timer.cancel();
                        timer.purge();
                        gameBoard.notify(new EndOfGameChange(activePlayers.get(0).getName()));
                        return true;
                    }
                    else {
                        timer.cancel();
                        timer.purge();
                        System.out.println("A player was reconnected");
                    }
                }
            }
            else{
                break;
            }
            activePlayers = players.stream().filter(p -> p.isActive()).collect(Collectors.toList());
        }

        //if nothing from above is true then this is not yet the end of the game
        return false;
    }

    /**Executes an action requested from particular client*/
    public void usePacket(Packet packet){
        packet.execute(this);
    }

    public boolean getAdvancedSettings() {
        return advancedSettings;
    }

    public boolean isSuspended(){
        return suspended;
    }

    /**Sets {@link #suspended} attribute equal to false*/
    public void proceed(){
        this.suspended = false;
    }

    /**Sets {@link #suspended} attribute equal to true*/
    public void suspend(){
        this.suspended = true;
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