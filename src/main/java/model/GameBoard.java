package model;

import controller.CharacterDeck;
import controller.Player;
import exceptions.NoEnoughEntryTilesException;
import exceptions.NoEntryException;
import exceptions.NumOfStudentsExceeded;
import controller.Character;

import java.util.*;
import java.util.Random;

public class GameBoard {

    private static GameBoard instanceOfGameBoard;
    private Bag instanceOfBag;

    private List<Island> islands;
    private List<Cloud> clouds;

    private int positionOfMotherNature;
    private int numOfPlayers;
    private int numOfCoins;
    private int maxNumOfStudentsInEntrance;

    /*creates 12 islands and puts MotherNature on a random island*/
    private GameBoard(){}

    /*Initializes instanceOfBag and clouds. Takes number of players and
    number of students to be in the bag( by default 130 ) */
    public void init(int numOfPlayers, int numOfStudents){

        islands = new ArrayList<Island>(12);
        for(int i = 0; i < 12; i++){
            islands.add(i, new Island());
        }

        positionOfMotherNature = new Random().nextInt(12);

        instanceOfBag = new Bag(numOfStudents);

        clouds = new ArrayList<Cloud>(numOfPlayers);

        for (int i=0; i < numOfPlayers; i++){
            clouds.add(i, new Cloud(numOfPlayers==3 ? 4 : 3));
        }

        maxNumOfStudentsInEntrance = numOfPlayers== 3 ? 9 : 7;

    }

    public void init(int numOfPlayers){
        init(numOfPlayers, 130);
    }

    public static GameBoard getInstanceOfGameBoard(){
        if (instanceOfGameBoard == null)
            instanceOfGameBoard = new GameBoard();

        return instanceOfGameBoard;
    }

    /*method that will be invoked at the start to refill entrance
    of each player`s SchoolBoard*/
    public void refillEntrance(Player player){
        for (int i = 0; i < maxNumOfStudentsInEntrance; i++)
            player.addStudentToEntrance(instanceOfBag.extractStudent());
    }

    public void moveMotherNature(int steps, Character character){
        if(character.moveMotherNature(steps))
            positionOfMotherNature = (positionOfMotherNature + steps) % islands.size();
    }

    public void moveStudentToIsland( Player player, Color studentColor, int islandNumber ){
        if(islandNumber < 1 || islandNumber > islands.size())
            throw new IllegalArgumentException("Error: invalid island number");

        player.moveStudentToIsland(studentColor, islands.get(islandNumber-1));
    }

    public void moveStudentToIsland(Color studentColor, int islandNumber){
        if(islandNumber < 0 || islandNumber > islands.size()-1)
            throw new IllegalArgumentException("Error: invalid island number");

        islands.get(islandNumber).addStudent(studentColor);
    }

    public void moveStudentToDining(Player player, Color studentColor) throws NumOfStudentsExceeded {
        player.moveStudentToDining(studentColor);
    }

    public void useCloud(Player player, int cloudNumber){

        for(Color color: clouds.get(cloudNumber-1).getStudentsColors()){
            player.addStudentToEntrance(color);
        }

        clouds.get(cloudNumber-1).removeStudents();

    }

    public int getNumOfIslands() {
        return islands.size();
    }

    /* Merge islands with same towerColor and returns boolean
    True if at least 2 islands have been merged, False otherwise*/
    public boolean mergeIslands(){

        int oldNumOfIslands = islands.size();

        for(int i = 0; i < islands.size()-1; i++){

            /*if 2 tower colors are the same, then unify*/
            if(islands.get(i).getTowersColor().equals(islands.get(i+1).getTowersColor())){

                islands.get(i).mergeIsland(islands.get(i+1));

                /*if i unite island with the one that MotherNature stays on,
                then positionOfMotherNature should be decremented*/
                if(positionOfMotherNature == i+1)
                    positionOfMotherNature = i;

                /*Decrement index in order to check if I can merge the same islands with the next one*/
                i--;

            }
        }

        return oldNumOfIslands != islands.size();
    }

    public void refillClouds(){

        for(Cloud cloud: clouds){
            for (int i = 0; i < cloud.getMaxNumOfStudents(); i++)
                cloud.addStudent(instanceOfBag.extractStudent());
        }

    }

    /*returns the score of the current player on particular island
    * note: the character knows who is the current player*/
    public int calculateInfluence( int islandNumber, Character character ) throws NoEntryException {
        return character.calculateInfluence(islands.get(islandNumber-1), islandNumber);
    }

    public void addProfessor(Player player, Color color){
        player.addProfessor(color);
    }

    public void removeProfessor(Player player, Color color){
        player.removeProfessor(color);
    }


    public void addStudentToEntrance(Player player, Color studentColor){

        player.addStudentToEntrance(studentColor);
    }

    public void removeStudentFromEntrance(Player player, Color studentColor){
        player.removeStudentFromEntrance(studentColor);
    }

    public void removeStudentFromDining(Player player, Color studentColor){
        player.removeStudentFromDining(studentColor);
    }

    public void addStudentToDining(Player player, Color studentColor) throws NumOfStudentsExceeded {
        player.addStudentToDining(studentColor);
    }

    public void getCoin() {
        numOfCoins--;
    }

    public void addCoin(){
        numOfCoins++;
    }

    public void setNoEntry(int islandNumber, boolean noEntry) throws NoEntryException, NoEnoughEntryTilesException {
        if ( islandNumber < 1 || islandNumber > islands.size() )
            throw new IllegalArgumentException("Incorrect island number");

        islands.get(islandNumber-1).setNoEntry(noEntry);
    }

    public Color getStudentFromBag(){
        return instanceOfBag.extractStudent();
    }
}
