package server.model;

import exceptions.*;
import modelChange.ModelChange;
import utils.Observable;

import java.util.*;
import java.util.Random;

public class GameBoard extends Observable<ModelChange> {

    private static GameBoard instanceOfGameBoard;
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
    public void init(int numOfPlayers) {
        playedCharacters = new Character[3];
        islands = new ArrayList<Island>(12);
        for (int i = 0; i < 12; i++) {
            islands.add(i, new Island());
        }

        positionOfMotherNature = new Random().nextInt(12);

        instanceOfBag = new Bag();

        clouds = new ArrayList<Cloud>(numOfPlayers);

        for (int i = 0; i < numOfPlayers; i++) {
            clouds.add(i, new Cloud(numOfPlayers == 3 ? 4 : 3));
        }

        maxNumOfStudentsInEntrance = numOfPlayers == 3 ? 9 : 7;

        assistantDeck = new AssistantFactory().getAssistantDeck();

        numOfCoins = 20;
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

    public void moveMotherNature(int steps) {
        if (!currentCharacter.moveMotherNature(steps))
            throw new IllegalArgumentException("This number of steps is not allowed");

        positionOfMotherNature = (positionOfMotherNature + steps) % islands.size();
        if(positionOfMotherNature<0){
            positionOfMotherNature += islands.size();
        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void moveStudentToIsland(Player player, Color studentColor, int islandNumber) {
        if (islandNumber < 0 || islandNumber > islands.size()-1)
            throw new IllegalArgumentException("Error: invalid island number");

        player.moveStudentToIsland(studentColor, islands.get(islandNumber));
        //TODO remove this method: use removeStudentFrom
    }

    public void addStudentToIsland(Color studentColor, int islandNumber){
        islands.get(islandNumber).addStudent(studentColor);
    }

    /*is invoked when character card is in use*/
    public void moveStudentToIsland(Color studentColor, int islandNumber) {
        if (islandNumber < 0 || islandNumber > islands.size()-1)
            throw new IllegalArgumentException("Error: invalid island number");

        islands.get(islandNumber).addStudent(studentColor);
    }

    public void useCloud(int cloudNumber) {

        for (Color color : clouds.get(cloudNumber).getStudentsColors()) {
            currentPlayer.addStudentToEntrance(color);
        }

        clouds.get(cloudNumber).removeStudents();

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

        return oldNumOfIslands != islands.size();
    }

    public void refillClouds() {

        for (Cloud cloud : clouds) {
            for (int i = 0; i < cloud.getMaxNumOfStudents(); i++)
                cloud.addStudent(instanceOfBag.extractStudent());
        }

    }

    /*returns the score of the player on particular island*/
    public int calculateInfluence(int islandNumber, Player player) {
        return currentCharacter.calculateInfluence(islands.get(islandNumber), islandNumber, player);
    }

    public void addProfessor(Player player, Color color) {
        player.addProfessor(color);
    }

    public void removeProfessor(Player player, Color color) {
        player.removeProfessor(color);
    }

    public void addStudentToEntrance(Player player, Color studentColor) {

        if (player.getNumOfStudentsInEntrance() < maxNumOfStudentsInEntrance)
            player.addStudentToEntrance(studentColor);
        else
            throw new NumOfStudentsExceeded();
    }

    public void addTowersToIsland(int islandNumber, Player player){
        try {
            islands.get(islandNumber).setTowersColor(player.getTowerColor());
        }
        catch (UnsupportedOperationException e){
            /*there were no towers on island*/
            islands.get(islandNumber).addTower(player.getTowerColor());
        }
    }

    public void removeStudentFromEntrance(Player player, Color studentColor) {
        player.removeStudentFromEntrance(studentColor);
    }

    public void addStudentToDining(Player player, Color studentColor) throws NumOfStudentsExceeded {
        player.addStudentToDining(studentColor);
    }

    public void removeStudentFromDining(Player player, Color studentColor) {
        player.removeStudentFromDining(studentColor);
    }

    public void getCoin() throws NoEnoughCoinsException {
        if (numOfCoins <= 0)
            throw new NoEnoughCoinsException("The Game board has no coins available");

        numOfCoins--;
    }

    public void addCoin() throws NumOfCoinsExceeded {
        if (numOfCoins >= 20)
            throw new NumOfCoinsExceeded();

        numOfCoins++;
    }

    public void setNoEntry(int islandNumber, boolean noEntry) throws NoEntryException, NoEnoughEntryTilesException {
        if (islandNumber < 0 || islandNumber > islands.size()-1)
            throw new IllegalArgumentException("Error: invalid island number");

        islands.get(islandNumber).setNoEntry(noEntry);
    }

    public Color getStudentFromBag() {
        return instanceOfBag.extractStudent();
    }

    public void buyCharacter(int characterNumber) {
        try {
            playedCharacters[characterNumber].buy();
            for (int i = 0; i < playedCharacters[characterNumber].getCost(); i++)
                addCoin();

            setCurrentCharacter(playedCharacters[characterNumber]);
        } catch (NoEnoughCoinsException e) {
            e.printStackTrace();
        }
    }

    public void removeCoins(Player player, int cost){
        player.removeCoins(cost);
    }

    public void addCoins(Player player, int coins){
        player.addCoins(coins);
    }


    public void activateCharacter(int islandNumber) {
        currentCharacter.setSelectedIslandNumber(islandNumber);
        currentCharacter.execute();
    }

    public void activateCharacter(ArrayList<Color> toBeSwappedStudents, ArrayList<Color> selectedStudents) {
        currentCharacter.setSelectedStudents(selectedStudents);
        currentCharacter.setToBeSwappedStudents(toBeSwappedStudents);
        currentCharacter.execute();
    }

    public void activateCharacter(Color color, int islandNumber) {
        currentCharacter.setSelectedStudent(color);
        currentCharacter.setSelectedIslandNumber(islandNumber);
        currentCharacter.execute();
    }

    public void activateCharacter(Color color) {
        currentCharacter.setSelectedStudent(color);
        currentCharacter.execute();
    }

    public Character getCurrentCharacter() {
        return currentCharacter;
    }

    public void setPlayedCharacters(int i, Character character) {
        playedCharacters[i]=character;
    }

    public void setCurrentCharacter(Character character) {
        currentCharacter=character;
    }

    public void addNoEntryTile() {
        for (Character c : playedCharacters) {
            c.addNoEntryTile();
        }
    }

    public void addStudentToBag(Color studentColor){
        instanceOfBag.addStudent(studentColor);
    }

    public boolean checkBagEmpty(){
        return instanceOfBag.checkEmpty();
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

    public int getNumOfTowersOnIsland(int numOfIsland){
        if (numOfIsland < 0 || numOfIsland > islands.size()-1)
            throw new IllegalArgumentException();

        return islands.get(numOfIsland).getNumOfTowers();
    }

    public TowerColor getTowersColorOnIsland(int numOfIsland){
        if (numOfIsland < 0 || numOfIsland > islands.size()-1)
            throw new IllegalArgumentException();

        return islands.get(numOfIsland).getTowersColor();
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

    public int getPositionOfMotherNature(){
        return positionOfMotherNature;
    }

    public void placeMotherNature(int numOfIsland){
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
}


