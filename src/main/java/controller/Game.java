package controller;

import exceptions.NoEnoughCoinsException;
import exceptions.NoEnoughEntryTilesException;
import exceptions.NoEnoughStudentsException;
import exceptions.NoEntryException;
import model.*;

import java.security.InvalidParameterException;
import java.util.ArrayList;

public class Game {
    private Game instanceOfGame;
    private GameBoard gameBoard;
    protected ArrayList<Player> players;
    protected Player currentPlayer;
    private Character currentCharacter;
    private Character playedCharacters[];
    private ArrayList<Assistant> playedAssistant;
    private boolean advancedSettings;

    private Game(){}

    public void init(ArrayList<String> playersNames, boolean advancedSettings, AssistantDeck assistantDeck, CharacterDeck characterDeck){
        switch (playersNames.size()){
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
        this.advancedSettings=advancedSettings;
        if (advancedSettings){
            for (int i = 0; i < 3; i++) {
                playedCharacters[i]=characterDeck.popCharacter();
            }
            currentCharacter=new Character();
        }
        else {
            currentCharacter=new Character();
        }

    }

    public void moveStudentToIsland(Color studentColor, int islandNumber){
        gameBoard.moveStudentToIsland(currentPlayer, studentColor, islandNumber);
    }

    public void moveStudentToDining(Color studentColor){
        removeStudentFromEntrance(studentColor);
        addStudentToDining(currentPlayer, studentColor);
    }

    protected void addStudentToEntrance(Player player){
        gameBoard.addStudentToEntrance(player, getStudent());
    }

    protected void addStudentToEntrance(Player player, Color studentColor){
        gameBoard.addStudentToEntrance(player, studentColor);
    }

    protected void addStudentToDining(Player player, Color studentColor){
        gameBoard.addStudentToDining(player, studentColor);
        reassignProfessor(studentColor);
    }

    protected void removeStudentFromEntrance(Color studentColor){
        gameBoard.removeStudentFromEntrance(currentPlayer, studentColor);
    }

    protected void removeStudentFromDining(Player player, Color studentColor){
        gameBoard.removeStudentFromDining(player, studentColor);
    }

    protected Color getStudent(){
        return gameBoard.getStudentFromBag();
    }

    protected void calculateInfluence(int islandNumber){
        try {
            gameBoard.calculateInfluence(islandNumber, currentCharacter);
        }
        catch (NoEntryException e){
            for (Character c : playedCharacters) {
                c.addNoEntryTile();
            }
        }
    }

    protected void reassignProfessor(Color professorColor){
        currentCharacter.reassignProfessor(professorColor);
    }

    protected void setNoEntry(int islandNumber, boolean noEntry){
        try {
            gameBoard.setNoEntry(islandNumber, noEntry);
        }

        catch (NoEntryException e){
            }
    }

    public void moveMotherNature(int steps){
        gameBoard.moveMotherNature(steps, currentCharacter);
    }

    public void buyCharacter(int characterNumber){
            try {
                currentPlayer.removeCoins(playedCharacters[characterNumber].getCost());
                playedCharacters[characterNumber].buy();
                this.currentCharacter=playedCharacters[characterNumber];
            }
            catch (NoEnoughCoinsException e) {
                /* Catch*/
            }
    }

    public void activateCharacter(int islandNumber){
        currentCharacter.setSelectedIslandNumber(islandNumber);
        currentCharacter.execute();
    }

    public void activateCharacter(ArrayList<Color> toBeSwappedStudents, ArrayList<Color> selectedStudents){
        currentCharacter.setSelectedStudents(selectedStudents);
        currentCharacter.setToBeSwappedStudents(toBeSwappedStudents);
        currentCharacter.execute();
    }

    public void activateCharacter(Color color, int islandNumber) throws NoEnoughStudentsException {
        currentCharacter.setSelectedStudent(color);
        currentCharacter.setSelectedIslandNumber(islandNumber);
        currentCharacter.execute();
    }

    public void activateCharacter(Color color){
        currentCharacter.setSelectedStudent(color);
        currentCharacter.execute();
    }

    public void useCloud(int cloudNumber){
        gameBoard.useCloud(currentPlayer, cloudNumber);
    }

    public void useAssistant(int assistantRank){}
    public void newTurn(){}
    public void newRound(){}


}