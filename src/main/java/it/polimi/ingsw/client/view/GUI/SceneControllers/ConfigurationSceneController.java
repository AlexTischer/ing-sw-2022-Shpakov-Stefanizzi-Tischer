package it.polimi.ingsw.client.view.GUI.SceneControllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * <p>This class is the controller assigned to the configuration scene</p>
 * <ul>
 *     Contains:
 *     <li>{@link #checkBox} becomes enabled if user wants advanced settings</li>
 *     <li>{@link #choiceBox} allow user to choose number of players (2,3 or 4)</li>
 *     <li>{@link #group} are all the the FXML objects</li>
 * </ul>
 */
public class ConfigurationSceneController extends SceneController implements Initializable {

    @FXML
    private CheckBox checkBox;
    @FXML
    private ChoiceBox choiceBox;

    @FXML
    private Group group;

    private Integer[] players = {2,3,4};

    private boolean configurationDone;
    private String advancedSettings;
    private int numOfPlayers;

    /**
     * <p>Called when ConfigurationSceneController is loaded to the FXML.</p>
     * <p>It resize the scene according to user screen size and initializes the ChoiceBox</p>
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resizeScreen(group);
        choiceBox.getItems().addAll(players);
    }

    /**
     * Called by the "play!" button: sets all the attributes to the chosen option
     * and notifies Gui (that is waiting for a response)
     */
    public synchronized void sendConfiguration(){
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

    /**
     * Called by Gui
     * @return a boolean that let Gui know if the user has completed the configuration
     */
    public boolean isConfigurationDone() {
        return configurationDone;
    }

    /**
     * Called by Gui
     * @return a String (y or n) that indicates if the user has enabled advanced settings or not
     */
    public String getAdvancedSettings() {
        return advancedSettings;
    }

    /**
     * Called by Gui
     * @return the number of player set by the user
     */
    public int getNumOfPlayers() {
        return numOfPlayers;
    }

}