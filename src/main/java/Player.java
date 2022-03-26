import exceptions.NumOfStudentsExceeded;

import java.security.InvalidParameterException;
import java.util.ArrayList;

public class Player {
    private String name;
    private SchoolBoard schoolBoard;
    private int coins;
    private TowerColor towerColor;
    private AssistantType assistantType;
    private Assistant playedAssistant;
    private Assistant[] assistants;


    public Player(String name, TowerColor towerColor, AssistantType assistantType, int numOfTowers){
        this.name=name;
        this.towerColor=towerColor;
        this.assistantType=assistantType;
        this.assistants = new Assistant[10];
        this.coins = 0;
        schoolBoard = new SchoolBoard(this.towerColor, numOfTowers);
    }

    public void moveStudentToIsland(Color studentColor,Island island){
        schoolBoard.moveStudentToIsland(studentColor, island);
    }

    public TowerColor getTowerColor() {
        return towerColor;
    }

    public ArrayList<Color> getProfessorsColor(){
        return schoolBoard.getProfessorsColor();
    }

    public void moveStudentToDining(Color studentColor) throws NumOfStudentsExceeded {
        schoolBoard.moveStudentToDining(studentColor);
    }

    public void addStudentToEntrance(Color studentColor){
        schoolBoard.addStudentToEntrance(studentColor);
    }

    public void addProfessor(Color color){
        schoolBoard.addProfessor(color);
    }

    public void removeProfessor(Color color){
        schoolBoard.removeProfessor(color);
    }

    public int getNumOfStudentsInDining(Color color){
        return schoolBoard.getNumOfStudentsInDining(color);
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
