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

    /*creates 12 islands and puts MotherNature on a random island*/
    private GameBoard(){
        islands = new ArrayList<Island>(12);
        for(int i = 0; i < 12; i++){
            islands.add(i, new Island());
        }

        positionOfMotherNature = new Random().nextInt(12);
    }

    /*Initializes instanceOfBag and clouds. Takes number of players and
    number of students to be in the bag( by default 120 ) and characterDeck to draw 3 characters*/
    public void init(int numOfPlayers, int numOfStudents, CharacterDeck characterDeck){

        instanceOfBag = new Bag(numOfStudents);

        clouds = new ArrayList<Cloud>(numOfPlayers);
        for (int i=0; i < numOfPlayers; i++){
            /*Cloud should accept numOfPlayers since it impacts on num of students that it contains*/
            clouds.add(i, new Cloud(/*numOfPlayers*/));
        }

        /*Maybe make 3 parametric value ?*/
        for (int i = 0; i < 3; i++){
            characters.add(i, characterDeck.popCharacter());
        }
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

    public void getCoin(){
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

    public void moveStudentToDining(Player player, Color studentColor){
        player.moveStudentToDining(studentColor);
    }

    public void useCloud(Player player, int cloudNumber){

        if(cloudNumber < 1 || cloudNumber > clouds.size())
            throw new IllegalArgumentException("Error: invalid cloudNumber");

        for(Color color: clouds.get(cloudNumber).getStudentsColors()){
            player.addStudentToEntrance(color);
        }

    }

    public int getNumOfIslands() {
        return islands.size();
    }

    /*how about renaming this method into mergeIslands that returns boolean ?*/
    public boolean checkMergeableIslands(){

        int oldNumOfIslands = islands.size();

        for(int i = 0; i < islands.size()-1; i++){

            if(islands.get(i).getTowersColor().equals(islands.get(i+1).getTowersColor())){
                /*if 2 tower colors are the same , then unify*/

                islands.get(i).mergeIsland(islands.get(i+1));

                /*Decrement index in order to check if I can merge other islands with the next one*/
                i--;
            }
        }

        return oldNumOfIslands != islands.size();
    }

    /*Mike: suggest creating max number of students attribute in a Cloud*/
    public void refillClouds(){
        if (numOfPlayers == 2 || numOfPlayers == 4){
            for(Cloud cloud: clouds){
                for (int i = 0; i < 3/*cloud.getMaxNumOfStudents*/; i++)
                    cloud.addStudents(instanceOfBag.extractStudent());
            }
        }
        if (numOfPlayers == 3 ){
            for(Cloud cloud: clouds){
                for (int i = 0; i < 4/*cloud.getMaxNumOfStudents*/; i++)
                    cloud.addStudents(instanceOfBag.extractStudent());
            }
        }
    }

    public int calculateInfluence(Player player){

        int score = 0;
        Island currentIsland = islands.get(positionOfMotherNature);

        for (Color color: player.getProfessorsColor()){
            score += currentIsland.getNumOfStudents(color);
        }

        if (player.getTowersColor().equals(currentIsland.getTowersColor())){
            score += currentIsland.getNumOfTowers();
        }

        return score;

    }
}
