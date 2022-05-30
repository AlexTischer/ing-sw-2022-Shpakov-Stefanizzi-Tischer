package server.model;

import exceptions.*;
import modelChange.*;
import server.controller.Game;
import utils.Observable;

import java.util.*;
import java.util.Random;

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

    /*creates 12 islands and puts MotherNature on a random island*/
    private GameBoard(){}

    /*Initializes instanceOfBag and clouds. Takes number of players and
    number of students to be in the bag( by default 130 ) */
    public void init(Game game, int numOfPlayers) {
        this.game = game;

        playedCharacters = new Character[3];

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

        assistantDeck = new AssistantFactory().getAssistantDeck();

        numOfCoins = 20;

        //initializing islands
        for(int i=0; i<12; i++){
            if(i!=positionOfMotherNature && i!=oppositeIsland(positionOfMotherNature)){
                islands.get(i).addStudent(instanceOfBag.extractStudent());
            }
        }

    }

    public static GameBoard getInstanceOfGameBoard() {
        if (instanceOfGameBoard == null)
            instanceOfGameBoard = new GameBoard();

        return instanceOfGameBoard;
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
        for (Cloud cloud : clouds) {
            for (int i = 0; i < cloud.getMaxNumOfStudents(); i++)
                cloud.addStudent(instanceOfBag.extractStudent());
        }
        CloudsChange cloudsChange = new CloudsChange(clouds);
        notify(cloudsChange);
        ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        notify(exceptionChange);
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

    public void moveMotherNature(int steps) {
        if (!currentCharacter.moveMotherNature(steps))
            throw new IllegalArgumentException("This number of steps is not allowed");

        positionOfMotherNature = (positionOfMotherNature + steps) % islands.size();
        if(positionOfMotherNature<0){
            positionOfMotherNature += islands.size();
        }
        MotherNatureChange motherNatureChange = new MotherNatureChange(positionOfMotherNature);
        notify(motherNatureChange);
        ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        notify(exceptionChange);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        CurrentPlayerChange currentPlayerChange = new CurrentPlayerChange(currentPlayer.getName());
        notify(currentPlayerChange);
    }

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

    public void addStudentToIsland(Color studentColor, int islandNumber){
        if (islandNumber < 0 || islandNumber > islands.size()-1)
            throw new IllegalArgumentException("Error: invalid island number");
        islands.get(islandNumber).addStudent(studentColor);
        IslandChange islandChange = new IslandChange(islands.get(islandNumber), islandNumber);
        notify(islandChange);
        ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        notify(exceptionChange);
    }

    public void useCloud(int cloudNumber) {
        for (Color color : clouds.get(cloudNumber).getStudentsColors()) {
            currentPlayer.addStudentToEntrance(color);
        }
        clouds.get(cloudNumber).removeStudents();
        CloudChange cloudChange = new CloudChange(clouds.get(cloudNumber), cloudNumber);
        notify(cloudChange);
        SchoolBoardChange schoolBoardChange = new SchoolBoardChange(currentPlayer);
        notify(schoolBoardChange);
        ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        notify(exceptionChange);
    }
    public int getNumOfIslands() {
        return islands.size();
    }

    /* Merge islands with same towerColor and returns boolean
    True if at least 2 islands have been merged, False otherwise
    Executed each time any island gets conquered*/

    public boolean mergeIslands() {

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
            ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
            notify(exceptionChange);
        }
        return oldNumOfIslands != islands.size();
    }
    /*returns the score of the player on particular island*/

    public int calculateInfluence(int islandNumber, Player player) {
        return currentCharacter.calculateInfluence(islands.get(islandNumber), islandNumber, player);
    }

    public void addProfessor(Player player, Color color) {
        player.addProfessor(color);
        SchoolBoardChange schoolBoardChange = new SchoolBoardChange(player);
        notify(schoolBoardChange);
        ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        notify(exceptionChange);
    }

    public void removeProfessor(Player player, Color color) {
        player.removeProfessor(color);
        SchoolBoardChange schoolBoardChange = new SchoolBoardChange(player);
        notify(schoolBoardChange);
        ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        notify(exceptionChange);
    }

    public void addStudentToEntrance(Player player, Color studentColor) {

        if (player.getNumOfStudentsInEntrance() < maxNumOfStudentsInEntrance) {
            player.addStudentToEntrance(studentColor);
            SchoolBoardChange schoolBoardChange = new SchoolBoardChange(player);
            notify(schoolBoardChange);
            ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
            notify(exceptionChange);
        }
        else
            throw new NumOfStudentsExceeded();
    }

    public void addTowersToIsland(int islandNumber, Player player){
        //method invoked by reassign island
        try {
            islands.get(islandNumber).setTowersColor(player.getTowerColor());
            IslandChange islandChange = new IslandChange(islands.get(islandNumber), islandNumber);
            notify(islandChange);
            ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
            notify(exceptionChange);
        }
        catch (UnsupportedOperationException e){
            /*there were no towers on island*/
            islands.get(islandNumber).addTower(player.getTowerColor());
        }
    }

    public void removeStudentFromEntrance(Player player, Color studentColor) {
        player.removeStudentFromEntrance(studentColor);
        SchoolBoardChange schoolBoardChange = new SchoolBoardChange(player);
        notify(schoolBoardChange);
        ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        notify(exceptionChange);
    }

    public void addStudentToDining(Player player, Color studentColor) throws NumOfStudentsExceeded {
        player.addStudentToDining(studentColor);
        SchoolBoardChange schoolBoardChange = new SchoolBoardChange(player);
        notify(schoolBoardChange);
        ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        notify(exceptionChange);
    }

    public void removeStudentFromDining(Player player, Color studentColor) {
        player.removeStudentFromDining(studentColor);
        SchoolBoardChange schoolBoardChange = new SchoolBoardChange(player);
        notify(schoolBoardChange);
        ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        notify(exceptionChange);
    }

    public void getCoin() throws NoEnoughCoinsException {
        if (numOfCoins <= 0)
            throw new NoEnoughCoinsException("The Game board has no coins available");
        numOfCoins--;
        CoinsOfGameBoardChange coinsOfGameBoardChange = new CoinsOfGameBoardChange(numOfCoins);
        notify(coinsOfGameBoardChange);
        ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        notify(exceptionChange);
    }

    public void addCoin() throws NumOfCoinsExceeded {
        if (numOfCoins >= 20)
            throw new NumOfCoinsExceeded();

        numOfCoins++;
        CoinsOfGameBoardChange coinsOfGameBoardChange = new CoinsOfGameBoardChange(numOfCoins);
        notify(coinsOfGameBoardChange);
        ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        notify(exceptionChange);
    }

    public void setNoEntry(int islandNumber, boolean noEntry) throws NoEntryException, NoEnoughEntryTilesException {
        if (islandNumber < 0 || islandNumber > islands.size()-1)
            throw new IllegalArgumentException("Error: invalid island number");

        islands.get(islandNumber).setNoEntry(noEntry);
        IslandChange islandChange = new IslandChange(islands.get(islandNumber), islandNumber);
        notify(islandChange);
        ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        notify(exceptionChange);
    }

    public Color getStudentFromBag() {
        return instanceOfBag.extractStudent();
    }

    public void buyCharacter(int characterNumber) {
        try {
            playedCharacters[characterNumber].buy();
            for (int i = 0; i < playedCharacters[characterNumber].getCost(); i++)
                addCoin();

            currentCharacter=playedCharacters[characterNumber];
        } catch (NoEnoughCoinsException e) {
            e.printStackTrace();
        }
        CharacterChange characterChange = new CharacterChange(currentCharacter, Arrays.stream(playedCharacters).toList().indexOf(currentCharacter));
        notify(characterChange);
        ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        notify(exceptionChange);
    }

    public void removeCoins(Player player, int cost){
        player.removeCoins(cost);
        CoinsOfPlayerChange coinsOfPlayerChange = new CoinsOfPlayerChange(player);
        notify(coinsOfPlayerChange);
        ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        notify(exceptionChange);
    }

    public void addCoins(Player player, int coins){
        player.addCoins(coins);
        CoinsOfPlayerChange coinsOfPlayerChange = new CoinsOfPlayerChange(player);
        notify(coinsOfPlayerChange);
        ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        notify(exceptionChange);
    }

    public void activateCharacter(int islandNumber) {
        if (islandNumber < 0 || islandNumber > islands.size()-1)
            throw new IllegalArgumentException("Error: invalid island number");

        currentCharacter.setSelectedIslandNumber(islandNumber);
        currentCharacter.execute();
        CharacterChange characterChange = new CharacterChange(currentCharacter, Arrays.stream(playedCharacters).toList().indexOf(currentCharacter));
        notify(characterChange);
        ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        notify(exceptionChange);
    }

    public void activateCharacter(ArrayList<Color> toBeSwappedStudents, ArrayList<Color> selectedStudents) {

        currentCharacter.setSelectedStudents(selectedStudents);
        currentCharacter.setToBeSwappedStudents(toBeSwappedStudents);
        currentCharacter.execute();
        CharacterChange characterChange = new CharacterChange(currentCharacter, Arrays.stream(playedCharacters).toList().indexOf(currentCharacter));
        notify(characterChange);
        ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        notify(exceptionChange);
    }

    public void activateCharacter(Color color, int islandNumber) {
        if (islandNumber < 0 || islandNumber > islands.size()-1)
            throw new IllegalArgumentException("Error: invalid island number");

        currentCharacter.setSelectedStudent(color);
        currentCharacter.setSelectedIslandNumber(islandNumber);
        currentCharacter.execute();
        CharacterChange characterChange = new CharacterChange(currentCharacter, Arrays.stream(playedCharacters).toList().indexOf(currentCharacter));
        notify(characterChange);
        ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        notify(exceptionChange);
    }

    public void activateCharacter(Color color) {
        currentCharacter.setSelectedStudent(color);
        currentCharacter.execute();
        CharacterChange characterChange = new CharacterChange(currentCharacter, Arrays.stream(playedCharacters).toList().indexOf(currentCharacter));
        notify(characterChange);
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
        CharacterChange characterChange = new CharacterChange(currentCharacter, -1);
        notify(characterChange);
        ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        notify(exceptionChange);
    }

    public void addNoEntryTile() {
        for (Character c : playedCharacters) {
            c.addNoEntryTile();
        }
        CharactersChange charactersChange = new CharactersChange(playedCharacters);
        notify(charactersChange);
        ExceptionChange exceptionChange = new ExceptionChange(new EndOfChangesException());
        notify(exceptionChange);
    }

    public void addStudentToBag(Color studentColor){
        instanceOfBag.addStudent(studentColor);
    }

    public void setPlayedAssistantRank(int assistantRank, Player player){
        player.setPlayedAssistantRank(assistantRank);
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

    public void addTowersToPlayer(int numOfTowers, Player player) {
        for (int i = 0; i < numOfTowers; i++) {
            player.addTower();
        }
    }

    public void removeTowersFromPlayer(int numOfIslands, Player player) {
        for (int i = 0; i < numOfIslands; i++) {
            player.removeTower();
        }
    }

    private int oppositeIsland(int n){
        n = n-6;
        if(n<0){
            return n+12;
        }
        else{
            return n;
        }
    }
}