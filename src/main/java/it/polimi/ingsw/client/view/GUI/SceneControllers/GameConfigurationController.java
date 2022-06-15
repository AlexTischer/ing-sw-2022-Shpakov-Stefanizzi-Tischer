package it.polimi.ingsw.client.view.GUI.SceneControllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.util.ResourceBundle;

public class GameConfigurationController extends SceneController implements Initializable {

    @FXML
    private CheckBox checkBox;
    @FXML
    private ChoiceBox choiceBox;

    private Integer[] players = {2,3,4};

    private boolean configurationDone;
    private String advancedSettings;
    private int numOfPlayers;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBox.getItems().addAll(players);
    }
    public synchronized void sendConfiguration(){
        //TODO advancedSettings = checkbox value
        //TODO numofPlayers = choicebox value
        if(checkBox.isSelected()){
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