import java.util.*;

public class GameBoard {

    private static GameBoard instanceOfGameBoard;
    private MotherNature motherNatureInstance;
    private Bag bagInstance;

    private List<Island> islands;
    private int positionOfMotherNature;
    private Island islandOfMotherNature;

    private List<Cloud> clouds;

    private List<Character> characters;/*size = 3*/

    private int numOfCoins;

    private GameBoard(){

    }

    public static GameBoard getInstanceOfGameBoard(){
        if (instanceOfGameBoard == null)
            instanceOfGameBoard = new GameBoard();

        return instanceOfGameBoard;
    }

    /*Maybe it`s better to incapsulate and not make it available from outside ?*/
    public MotherNature getMotherNatureInstance(){
        return motherNatureInstance;
    }

    public Bag getBagInstance(){
        return bagInstance;
    }

    public Island getIslandOfMotherNature() {
        return islandOfMotherNature;
    }

    public void moveMotherNature(int movement){
        positionOfMotherNature = (positionOfMotherNature + movement) % islands.toArray().length;
        islandOfMotherNature = islands.get(positionOfMotherNature);
    }

    public void getCoin(){
        numOfCoins--;
    }

    public void addCoin(Coin coin){
        numOfCoins++;
    }

    /*returns student popped from the bag*/
    public Student getStudent(){
        return null;
    }

    public Character[] getCharacters(){
        return (Character[]) characters.toArray();
    }
}
