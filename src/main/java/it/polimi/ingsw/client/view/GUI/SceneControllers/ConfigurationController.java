package it.polimi.ingsw.client.view.GUI.SceneControllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class ConfigurationController extends SceneController{

    @FXML
    private Checkbox checkBox;

    @FXML
    private ChoiceBox choiceBox;

    private boolean configurationDone;
    private String advancedSettings;
    private int numOfPlayers;

    public synchronized void sendConfiguration(){
        //TODO advancedSettings = checkbox value
        //TODO numofPlayers = choicebox value
        if(checkBox.getState()){
            advancedSettings = "true";
        }
        else {
            advancedSettings = "false";
        }
        numOfPlayers=(Integer) choiceBox.getValue();
        configurationDone=true;
        notifyAll();
    }

    public boolean isConfigurationDone() {
        return configurationDone;
    }

    public String getAdvancedSettings() {
        return advancedSettings;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }
}