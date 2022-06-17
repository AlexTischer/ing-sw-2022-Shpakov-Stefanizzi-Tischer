package it.polimi.ingsw.client.view.GUI.SceneControllers;

import it.polimi.ingsw.server.model.Color;

import static it.polimi.ingsw.server.model.Color.*;

public class GameSceneController extends SceneController{

    //TODO implement every method

    private boolean askingDone = false; //Boolean used to say to the controller thread that all the questions to the user have been answered TODO: change this description
    private int assistantRank;
    private int chosenAction = 1;
    private int characterNumber;
    private int destination;
    private int cloudNumber;
    private int motherNatureSteps;
    private Color studentColor;
    private int positionOfMotherNature;

    public synchronized void selectStudent(Color studentColor){
        askingDone=true;
        chosenAction = 1;
        this.studentColor = studentColor;
        /*TODO: disable students*/
        notifyAll();
    }

    public synchronized void selectCharacter(int index){
        askingDone=true;
        chosenAction = 2;
        this.characterNumber = index;
        /*TODO: disable students*/
        notifyAll();
    }

    public synchronized void selectAssistant(int assistantRank){
        askingDone=true;
        this.assistantRank= assistantRank;
        /*TODO: disable assistant cards*/
        notifyAll();
    }

    public synchronized void selectDestination(int index){

    }

    public synchronized void selectCloud(int index){

    }

    public synchronized void selectMotherNature(int selectedIsland){
        askingDone=true;
        // TODO: this.motherNatureSteps = selectedIsland-currentIsland;
        /*TODO: disable assistant cards*/
        notifyAll();
    }

    public synchronized void cancelOperation(){

    }

    public synchronized void askAssistant(){
        askingDone=false;
        /*TODO: print: Select an assistant from your Deck*/
        /*TODO: enable Assistants and set onMouseClickAction to selectAssistant(index+1)*/
    }

    public void askStudentColor() {
        askingDone=false;
        /*TODO: print: Select a student*/
        /*TODO: enable students and set onMouseClickAction to selectStudent(color)*/
    }

    public void askIslandNumber() {
        askingDone=false;
        /*TODO: print: Select an island*/
        /*TODO: enable islands and set onMouseClickAction to selectDestination(index+1)*/
    }

    public void askStudentDestination() {
        askingDone=false;
        /*TODO: print: Select an island*/
        /*TODO: enable islands and set onMouseClickAction to selectDestination(index+1)*/
        /*TODO: enable diningRoom and set onMouseClickAction to selectDestination(0)*/
    }

    public void askMotherNatureSteps() {
        askingDone=false;
        /*TODO: print: Select the island you want to move MotherNature to*/
        /*TODO: enable assistantSteps islands after currentIsland and set onMouseClickAction to selectMotherNature(index)*/
    }

    public void askCloudNumber() {
        askingDone=false;
        /*TODO: print: Select a cloud*/
        /*TODO: enable clouds and set onMouseClickAction to selectCloud(index+1)*/
    }

    public void askCharacterNumber() {
        askingDone=false;
        /*TODO: print: Select a Character*/
        /*TODO: enable clouds and set onMouseClickAction to selectCharacter(index+1)*/
    }

    public void chooseActionStudent() {
        /*TODO: print: Select a student from your entrance to move it, or select a Character to buy and use it*/
        /*TODO: enable Entrance students and set onMouseClickAction to selectStudent(color)*/
        /*TODO: enable Characters and set onMouseClickAction to selectCharacter(index+1)*/
    }

    public void chooseActionMotherNature() {
        /*TODO: print: Select a student from your entrance to move it, or select a Character to buy and use it*/
        /*TODO: enable Entrance students and set onMouseClickAction to selectStudent(color)*/
        /*TODO: enable Characters and set onMouseClickAction to selectCharacter(index+1)*/
    }

    public void chooseActionCloud() {
        /*TODO: print: Select a cloud to use it, or select a Character to buy and use it*/
        /*TODO: enable clouds and set onMouseClickAction to selectCloud(index+1)*/
        /*TODO: enable Characters and set onMouseClickAction to selectCharacter(index+1)*/
    }

    public boolean isAskingDone(){
        return askingDone;
    }

    public int getAssistantRank(){
        return assistantRank;
    }

    public int getChosenAction() {
        return chosenAction;
    }

    public Color getStudentColor() {
        return RED;
    }

    public int getMotherNatureSteps() {
        return motherNatureSteps;
    }

    public int getCloudNumber(){
        return cloudNumber;
    }

    public int getCharacterNumber() {
        return characterNumber;
    }

    public int getIslandNumber() {
        return destination;
    }

    public int getStudentDestination() {
        return destination;
    }

    public void showModel() {
    }
}
