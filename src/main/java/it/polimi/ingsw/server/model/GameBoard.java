package it.polimi.ingsw.server.model;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.modelChange.*;
import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.utils.Observable;

import java.util.*;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameBoard extends Observable<ModelChange> {

    private static GameBoard instanceOfGameBoard;
    private Game game;
    private Bag instanceOfBag;
    private List<Island> islands;
    private List<Cloud> clouds;
    private Character currentCharacter;
    private Character playedCharacters[];
    private AssistantDeck assistantDeck;
    private int positionOfMotherNature;
    private int numOfCoins;
    private int maxNumOfStudentsInEntrance;
    private Player currentPlayer;
    private AtomicBoolean isGameOn = new AtomicBoolean(false);

    private GameBoard(){}

    /*Initializes instanceOfBag and clouds. Takes number of players and
    number of students to be in the bag( by default 130 ) */
    public void init(Game game, int numOfPlayers) {
        this.game = game;

        islands = new ArrayList<Island>(12);
        for (int i = 0; i < 12; i++) {
            islands.add(i, new Island());
        }

        /*position of MN from 0 to 11*/
        positionOfMotherNature = new Random().nextInt(12);

        instanceOfBag = new Bag();

        clouds = new ArrayList<Cloud>(numOfPlayers);

        for (int i = 0; i < numOfPlayers; i++) {
            clouds.add(i, new Cloud(numOfPlayers == 3 ? 4 : 3));
        }

        maxNumOfStudentsInEntrance = numOfPlayers == 3 ? 9 : 7;

        //assistantDeck = new AssistantFactory().getAssistantDeck();
        assistantDeck = new AssistantDeck();

        numOfCoins = 20-numOfPlayers;


    }

    public int getMaxNumOfStudentsInEntrance() {
        return maxNumOfStudentsInEntrance;
    }
    public static GameBoard getInstanceOfGameBoard() {
        if (instanceOfGameBoard == null)
            instanceOfGameBoard = new GameBoard();

        return instanceOfGameBoard;
    }

    public boolean isGameOn() {
        return isGameOn.get();
    }

    public void setGameOn(boolean val){
        isGameOn.set(val);
    }

    /*method that will be invoked at the start to refill entrance
    of each player`s SchoolBoard*/
    public void refillEntrance(Player player) throws NumOfStudentsExceeded {
        if (player.getNumOfStudentsInEntrance() >= maxNumOfStudentsInEntrance)
            throw new NumOfStudentsExceeded();

        for (int i = 0; i < maxNumOfStudentsInEntrance; i++)
            player.addStudentToEntrance(instanceOfBag.extractStudent());
    }

    public void refillAssistants(Player player){
        player.setAssistants(assistantDeck.popAssistants(player.getAssistantType()));
    }

    public void refillClouds() {
        //refill each cloud as much as needed
        try {
            for (Cloud cloud : clouds) {
                for (int i = cloud.getStudentsColors().size(); i < cloud.getMaxNumOfStudents(); i++)
                    cloud.addStudent(instanceOfBag.extractStudent());
            }
            CloudsChange cloudsChange = new CloudsChange(clouds);
            notify(cloudsChange);
        }
        catch (NoEnoughStudentsException e) {
            //all students were exhausted, game is finished
            //checkEndGame will send endOfGameChange for clients that wait
            setGameOn(!game.checkEndGame());
        }
    }

    /*fill each island with 1 student except the island with MN and opposite to it*/
    public void refillIslands(){
        for (int i = 0; i < islands.size(); i++){
            if ( i != positionOfMotherNature && i != ( (positionOfMotherNature + 6) %islands.size() ) )
                islands.get(i).addStudent(instanceOfBag.extractStudentForIslands());
        }
    }

    public void sendGameBoardChange() {
        GameBoardChange gameBoardChange = new GameBoardChange(this, game.getPlayers());
        notify(gameBoardChange);
        //ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        //notify(exceptionChange);
    }

    /**
     * Moves motherNature for the specified number of steps and updates it`s {@link #positionOfMotherNature}
     * <p>Checks whether a player is allowed to move motherNature for this number of steps by
     * calling {@link Character#moveMotherNature(int)} implementation of the currentCharacter</p>
     * <p>Generates {@link MotherNatureChange} and sends it to all active players
     * by calling {@link it.polimi.ingsw.utils.Observable#notify(Object)}</p>
     * @param steps  number of steps to move mother nature
     * @throws IllegalArgumentException  if {@link #currentPlayer} is not allowed to move motherNature for
     * this number of steps
     * */
    public void moveMotherNature(int steps) {
        if (!currentCharacter.moveMotherNature(steps))
            throw new IllegalArgumentException("This number of steps is not allowed");

        positionOfMotherNature = (positionOfMotherNature + steps) % islands.size();

        MotherNatureChange motherNatureChange = new MotherNatureChange(positionOfMotherNature);
        notify(motherNatureChange);
        //ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        //notify(exceptionChange);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;

        //model change that will launch recursively newTurn() on client side
        CurrentPlayerChange currentPlayerChange = new CurrentPlayerChange(currentPlayer.getName());
        notify(currentPlayerChange);
    }

    /**
     * <ul>
     *  <li>Removes student of studentColor from player`s schoolBoard entrance
     *  by calling {@link Player#removeStudentFromEntrance(Color)}
     *  </li>
     *  <li>
     *      Adds this student to island at index islandNumber by calling {@link Island#addStudent(Color)}
     *  </li>
     *  <li>
     *      Generates {@link IslandChange}, {@link SchoolBoardChange} and {@link EndOfChangesException} and
     *      sends them to all active players by calling {@link Observable#notify(Object)}
     *  </li>
     * </ul>
     * @param player  instance of player that wants to move student
     * @param studentColor  color of student that player wants to move
     * @param islandNumber  number of island that player wants to move student on
     * @throws IllegalArgumentException  if islandNumber is less than 0 or greater than number of available islands-1
     * @throws StudentNotFoundException  if there is no student of such color in entrance of player`s schoolboard
     * */
    public void addStudentToIsland(Player player, Color studentColor, int islandNumber) {
        if (islandNumber < 0 || islandNumber > islands.size()-1)
            throw new IllegalArgumentException("Error: invalid island number");
        player.removeStudentFromEntrance(studentColor);
        islands.get(islandNumber).addStudent(studentColor);

        IslandChange islandChange = new IslandChange(islands.get(islandNumber), islandNumber);
        notify(islandChange);
        SchoolBoardChange schoolBoardChange = new SchoolBoardChange(player);
        notify(schoolBoardChange);

        //send endOfChanges to let client know that there will be no further model changes corresponding to received packet
        ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        notify(exceptionChange);
    }

    /**
     * <ul>
     * <li>>Adds student of studentColor to an island at index islandNumber
     * by calling {@link Island#addStudent(Color)}</li>
     * <li>Generates {@link IslandChange} and sends them to all active players by calling {@link Observable#notify(Object)}
     * </li>
     * </ul>
     * @param studentColor  color of the student to add to an island
     * @param islandNumber  index of an island that student must be added to
     * @throws IllegalArgumentException  if islandNumber is index that is out of bound in {@link #islands} list
     * */
    public void addStudentToIsland(Color studentColor, int islandNumber){
        if (islandNumber < 0 || islandNumber > islands.size()-1)
            throw new IllegalArgumentException("Error: invalid island number");
        islands.get(islandNumber).addStudent(studentColor);
        IslandChange islandChange = new IslandChange(islands.get(islandNumber), islandNumber);
        notify(islandChange);
        //ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        //notify(exceptionChange);
    }

    public void useCloud(int cloudNumber) {
        useCloud(cloudNumber, currentPlayer);
    }

    /**<ul>
     * <li>Refills player`s school board entrance with students taken from a chosen cloud</li>
     * <li>Generates {@link CloudChange}, {@link SchoolBoardChange} and sends them to all
     * active players by calling {@link Observable#notify(Object)}</li>
     * </ul>
     * @param cloudNumber  index of cloud in {@link #clouds} that player wants to use
     * @param player  instance of player that wants to use a cloud
     * @throws IllegalArgumentException  if cloudNumber index is out of bound in {@link #clouds} list
     * @throws StudentNotFoundException  if this cloud is empty
     * @throws NumOfStudentsExceeded  if the entrance of player is full
     * */
    public void useCloud(int cloudNumber, Player player) {
        if (cloudNumber < 0 || cloudNumber >= clouds.size())
            throw new IllegalArgumentException("You have inserted invalid island number");

        if(clouds.get(cloudNumber).getStudentsColors().isEmpty())
            throw new StudentNotFoundException("This cloud is empty! Choose another one");

        for (Color color : clouds.get(cloudNumber).getStudentsColors()) {
            //add only as many students as needed
            if (player.getNumOfStudentsInEntrance() < this.maxNumOfStudentsInEntrance) {
                player.addStudentToEntrance(color);
                clouds.get(cloudNumber).removeStudent(color);
            }
            else {
                throw new NumOfStudentsExceeded("The entrance is full. There is no empty space for any student!");
            }
        }

        CloudChange cloudChange = new CloudChange(clouds.get(cloudNumber), cloudNumber);
        notify(cloudChange);
        SchoolBoardChange schoolBoardChange = new SchoolBoardChange(player);
        notify(schoolBoardChange);

        //does not send endOfChanges because after useCloud current player should change
        //ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        //notify(exceptionChange);
    }

    public int getNumOfIslands() {
        return islands.size();
    }

    /** <ul>
     * <li>Merges adjacent islands with the same {@link Island#towersColor}</li>
     * <li>Gets executed each time any island gets (re)assigned to any player</li>
     * <li>Generates {@link MotherNatureChange} in case islands merge causes the change of {@link #positionOfMotherNature}</li>
     * <li>Generates {@link IslandChange} in case any islands have been merged</li>
     * <li>Sends any generated {@link ModelChange} to all active players by calling {@link Observable#notify(Object)}</li>
     * <p>Note: see also {@link Game#reassignIsland(int)}, {@link Island#mergeIsland(Island)}</p>
     * </ul>
     * @return true if at least 2 islands have been merged, false otherwise
     */
    public void mergeIslands() {

        int oldNumOfIslands = islands.size();

        for (int i = 0; i < islands.size(); i++) {

            if (islands.get(i).getTowersColor() != null && islands.get((i+1) % islands.size()).getTowersColor() != null){
                /*if 2 tower colors are the same, then unify*/
                if (islands.get(i).getTowersColor().equals(islands.get((i+1) % islands.size()).getTowersColor())) {

                    islands.get(i).mergeIsland(islands.get((i+1) % islands.size()));


                    /*change position of MN only if it is located on the next islands*/
                    if (positionOfMotherNature > i || i == islands.size()-1 ){
                        positionOfMotherNature--;

                        if (positionOfMotherNature < 0)
                            positionOfMotherNature = islands.size()-2;
                        MotherNatureChange motherNatureChange = new MotherNatureChange(positionOfMotherNature);
                        notify(motherNatureChange);
                    }



                    /*remove empty island*/
                    islands.remove((i+1) % islands.size());

                    /*Decrement index in order to check if I can merge the same islands with the next one*/
                    i--;

                }
            }
        }
        if(oldNumOfIslands != islands.size()) {
            IslandsChange islandsChange = new IslandsChange(islands);
            notify(islandsChange);
            //ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
            //notify(exceptionChange);
        }
    }

    /**
     * @param islandNumber  number of the island to calculate influence score on
     * @param player  player to calculate score for
     * @return the influence score of the player on the island according to the character that was played
     * */
    public int calculateInfluence(int islandNumber, Player player) {
        return currentCharacter.calculateInfluence(islands.get(islandNumber), islandNumber, player);
    }

    /** <ul>
     * <li>Calls {@link Player#addProfessor(Color)}</li>
     * <li>Generates {@link SchoolBoardChange} and sends it to all active players by calling {@link Observable#notify(Object)}</li>
     * </ul>
     * 
     * @param player  which school board is to add professor to
     * @param color  color of the professor to add
     */
    public void addProfessor(Player player, Color color) {
        player.addProfessor(color);
        SchoolBoardChange schoolBoardChange = new SchoolBoardChange(player);
        notify(schoolBoardChange);
        //ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        //notify(exceptionChange);
    }
    
    /** <ul>
     * <li>Calls {@link Player#removeProfessor(Color)}</li>
     * <li>Generates {@link SchoolBoardChange} and sends it to all active players by calling {@link Observable#notify(Object)}</li>
     * </ul>
     *
     * @param player  which school board is to remove professor from
     * @param color  color of the professor to remove
     */
    public void removeProfessor(Player player, Color color) {
        player.removeProfessor(color);
        SchoolBoardChange schoolBoardChange = new SchoolBoardChange(player);
        notify(schoolBoardChange);
        //ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        //notify(exceptionChange);
    }

    public void addStudentToEntrance(Player player, Color studentColor) {

        if (player.getNumOfStudentsInEntrance() < maxNumOfStudentsInEntrance) {
            player.addStudentToEntrance(studentColor);
            SchoolBoardChange schoolBoardChange = new SchoolBoardChange(player);
            notify(schoolBoardChange);
            //ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
            //notify(exceptionChange);
        }
        else
            throw new NumOfStudentsExceeded();
    }

    /**<ul>
     * Adds towers of {@link Player#towerColor} to an island
     * <li>If there are no towers on island then adds only 1 tower by calling
     * {@link Island#addTower(TowerColor)}</li>
     * <li>If there is at least 1 tower on island then changes the color of
     * present towers by calling {@link Island#setTowersColor(TowerColor)}</li>
     * <li>Generates {@link IslandChange} and sends it to all active players
     * by calling {@link Observable#notify(Object)}</li>
     * </ul>
     * @param islandNumber  index of the island to add towers to
     * @param player  istance of player who`s towers are being added to island
     * */
    public void addTowersToIsland(int islandNumber, Player player){
        //method invoked by reassign island
        try {
            islands.get(islandNumber).setTowersColor(player.getTowerColor());
            IslandChange islandChange = new IslandChange(islands.get(islandNumber), islandNumber);
            notify(islandChange);
            //ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
            //notify(exceptionChange);
        }
        catch (UnsupportedOperationException e){
            /*there were no towers on island*/
            islands.get(islandNumber).addTower(player.getTowerColor());
            IslandChange islandChange = new IslandChange(islands.get(islandNumber), islandNumber);
            notify(islandChange);
        }
    }

    /**
     * <p>Removes student of studentColor from player`s {@link SchoolBoard} entrance
     * by calling {@link Player#removeStudentFromEntrance(Color)}</p>
     * <p>Generates {@link SchoolBoardChange} and sends it to all active players
     * by calling {@link Observable#notify(Object)}</p>
     * @throws StudentNotFoundException  if there is no student of such color in entrance
     * */
    public void removeStudentFromEntrance(Player player, Color studentColor) {
        player.removeStudentFromEntrance(studentColor);
        SchoolBoardChange schoolBoardChange = new SchoolBoardChange(player);
        notify(schoolBoardChange);
        //ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        //notify(exceptionChange);
    }

    /**
     * Adds student of <code>studentColor</code> to player's {@link SchoolBoard} dining room
     * by calling {@link Player#addStudentToDining(Color)}
     * <p>Generates {@link SchoolBoardChange} and sends it to all active players
     * by calling {@link Observable#notify(Object)}</p>
     * @param player  instance of player that wants to move the student to dining room
     * @param studentColor  color of student to add to the dining room
     * @throws NumOfStudentsExceeded  if there is no free space in dining room for that color
     * */
    public void addStudentToDining(Player player, Color studentColor) throws NumOfStudentsExceeded {
        player.addStudentToDining(studentColor);
        SchoolBoardChange schoolBoardChange = new SchoolBoardChange(player);
        notify(schoolBoardChange);
        //ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        //notify(exceptionChange);
    }

    /**Removes student of studentColor from player`s schoolBoard dining room
     * <p>Calls {@link GameBoard#removeStudentFromDining(Player, Color)}</p>
     * <p>Generates {@link SchoolBoardChange} and sends it to all active players
     * by calling {@link Observable#notify(Object)}</p>
     * @param player  player who`s {@link SchoolBoard} is to be taken student from
     * @param studentColor  color of the student to remove from dining
     * @throws StudentNotFoundException  if there is no student of such color in the dining room of the {@link SchoolBoard}
     * */
    public void removeStudentFromDining(Player player, Color studentColor) {
        player.removeStudentFromDining(studentColor);
        SchoolBoardChange schoolBoardChange = new SchoolBoardChange(player);
        notify(schoolBoardChange);
        //ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        //notify(exceptionChange);
    }

    public void getCoinFromBank() throws NoEnoughCoinsException {
        if (numOfCoins <= 0)
            throw new NoEnoughCoinsException("The Game board has no coins available");
        numOfCoins--;
        CoinsOfGameBoardChange coinsOfGameBoardChange = new CoinsOfGameBoardChange(numOfCoins);
        notify(coinsOfGameBoardChange);
        //ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        //notify(exceptionChange);
    }

    public void addCoinsToBank(int numOfCoins) throws NumOfCoinsExceeded {
        if (this.numOfCoins + numOfCoins > 20 )
            throw new NumOfCoinsExceeded();

        this.numOfCoins+=numOfCoins;
        CoinsOfGameBoardChange coinsOfGameBoardChange = new CoinsOfGameBoardChange(this.numOfCoins);
        notify(coinsOfGameBoardChange);
        //ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        //notify(exceptionChange);
    }

    /**
     * Puts or removes noEntryTile on an island by calling {@link Island#setNoEntry(boolean)}
     * <p>Generates {@link IslandChange} and sends it to all active players
     * by calling {@link Observable#notify(Object)}</p>
     * @param islandNumber  index of island in {@link #islands} to put or remove noEntryTile from
     * @param noEntry  true if noEntryTile needs to be put, false if noEntryTile needs to be removed
     * @throws IllegalArgumentException  if islandNumber index is out of bound in {@link #islands} list
     * @throws NoEntryException  if more than one <code>noEntryTile</code> is being placed on this island
     * */
    public void setNoEntry(int islandNumber, boolean noEntry) throws NoEntryException, NoEnoughEntryTilesException {
        if (islandNumber < 0 || islandNumber > islands.size()-1)
            throw new IllegalArgumentException("Error: invalid island number");

        islands.get(islandNumber).setNoEntry(noEntry);
        IslandChange islandChange = new IslandChange(islands.get(islandNumber), islandNumber);
        notify(islandChange);
    }

    /**
     * Calls {@link Bag#extractStudent()}
     * @return  color of the student extracted from bag
     * @throws NoEnoughStudentsException  if the bag is empty
     * */
    public Color getStudentFromBag() {
        return instanceOfBag.extractStudent();
    }

    /**
     * Buy a character for the {@link #currentPlayer} by calling {@link Character#buy()} method
     * <p>Assigns {@link #currentCharacter} attribute to the character bought</p>
     * <p>Generates {@link CharactersChange}, {@link ExceptionChange} and sends it to all active players
     * by calling {@link Observable#notify(Object)}</p>
     * @param characterNumber  index of character in {@link #playedCharacters} that {@link #currentPlayer} wants to buy
     * @throws IllegalArgumentException if index of character is out of bond in {@link #playedCharacters}
     * */
    public void buyCharacter(int characterNumber) {
        if (characterNumber < 0 || characterNumber >= 3)
            throw new IllegalArgumentException("Error: invalid island number");

        playedCharacters[characterNumber].buy();

        currentCharacter=playedCharacters[characterNumber];

        /*CharacterChange characterChange = new CharacterChange(currentCharacter, Arrays.stream(playedCharacters).toList().indexOf(currentCharacter));
        notify(characterChange);*/

        CharactersChange charactersChange = new CharactersChange(playedCharacters);
        notify(charactersChange);

        //buy character must send endOfChange because BuyCharacterPacket
        //and ActivateCharacterPacket are sent separately from client
        ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        notify(exceptionChange);

    }

    public void removeCoinsFromPlayer(Player player, int cost){
        player.removeCoins(cost);
        CoinsOfPlayerChange coinsOfPlayerChange = new CoinsOfPlayerChange(player);
        notify(coinsOfPlayerChange);
    }

    /**
     * <p>Gets coins from bank by calling {@link #getCoinFromBank()} and adds
     * them to the player by calling {@link Player#addCoins(int)}</p>
     * <p>Generates {@link CoinsOfPlayerChange} and sends them to all
     * active players by calling {@link Observable#notify(Object)}</p>
     * */
    public void addCoinsToPlayer(Player player, int coins){
        for (int i = 0; i < coins; i++) {
            getCoinFromBank();
            player.addCoins(1);
        }

        CoinsOfPlayerChange coinsOfPlayerChange = new CoinsOfPlayerChange(player);
        notify(coinsOfPlayerChange);
        //ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        //notify(exceptionChange);
    }

    /**
     * <ul>
     * <li>Calls {@link Character#setSelectedIslandNumber(int)} to execute setting for the {@link #currentCharacter}</li>
     * <li>Calls {@link Character#execute()} in order to execute character`s action</li>
     * <li>Generates {@link CharactersChange}, {@link EndOfChangesException} and sends it to all active players
     * by calling {@link Observable#notify(Object)}</li>
     * </ul>
     * @param islandNumber  index of the island that is needed in {@link #currentCharacter} setting
     * @throws IllegalArgumentException  if islandNumber index is out of bound in {@link #islands}
     * @throws UnsupportedOperationException  if currentCharacter doesn`t need island number setting
     * */
    public void activateCharacter(int islandNumber) {
        if (islandNumber < 0 || islandNumber > islands.size()-1)
            throw new IllegalArgumentException("Error: invalid island number");

        currentCharacter.setSelectedIslandNumber(islandNumber);
        currentCharacter.execute();
        //execute generates necessary modelChanges

        CharactersChange charactersChange = new CharactersChange(playedCharacters);
        notify(charactersChange);

        //every character activation is accompanied by execute which may change GameBoard state,
        //after execute we need to send endOfChanges so that client can go on
        ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        notify(exceptionChange);
    }

    /**
     * <ul>
     * <li>Calls {@link Character#setSelectedStudents(ArrayList)} to execute setting for the {@link #currentCharacter}</li>
     * <li>Calls {@link Character#setToBeSwappedStudents(ArrayList)} to execute setting for the {@link #currentCharacter}</li>
     * <li>Calls {@link Character#execute()} in order to execute character`s action</li>
     * <li>Generates {@link CharactersChange}, {@link EndOfChangesException} and sends it to all active players
     * by calling {@link Observable#notify(Object)}</li>
     * </ul>
     * @param toBeSwappedStudents  students needed for character execution in {@link Character#execute()}
     * @param selectedStudents  students needed for character execution in {@link Character#execute()}
     * @throws UnsupportedOperationException  if currentCharacter doesn`t need toBeSwapped and selectedStudents settings
     * */
    public void activateCharacter(ArrayList<Color> toBeSwappedStudents, ArrayList<Color> selectedStudents) {

        currentCharacter.setSelectedStudents(selectedStudents);
        currentCharacter.setToBeSwappedStudents(toBeSwappedStudents);
        currentCharacter.execute();

        /*CharacterChange characterChange = new CharacterChange(currentCharacter, Arrays.stream(playedCharacters).toList().indexOf(currentCharacter));
        notify(characterChange);*/

        CharactersChange charactersChange = new CharactersChange(playedCharacters);
        notify(charactersChange);

        //every character activation is accompanied by execute which may change GameBoard state
        //after execute we need to send endOfChanges so that client can go on
        ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        notify(exceptionChange);
    }

    /**
     * <ul>
     * <li>Calls {@link Character#setSelectedStudent(Color)} to execute setting for the {@link #currentCharacter}</li>
     * <li>Calls {@link Character#setSelectedIslandNumber(int)} to execute setting for the {@link #currentCharacter}</li>
     * <li>Calls {@link Character#execute()} in order to execute character`s action</li>
     * <li>Generates {@link CharactersChange}, {@link EndOfChangesException} and sends it to all active players
     * by calling {@link Observable#notify(Object)}</li>
     * </ul>
     * @param color  students color needed for character execution in {@link Character#execute()}
     * @param islandNumber  index of the island needed for character execution in {@link Character#execute()}
     * @throws UnsupportedOperationException  if currentCharacter doesn`t need color and islandNumber settings
     * */
    public void activateCharacter(Color color, int islandNumber) {
        if (islandNumber < 0 || islandNumber > islands.size()-1)
            throw new IllegalArgumentException("Error: invalid island number");

        currentCharacter.setSelectedStudent(color);
        currentCharacter.setSelectedIslandNumber(islandNumber);
        currentCharacter.execute();

        /*CharacterChange characterChange = new CharacterChange(currentCharacter, Arrays.stream(playedCharacters).toList().indexOf(currentCharacter));
        notify(characterChange);*/

        CharactersChange charactersChange = new CharactersChange(playedCharacters);
        notify(charactersChange);

        //every character activation is accompanied by execute which may change GameBoard state,
        //after execute we need to send endOfChanges so that client can go on
        ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        notify(exceptionChange);
    }

    /**
     * <ul>
     * <li>Calls {@link Character#setSelectedStudent(Color)} to execute setting for the {@link #currentCharacter}</li>
     * <li>Calls {@link Character#execute()} in order to execute character`s action</li>
     * <li>Generates {@link CharactersChange}, {@link EndOfChangesException} and sends it to all active players
     * by calling {@link Observable#notify(Object)}</li>
     * </ul>
     * @param color  students color needed for character execution in {@link Character#execute()}
     * @throws UnsupportedOperationException  if currentCharacter doesn`t need color setting
     * */
    public void activateCharacter(Color color) {
        currentCharacter.setSelectedStudent(color);
        currentCharacter.execute();

        /*CharacterChange characterChange = new CharacterChange(currentCharacter, Arrays.stream(playedCharacters).toList().indexOf(currentCharacter));
        notify(characterChange);*/

        CharactersChange charactersChange = new CharactersChange(playedCharacters);
        notify(charactersChange);

        //every character activation is accompanied by execute which may change GameBoard state,
        //after execute we need to send endOfChanges so that client can go on
        ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        notify(exceptionChange);
    }

    public Character getCurrentCharacter() {
        return currentCharacter;
    }

    public void setPlayedCharacters(Character[] characters) {
        playedCharacters = characters;
    }

    public void setCurrentCharacterToDefault(Character character) {
        currentCharacter=character;
        DefaultCharacterChange characterChange = new DefaultCharacterChange();
        notify(characterChange);
        /*ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        notify(exceptionChange);*/
    }

    public void addNoEntryTile() {
        for (Character c : playedCharacters) {
            c.addNoEntryTile();
        }
        CharactersChange charactersChange = new CharactersChange(playedCharacters);
        notify(charactersChange);
        //ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        //notify(exceptionChange);
    }

    public void addStudentToBag(Color studentColor){
        instanceOfBag.addStudent(studentColor);
    }

    /**
     * Executes {@link Player#setPlayedAssistant(int)} and generates {@link AssistantChange} and send it
     * to all active players by calling {@link Observable#notify(Object)}
     * */
    public void setPlayedAssistantRank(int assistantRank, Player player){
        player.setPlayedAssistant(assistantRank);
        AssistantChange assistantChange = new AssistantChange(player);
        notify(assistantChange);
    }

    public boolean checkBagEmpty(){
        return instanceOfBag.checkEmpty();
    }

    public TowerColor getTowersColorOnIsland(int numOfIsland){
        if (numOfIsland < 0 || numOfIsland > islands.size()-1)
            throw new IllegalArgumentException();

        return islands.get(numOfIsland).getTowersColor();
    }

    public int getNumOfTowersOnIsland(int numOfIsland){
        if (numOfIsland < 0 || numOfIsland > islands.size()-1)
            throw new IllegalArgumentException();

        return islands.get(numOfIsland).getNumOfTowers();
    }

    public int getPositionOfMotherNature(){
        return positionOfMotherNature;
    }

    public int getNumOfMergedIslands(int islandNumber){
        return islands.get(islandNumber).getNumOfIslands();
    }

    public List<Island> getIslands() {
        return islands;
    }

    public List<Cloud> getClouds() {
        return clouds;
    }

    public Character[] getPlayedCharacters() {
        return playedCharacters;
    }

    public boolean getAdvancedSettings(){
        return game.getAdvancedSettings();
    }

    /*TEST METHODS*/
    public int getNumOfClouds() { return clouds.size(); }

    public int getNumOfStudentsOnCloud(int numOfCloud){
        return clouds.get(numOfCloud).getStudentsColors().size();
    }

    public int getNumOfStudentsOnIsland(int numOfIsland, Color studentColor){
        if (numOfIsland < 0 || numOfIsland > islands.size()-1)
            throw new IllegalArgumentException();

        return islands.get(numOfIsland).getNumOfStudents(studentColor);
    }


    public boolean getNoEntryOnIsland(int numOfIsland){
        if (numOfIsland < 0 || numOfIsland > islands.size()-1)
            throw new IllegalArgumentException();

        return islands.get(numOfIsland).getNoEntry();
    }

    /*method that adds a tower or changes it`s color
    * note:not only one current player can conquer an island that MN stops on
    * but other players as well*/

    public void conquerIslandTEST(int numOfIsland, TowerColor towerColor){
        if (numOfIsland < 0 || numOfIsland > islands.size()-1)
            throw new IllegalArgumentException();

        try {
            islands.get(numOfIsland).setTowersColor(towerColor);
        }
        catch (UnsupportedOperationException e){
            islands.get(numOfIsland).addTower(towerColor);
        }

    }

    public void placeMotherNatureTEST(int numOfIsland){
        if (numOfIsland < 0 || numOfIsland > islands.size()-1)
            throw new IllegalArgumentException();

        positionOfMotherNature = numOfIsland;
    }

    public int getNumOfCoins(){
        return numOfCoins;
    }

    /**
     * Adds towers to player`s SchoolBoard by calling {@link Player#addTower()}
     * Generates {@link SchoolBoardChange} and sends it to all active players
     * by calling {@link Observable#notify(Object)}
     * @param numOfTowers  number of towers to add to player`s SchoolBoard
     * @param player  player that owns SchoolBoard that towers must be added to
     * */
    public void addTowersToPlayer(int numOfTowers, Player player) {
        for (int i = 0; i < numOfTowers; i++) {
            player.addTower();
        }
        SchoolBoardChange schoolBoardChange = new SchoolBoardChange(player);
        notify(schoolBoardChange);
    }

    /**
     * Calls {@link Player#removeTower()}
     * <p>Generates {@link SchoolBoardChange} and sends it to all active players
     * by calling {@link Observable#notify(Object)}</p>
     * @param numOfTowers  number of towers to remove from player`s SchoolBoard
     * @param player  instance of player who owns the schoolBoard to remove towers from
     * @throws NoEnoughTowersException  if there are no towers on the school board*/
    public void removeTowersFromPlayer(int numOfTowers, Player player) {
        for (int i = 0; i < numOfTowers; i++) {
            player.removeTower();
        }
        SchoolBoardChange schoolBoardChange = new SchoolBoardChange(player);
        notify(schoolBoardChange);
    }
}