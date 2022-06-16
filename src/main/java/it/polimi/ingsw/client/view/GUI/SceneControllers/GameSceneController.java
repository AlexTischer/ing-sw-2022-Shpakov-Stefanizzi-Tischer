package it.polimi.ingsw.client.view.GUI.SceneControllers;

import it.polimi.ingsw.server.model.Color;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.ArrayList;

public class GameSceneController extends SceneController{

    ArrayList<Button> buttons = new ArrayList<>();

    //TODO implement every method

    @FXML
    Button btn1, btn2, btn3, btn4, btn5, btn6;

    private boolean askingDone = false; //Boolean used to say to the controller thread that all the questions to the user have been answered TODO: change this description
    private int assistantRank;

    public synchronized void selectStudent(Color color){

    }

    public synchronized void selectCharacter(int index){

    }

    public synchronized void selectAssistant(int rank){
        askingDone=true;
        assistantRank= rank;
        for(Button b : buttons){
            b.setDisable(true);
        }
        notifyAll();
    }

    public synchronized void selectIsland(int index){

    }

    public synchronized void selectCloud(int index){

    }

    public synchronized void selectMotherNature(){}

    public synchronized void selectDining(){

    }

    public synchronized void cancelOperation(){

    }

    public synchronized void askAssistant(){
        askingDone=false;
        buttons.add(btn1);
        buttons.add(btn2);
        buttons.add(btn3);
        buttons.add(btn4);
        buttons.add(btn5);
        buttons.add(btn6);

        for(int i=0; i<6; i++){
            int finalI = i;
            Platform.runLater(() -> {
                buttons.get(finalI).setOnMouseClicked(event -> {selectAssistant(finalI+1);});
                buttons.get(finalI).setDisable(false);
            });

        }
    }

    public boolean isAskingDone(){
        return askingDone;
    }

    public int getAssistantRank(){
        return assistantRank;
    }


}
