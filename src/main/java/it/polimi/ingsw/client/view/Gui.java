package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.model.ClientGameBoard;
import it.polimi.ingsw.server.model.Color;
import java.awt.event.ActionEvent;
import java.util.List;

public class Gui extends View{

    private String string = null;
    private int chosenAction;
    private int num;
    private Color studentColor = null;
    private boolean done;

    //TODO: In each method, check if destination is compatible with moved element

    public synchronized void nameSelected(ActionEvent ae){ //called by student selection via mouse click
        done=true;
        notifyAll();
    }

    public synchronized void studentSelected(ActionEvent ae){ //called by student selection via mouse click
        chosenAction = 1;
        done=true;
        //studentColor=GUI.selectedColor
        notifyAll();
    }

    public synchronized void characterSelected(ActionEvent ae){ //called by character selection via mouse click
        chosenAction = 2;
        done=true;
        //character=GUI.selectedChar
        notifyAll();
    }

    public synchronized void numOfPlayerSelected(ActionEvent ae){
        num = ae.getID();
        done =true;
        notifyAll();
    }

    @Override
    public synchronized int askNumOfPlayers() {

        while(!done){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        done=false;

        return num;
    }

    @Override
    public String askAdvancedSettings() {
        return null;
    }

    @Override
    public void printMessage(String message) {

    }

    @Override
    public synchronized String askName() {

        while(!done){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        done=false;

        String name = ""; //TextField.getText()

        return name;
    }

    @Override
    public int askAssistant() {
        return 0;
    }

    @Override
    public int chooseActionStudent(boolean characterActivated) {



        while(!done){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return chosenAction;
    }

    @Override
    public Color askStudentColor() {

        while(!done){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        done=false;

        return studentColor;
    }

    @Override
    public int askStudentDestination() {
        return 0;
    }

    @Override
    public void showModel(ClientGameBoard gameBoard) {

    }

    @Override
    public int chooseActionMotherNature(boolean characterActivated) {
        return 0;
    }

    @Override
    public int askMotherNatureSteps() {
        return 0;
    }

    @Override
    public int chooseActionClouds(boolean characterActivated) {
        return 0;
    }

    @Override
    public int askCloudNumber() {
        return 0;
    }

    @Override
    public int askCharacterNumber() {
        return 0;
    }

    @Override
    public int askIslandNumber() {
        return 0;
    }

    @Override
    public void showLobby(List<String> userNames) {

    }
}
