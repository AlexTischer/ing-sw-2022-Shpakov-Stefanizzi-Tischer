import java.security.InvalidParameterException;
import java.util.ArrayList;

public class Player {
    private String name;
    private SchoolBoard schoolBoard;
    private int coins;
    private TowerColor towerColor;
    private AssistantType assistantType;
    private Assistant playedAssistant;

    /*Mike: why not creating an ArrayList?*/
    private Assistant[] assistants;

    /*Mike: Player must have getProfessorsColor method in order to check influence inside GameBoard
    * method must return Set<Color>*/

    /*Mike: Player must have getTowerColor method in order to check influence inside GameBoard*/

    public Player(String name, TowerColor towerColor, AssistantType assistantType, int numOfTowers){
        this.name=name;
        this.towerColor=towerColor;
        this.assistantType=assistantType;
        this.assistants = new Assistant[];
        this.coins = 0;
        schoolBoard = new SchoolBoard(this.towerColor, numOfTowers);
    }

    /*Mike: shouldn`t it throw exception if there are no students in entrance ?*/
    public void moveStudentToIsland(Color studentColor,Island island){
        schoolBoard.moveStudentToIsland(studentColor, island);
    }

    /*Mike: shouldn`t it throw exception if there are no students in entrance ?*/
    public void moveStudentToDining(Color studentColor){
        schoolBoard.moveStudentToDining(studentColor);
    }

    public void addStudentToEntrance(Color studentColor){
        schoolBoard.addStudentToEntrance(studentColor);
    }

    public Assistant getPlayedAssistant() {
        return playedAssistant;
    }

    public void setPlayedAssistant(int rank) throws InvalidParameterException {
        if(assistants[rank - 1] != null) {
            playedAssistant = assistants[rank - 1];
            assistants[rank - 1] = null;
        }
        else throw new InvalidParameterException();
    }

}
