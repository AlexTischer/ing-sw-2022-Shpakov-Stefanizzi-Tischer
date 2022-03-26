import exceptions.NumOfStudentsExceeded;

import java.util.*;
import java.util.Random;

public class GameBoard {

    private static GameBoard instanceOfGameBoard;
    private Bag instanceOfBag;

    private List<Island> islands;
    private List<Cloud> clouds;
    private List<Character> characters;

    private int positionOfMotherNature;
    private int numOfPlayers;
    private int numOfCoins;
    private int maxNumOfStudentsInEntrance;

    /*creates 12 islands and puts MotherNature on a random island*/
    private GameBoard(){
        islands = new ArrayList<Island>(12);
        for(int i = 0; i < 12; i++){
            islands.add(i, new Island());
        }

        positionOfMotherNature = new Random().nextInt(12);
    }

    /*Initializes instanceOfBag and clouds. Takes number of players and
    number of students to be in the bag( by default 130 ) and characterDeck to draw 3 characters*/
    public void init(int numOfPlayers, int numOfStudents, CharacterDeck characterDeck){

        if (numOfPlayers < 2 || numOfPlayers > 4 || numOfStudents < 0 ) {
            throw new IllegalArgumentException("Error: Invalid Arguments. NumOfPlayers is not in [2, 4] or numOfStudents is negative");
        }

        instanceOfBag = new Bag(numOfStudents);

        clouds = new ArrayList<Cloud>(numOfPlayers);

        for (int i=0; i < numOfPlayers; i++){
            clouds.add(i, new Cloud(numOfPlayers==3 ? 4 : 3));
        }

        /*Maybe make 3 parametric value ?*/
        for (int i = 0; i < 3; i++){
            characters.add(i, characterDeck.popCharacter());
        }

        switch (numOfPlayers){
            case 2: maxNumOfStudentsInEntrance = 7;
            case 3: maxNumOfStudentsInEntrance = 9;
            case 4: maxNumOfStudentsInEntrance = 7;
        }
    }

    public void init(int numOfPlayers, CharacterDeck characterDeck){
        init(numOfPlayers, 130, characterDeck);
    }

    /*method that will be invoked at the start to refill entrance
    of each player`s SchoolBoard*/
    public void refillEntrance(Player player){

        for (int i = 0; i < maxNumOfStudentsInEntrance; i++)
            player.addStudentToEntrance(instanceOfBag.extractStudent());

    }

    public static GameBoard getInstanceOfGameBoard(){
        if (instanceOfGameBoard == null)
            instanceOfGameBoard = new GameBoard();

        return instanceOfGameBoard;
    }

    public void moveMotherNature(int steps){
        if (steps < 1 || steps > 5)
            throw new IllegalArgumentException("You can move Mother Nature 1 island at least and 5 at most");

        positionOfMotherNature = (positionOfMotherNature + steps) % islands.size();
    }

    public void getCoin() {

        numOfCoins--;
    }

    public void addCoin(){
        numOfCoins++;
    }

    public void moveStudentToIsland(Player player, Color studentColor, int islandNumber ){
        if(islandNumber < 1 || islandNumber > islands.size())
            throw new IllegalArgumentException("Error: invalid island number");

        player.moveStudentToIsland(studentColor, islands.get(islandNumber));
    }

    public void moveStudentToDining(Player player, Color studentColor) throws NumOfStudentsExceeded {
        player.moveStudentToDining(studentColor);
    }

    public void useCloud(Player player, int cloudNumber){

        if(cloudNumber < 1 || cloudNumber > clouds.size())
            throw new IllegalArgumentException("Error: invalid cloudNumber");

        for(Color color: clouds.get(cloudNumber).getStudentsColors()){
            player.addStudentToEntrance(color);
        }

        clouds.get(cloudNumber).removeStudents();

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

                /*Decrement index in order to check if I can merge other islands with the next one*/
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

    /*returns the score of the player on particular island*/
    public int calculateInfluence(Player player, int numOfIsland){

        int score = 0;
        Island currentIsland = islands.get(numOfIsland);

        for (Color color: player.getProfessorsColor()){
            score += currentIsland.getNumOfStudents(color);
        }

        if (player.getTowerColor().equals(currentIsland.getTowersColor())){
            score += currentIsland.getNumOfTowers();
        }

        return score;
    }

    public int calculateInfluence(Player player){

        return this.calculateInfluence(player, positionOfMotherNature);
    }

    public void addProfessor(Player player, Color color){
        player.addProfessor(color);
    }

    public void removeProfessor(Player player, Color color){
        player.removeProfessor(color);
    }

}
