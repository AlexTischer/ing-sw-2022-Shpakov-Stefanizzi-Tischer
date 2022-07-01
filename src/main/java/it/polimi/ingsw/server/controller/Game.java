package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.modelChange.EndOfGameChange;
import it.polimi.ingsw.modelChange.ExceptionChange;
import it.polimi.ingsw.packets.Packet;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.Character;
import it.polimi.ingsw.server.model.*;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

/**<p>This class represents the game which serves as a controller in MVC pattern</p>
 * <p>The game controller executes any valid action requested by client or throws an exception in case of errors in client request</p>
 * Also game controller responds for game flow and order of client actions
 * <ul>
 *     Contains
 *     <li>{@link #gameBoard} - instance of game board that serves as a model in MVC pattern</li>
 *     <li>{@link #players} - list of players that were in {@link Server#waitingConnection} lobby at the moment of gam creation</li>
 *     <li>
 *         <p>{@link #studentMove} - counter of how many students have been moved during current round</p>
 *         <p>{@link #motherNatureMove} - flag that tells whether a mother nature has been moved during current round</p>
 *         <p>{@link #useCloudMove} - flag that tells whether a cloud has been used during current round</p>
 *         <p>{@link #characterUsed} - flag that tells whether a character has been used during current round</p>
 *     </li>
 *     <li>
 *         {@link #suspended} attribute tells whether this Game is suspended
 *         <p>Note: Game is suspended when only 1 player or 1 team remains connected to the server</p>
 *     </li>
 *     <li>
 *         {@link #defaultCharacter} - instance of {@link Character} that executes operations in standard mode
 *     </li>
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
     *     calls {@link Character#initialFill} and sets characters in GameBoard using {@link GameBoard#setPlayedCharacters}
     *     </li>
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

    /**
     * Resets counters of player's actions
     * Launches a separate thread that executes {@link #playGame()} method*/
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

    /**<ul>
     *      <li>Sets {@link GameBoard#isGameOn} to true and {@link #suspended} to false</li>
     *      <li>Wakes up a thread in {@link Server#createGame()} that waits for {@link GameBoard#refillClouds()}
     *      and {@link GameBoard#setCurrentPlayer(Player)} to happen before it can send
     *      {@link it.polimi.ingsw.modelChange.GameBoardChange } to all clients</li>
     *      <li>Invokes {@link #newRound()} as long as {@link GameBoard#isGameOn} is equal to true</li>
     *      <li>Turns off virtual machine when game gets turned off</li>
     * </ul>
     * */
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


    /**Starts a new round.
     * <ul>New round is a sequence of actions consisting of
     *      <li>{@link GameBoard#refillClouds()}</li>
     *      <li>{@link #useAssistant(int)} for each active player</li>
     *      <li>sorting of players based on assistants played</li>
     *      <li>{@link #newTurn(Player)} for each active player</li>
     *      <li>eventual assignment of unused clouds for not active players</li>
     *      <li>reset of playedAssistant for all players</li>
     * </ul>*/
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
     * <ul>
     *      <li>Setting this player as a current. See {@link GameBoard#setCurrentPlayer(Player)} method</li>
     *      <li>
     *          Accepting actions that modify studentMove, motherNatureMove, useCloudMove and eventually characterUsed
     *          <p>Note: Game thread goes in wait before receiving an action from client. That action wakes up this thread</p>
     *      </li>
     *      <li>
     *          Resets following counters and flags related to the current player's actions
     *          {@link #studentMove}, {@link #motherNatureMove}, {@link #useCloudMove}, {@link #characterUsed}
     *      </li>
     * </ul>*/
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

    /**
     * Calls {@link GameBoard#getCurrentPlayer()} method
     *
     * @return current player*/
    public Player getCurrentPlayer(){
        return gameBoard.getCurrentPlayer();
    }

    /**
     * @return  {@link #players}*/
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * This method can be executed only when game thread goes in wait for a player`s action  inside {@link #newRound()}
     * <p>Calls {@link GameBoard#setPlayedAssistantRank(int, Player)} method and wakes up game thread
     * so that it can wait assistant from the next player or pass to the action phase</p>
     * @param assistantRank  rank of the assistant that player wants to play
     * @throws IllegalArgumentException  if assistantRank is less than 1 or greater than 10
     * @throws RepeatedAssistantRankException  if {@link #checkAssistant(int, Player)} returns <code>false</code>
     * */
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

    /**
     * <p>Check whether an input player can play an assistant with an assistantRank</p>
     * <ul>
     *     Player can play an assistant with a certain rank if the following conditions get satisfied
     *     <li>
     *         There is an assistant with such rank in his hand {@link Player#assistants}
     *     </li>
     *     <li>
     *         The rank is different from ranks already played by other players in the {@link #newRound()}
     *         or the player has no other option than playing an assistant already played by someone else
     *         <p>Note: In this case however a player is put on the last position in {@link #players} after sorting
     *         in {@link #newRound()}</p>
     *     </li>
     * </ul>
     * @param  assistantRank rank of assistant that player wants to play
     * @param  player  player that wants to play an assistant
     * @return  <code>true </code>if player can play an assistant with assistantRank, <code>false</code> otherwise
     * */
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

    /**
     * <ul>
     * This method can be executed only when game thread goes in wait for a player`s action  inside {@link #newTurn(Player)}
     * <li>Calls {@link GameBoard#addStudentToIsland(Player, Color, int)} method passing <code>currentPlayer</code> as a parameter</li>
     * <li>Increments {@link #studentMove} counter</li>
     * <li>Wakes up game thread so that it can pass to the next step of action phase</li>
     * </ul>
     * @param studentColor  color of the student that currentPlayer wants to move
     * @param islandNumber  index of the island that that currentPlayer wants to move student on
     * @throws WrongActionException  if player has already moved required number of students during his turn
     * @throws IllegalArgumentException  if islandNumber is less than 0 or greater than number of available islands - 1
     * @throws StudentNotFoundException  if there is no student of such color in entrance of player`s school board
     * */
    public synchronized void moveStudentToIsland(Color studentColor, int islandNumber){
        if (studentMove > (players.size() == 3? 4: 3))
            throw new WrongActionException("You have already moved all students");

        gameBoard.addStudentToIsland(gameBoard.getCurrentPlayer(), studentColor, islandNumber);
        studentMove++;
        this.notifyAll();
    }

    /**
     * <ul>
     * This method can be executed only when game thread goes in wait for a player`s action inside {@link #newTurn(Player)}
     * <li>Removes student of studentColor from dining of <code>currentPlayer</code> by calling {@link #removeStudentFromEntrance(Color)}</li>
     * <li>Adds student of studentColor in dining of <code>currentPlayer</code> by calling {@link #addStudentToDining(Player, Color)}</li>
     * <li>Increments {@link #studentMove} counter</li>
     * <li>Wakes up game thread so that it can pass to the next step of action phase</li>
     * </ul>
     * @param studentColor  color of the student that currentPlayer wants to move in dining room
     * @throws WrongActionException  if player has already moved required number of students during his turn
     * @throws NumOfStudentsExceeded  if there is no free space in dining room for that studentColor
     * @throws StudentNotFoundException  if there is no student of such color in entrance of player`s school board
     * @throws NoEnoughCoinsException  if GameBoard bank has run out of coins
     * */
    public synchronized void moveStudentToDining(Color studentColor){
        if (studentMove > (players.size() == 3? 4: 3))
            throw new WrongActionException("You have already moved all students");

        removeStudentFromEntrance(studentColor);
        addStudentToDining(gameBoard.getCurrentPlayer(), studentColor);
        studentMove++;
        this.notifyAll();
    }

    /**
     * <ul>
     * <li>Adds student to dining room of input player`s school board by calling
     * {@link GameBoard#addStudentToDining(Player, Color)}</li>
     * <li>If {@link #advancedSettings} is equal to true, pays 1 coin if 3rd, 6th or 9th student
     * of that color was put in dining room by calling {@link GameBoard#addCoinsToPlayer(Player, int)}</li>
     * <li>Calls {@link #reassignProfessor()} to check whether professors` owners changed</li>
     * <li>Generates {@link EndOfChangesException} and sends it to all active players by calling {@link it.polimi.ingsw.utils.Observable#notify(Object)}
     * after any other <code>ModelChange</code> generated and sent from the methods above</li>
     * </ul>
     * @param player  player that wants to add a student to it`s dining room
     * @param studentColor  color of the student that player wants to add in dining
     * @throws NumOfStudentsExceeded  if there is no free space in dining room for that studentColor
     * @throws NoEnoughCoinsException  if GameBoard bank has run out of coins
     * */
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

    /**
     * Adds student to an island by calling {@link GameBoard#addStudentToIsland(Color, int)}
     * @param studentColor  color of the student to add to an island
     * @param islandNumber  index of an island that student must be added to
     * @throws IllegalArgumentException  if islandNumber is index that is out of bound in {@link GameBoard#islands} list
     * */
    public void addStudentToIsland(Color studentColor, int islandNumber){
        gameBoard.addStudentToIsland(studentColor, islandNumber);
    }

    /**
     * Removes student of studentColor from current players`s {@link SchoolBoard} entrance
     * by calling {@link GameBoard#removeStudentFromEntrance(Player, Color)} passing
     * <code>currentPlayer</code> as a parameter
     * <p>Note: see {@link GameBoard#getCurrentPlayer()}</p>
     * @param studentColor  color of the student to remove from entrance
     * @throws StudentNotFoundException  if there is no student of such color in entrance
     * */
    public void removeStudentFromEntrance(Color studentColor){
        gameBoard.removeStudentFromEntrance(gameBoard.getCurrentPlayer(), studentColor);
    }

    /**
     * Removes student of studentColor from player`s schoolBoard dining room
     * by calling {@link GameBoard#removeStudentFromDining(Player, Color)}
     * @param player  player who`s {@link SchoolBoard} is to be taken student from
     * @param studentColor  color of the student to remove from dining
     * @throws StudentNotFoundException  if there is no student of such color in the dining room
     * */
    public void removeStudentFromDining(Player player, Color studentColor){
        gameBoard.removeStudentFromDining(player, studentColor);
    }

    /**
     * Calls {@link GameBoard#getStudentFromBag()}
     * @return  color of the student extracted from {@link Bag}
     * @throws NoEnoughStudentsException  if the bag is empty
     * */
    public Color getStudent(){
        return gameBoard.getStudentFromBag();
    }

    /**
     * <ul>
     * <li>Finds a player that has most influence on an island.
     * Such player becomes the owner of the island.
     * <p>The influence is calculated by calling {@link #calculateInfluence(int, Player)}</p>
     * </li>
     * <li>If the island has no tower then the owner puts it`s tower on it.
     * <p>This operation is called island conquer</p></li>
     * <li>If the island has at least 1 tower and the new owner is different from the previous one, then
     *      <ul>
     *          <li>towers on island get substituted by towers of the new owner by calling
     *          {@link GameBoard#addTowersToIsland(int, Player)} and {@link GameBoard#removeTowersFromPlayer(int, Player)}</li>
     *          <li>previous towers return back to the schoolBoard of the previous owner by calling {@link GameBoard#addTowersToPlayer(int, Player)}</li>
     *      </ul>
     * This operation is called island reassignment</li>
     * <li>If the island has at least 1 tower and the owner hasn`t changed, then nothing happens</li>
     * <li>Calls {@link GameBoard#mergeIslands()} at the end to check if any adjacent islands can be merged</li>
     * <li>Note: If there is <code>NoEntryTile</code> on the island then island doesn`t get reassigned</li>
     * @param islandNumber  the index of the island that may be reassigned or conquered
     * @throws NoEnoughTowersException  if there are no enough towers on the school board of
     * the new owner of the island, that needs to substitute towers that were located on island previously
     * */
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


    /**
     * Calculates influence score of a player on the specified island by calling
     * {@link GameBoard#calculateInfluence(int, Player)}
     * <p>If there is a NoEntryTile on the island returns it to the {@link Character5}
     * by calling {@link GameBoard#addNoEntryTile()}</p>
     * @param islandNumber  index of the island to calculate influence on
     * @param player  instance of the player to calculate influence for
     * @return -1 if there is a NoEntryTile on the island,
     * otherwise returns an influence score of the player on the island
     *
     * */
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

    /**
     * Calls {@link Character#reassignProfessor()} implementation of currentCharacter played in this round.
     * <p>If no character were played or {@link #advancedSettings} is equal to false,
     * then <code>currentCharacter</code> is set to default character {@link Character}</p>
     * */
    public void reassignProfessor(){
        gameBoard.getCurrentCharacter().reassignProfessor();
    }

    /**
     * Puts or removes noEntryTile on an island by calling {@link GameBoard#setNoEntry(int, boolean)}
     * @param islandNumber  number of island to put or remove noEntryTile from
     * @param noEntry  true if noEntryTile needs to be put, false if noEntryTile needs to be removed
     * @throws IllegalArgumentException  if islandNumber index is out of bound in {@link GameBoard#islands} list
     * @throws NoEntryException  if more than one <code>noEntryTile</code> is being placed on this island
     * */
    public void setNoEntry(int islandNumber, boolean noEntry){
        gameBoard.setNoEntry(islandNumber, noEntry);
    }

    /**
     * <ul>
     * This method can be executed only when game thread goes in wait for a player`s action inside {@link #newTurn(Player)}
     * <li>Calls {@link GameBoard#moveMotherNature(int)} method</li>
     * <li>Calls {@link #reassignIsland(int)} to conquer or reassign an island corresponding to the new positionOfMotherNature</li>
     * <li>Checks whether tha game was finished with {@link #checkEndGame()}</li>
     * <li>In case game continues, generates {@link EndOfChangesException} and sends it to all active players by calling {@link it.polimi.ingsw.utils.Observable#notify(Object)}
     * after any other <code>ModelChange</code> generated and sent from the methods above</li>
     * <li>Sets {@link #motherNatureMove} flag to true</li>
     * <li>Wakes up game thread so that it can pass to the next step of action phase</li>
     * </ul>
     * @param steps  number of steps to move mother nature
     * @throws WrongActionException  if currentPlayer hasn`t already moved required number of students during his turn
     * or currentPlayer has already moved motherNature
     * @throws IllegalArgumentException if currentPlayer is not allowed to move motherNature for this number of steps
     * */
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

    /**
     * <ul>
     * This method can be executed only when game thread goes in wait for a player`s action inside {@link #newTurn(Player)}
     * <li>Calls {@link GameBoard#useCloud(int)} method</li>
     *
     * <li>Sets {@link #useCloudMove} flag to true</li>
     * <li>Wakes up game thread so that it can pass to the next step of action phase</li>
     * </ul>
     * @param cloudNumber  number of cloud that {@link GameBoard#currentPlayer} wants to use
     * @throws WrongActionException  if currentPlayer hasn`t already moved required number of students during his turn
     * or currentPlayer hasn`t already moved motherNature or currentPlayer has already used a cloud
     * @throws IllegalArgumentException  if cloudNumber index is out of bound in {@link GameBoard#clouds} list
     * @throws StudentNotFoundException  if this cloud is empty
     * @throws NumOfStudentsExceeded  if the entrance of player is full
     * */
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

    /**
     * <p>This method can be executed only when game thread goes in wait for a player`s action inside {@link #newTurn(Player)}</p>
     * <p>Calls {@link GameBoard#buyCharacter(int)}</p>
     * <p>Sets {@link #characterUsed} flag to <code>true</code></p>
     * @param characterNumber  index of character that currentPlayer wants to buy
     * @throws WrongActionException  if currentPlayer has already used an assistant during it`s turn
     * or if {@link #advancedSettings} is set to false
     * @throws IllegalArgumentException if index of character is out of bond in {@link GameBoard#playedCharacters}
     * */
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

    /**<ul>
     * This method can be executed only when game thread goes in wait for a player`s action inside {@link #newTurn(Player)}
     * <li>Calls {@link GameBoard#activateCharacter(int)} method</li>
     * <li>Wakes up game thread so that it can pass to the next step of action phase</li>
     * </ul>
     * @param islandNumber  number of island needed for activation of {@link GameBoard#currentCharacter}
     * @throws WrongActionException  if {@link #advancedSettings} is set to false
     *
     * */
    public synchronized void activateCharacter(int islandNumber){
        if (!advancedSettings)
            throw new WrongActionException("You can't use characters. Advanced settings were set to false");

        gameBoard.activateCharacter(islandNumber);

        //wake up the game thread that waits for action
        //check end game will be done in newTurn()
        this.notifyAll();
    }

    /**<ul>
     * This method can be executed only when game thread goes in wait for a player`s action inside {@link #newTurn(Player)}
     * <li>Calls {@link GameBoard#activateCharacter(ArrayList, ArrayList)} method</li>
     * <li>Wakes up game thread so that it can pass to the next step of action phase</li>
     * </ul>
     * @param toBeSwappedStudents  students needed for activation of {@link GameBoard#currentCharacter}
     * @param selectedStudents  students needed for activation of {@link GameBoard#currentCharacter}
     * @throws WrongActionException  if {@link #advancedSettings} is set to false
     *
     * */
    public synchronized void activateCharacter(ArrayList<Color> toBeSwappedStudents, ArrayList<Color> selectedStudents){
        if (!advancedSettings)
            throw new WrongActionException("You can't use characters. Advanced settings were set to false");

        gameBoard.activateCharacter(toBeSwappedStudents, selectedStudents);

        //wake up the game thread that waits for action
        //check end game will be done in newTurn()
        this.notifyAll();
    }

    /**<ul>
     * This method can be executed only when game thread goes in wait for a player`s action inside {@link #newTurn(Player)}
     * <li>Calls {@link GameBoard#activateCharacter(Color, int)} method</li>
     * <li>Wakes up game thread so that it can pass to the next step of action phase</li>
     * </ul>
     * @param color  student needed for activation of {@link GameBoard#currentCharacter}
     * @param islandNumber island index in {@link GameBoard#islands} needed for activation of {@link GameBoard#currentCharacter}
     * @throws WrongActionException  if {@link #advancedSettings} is set to false
     * */
    public synchronized void activateCharacter(Color color, int islandNumber){
        if (!advancedSettings)
            throw new WrongActionException("You can't use characters. Advanced settings were set to false");

        gameBoard.activateCharacter(color, islandNumber);

        //wake up the game thread that waits for action
        //check end game will be done in newTurn()
        this.notifyAll();
    }

    /**<ul>
     * This method can be executed only when game thread goes in wait for a player`s action inside {@link #newTurn(Player)}
     * <li>Calls {@link GameBoard#activateCharacter(Color)} method</li>
     * <li>Wakes up game thread so that it can pass to the next step of action phase</li>
     * </ul>
     * @param color  student needed for activation of {@link GameBoard#currentCharacter}
     * @throws WrongActionException  if {@link #advancedSettings} is set to false
     * */
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
     *      ( The timer for reconnection gets started. If time expires then that player or team becomes the winner )
     *      <p>See: {@link Server#startTimer()} and {@link Server#resetTimer()}</p></li>
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

        List<Player> activePlayers = players.stream().filter(p -> p.isActive()).collect(Collectors.toList());
        while( players.stream().filter(p -> p.isActive()).map((p)->p.getTowerColor()).collect(Collectors.toSet()).size() == 1 ) {
            //if there is only 1 active player in case of 2-3 players then the game gets suspended for 60 sec
            activePlayers = players.stream().filter(p -> p.isActive()).collect(Collectors.toList());
            if (activePlayers.size() == 1) {
                System.out.println("Only one player remained");
                if (gameBoard.isGameOn()) {
                    try {
                        suspended = true;
                        System.out.println("I AM GOING IN WAIT STATE. ONLY 1 PLAYER REMAINED");
                        //gameBoard.notify(new ExceptionChange(new GameSuspendedException("Wait for other players to reconnect. 30 seconds remained")));
                        //start server timer that sends gameSuspendedException each second
                        if (!Server.isTimerActive())
                            Server.startTimer();

                        this.wait((Server.getRemainedTime()+1) * 1000);
                        System.out.println("I AM WAKEN UP");
                    } catch (InterruptedException e) {
                        //if something went wrong then finish the game
                        System.out.println("I am in checkEndGame() of Game in active players control. The thread was interrupted");
                        e.printStackTrace();
                        return true;
                    }
                    //if no client has been reconnected in 60 sec, then the game is finished
                    if (suspended) {
                        gameBoard.notify(new EndOfGameChange(activePlayers.get(0).getName()));
                        return true;
                    } else {
                        //gameBoard.notify(new ExceptionChange(new EndOfChangesException()));
                        System.out.println("A player was reconnected or disconnected 1");
                    }
                }
                activePlayers = players.stream().filter(p -> p.isActive()).collect(Collectors.toList());
            }

            //if there are 2 active players from the same team then the game gets suspended for 60 sec
            if (activePlayers.size() == 2 && players.size() == 4) {
                if (activePlayers.get(0).getTowerColor().equals(activePlayers.get(1).getTowerColor())) {
                    System.out.println("Only one team remained");
                    if (gameBoard.isGameOn()) {
                        try {
                            suspended = true;
                            System.out.println("I AM GOING IN WAIT STATE. ONLY 2 PLAYERS REMAINED");
                            //gameBoard.notify(new ExceptionChange(new GameSuspendedException("Wait for other players to reconnect. 30 seconds remained")));
                            if (!Server.isTimerActive())
                                Server.startTimer();

                            this.wait((Server.getRemainedTime()+1) * 1000);
                            System.out.println("I AM WAKEN UP!");

                        } catch (InterruptedException e) {
                            //if something went wrong then finish the game
                            System.out.println("I am in checkEndGame() of Game in active players control. The thread was interrupted");
                            e.printStackTrace();
                            return true;
                        }
                        //if no client has been reconnected in 60 sec, then the game is finished
                        if (suspended) {
                            gameBoard.notify(new EndOfGameChange(activePlayers.get(0).getName()));
                            return true;
                        } else {
                            //gameBoard.notify(new ExceptionChange(new EndOfChangesException()));
                            System.out.println("A player was reconnected or disconnected 2");
                        }
                    }
                }
            }
        }
        //if there are no active players, then game finishes
        if (activePlayers.size()==0)
            return true;

        //the timer was started only if there were only 1 player or 1 team remained
        //if thread exits from while and timer is still active then reset will send game activated
        //if timer was expired then I have already executed return true and I will never arrive up to this command
        Server.resetTimer();

        //if nothing from above is true then this is not yet the end of the game
        return false;
    }



    /**Executes the packet arrived from {@link it.polimi.ingsw.server.VirtualView} associated with
     * {@link GameBoard#currentPlayer}
     * <p>The packet contains a call to the method that client want to execute</p>
     * */
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