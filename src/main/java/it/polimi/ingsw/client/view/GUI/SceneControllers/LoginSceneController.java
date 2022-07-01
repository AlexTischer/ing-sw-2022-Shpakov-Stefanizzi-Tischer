package it.polimi.ingsw.client.view.GUI.SceneControllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>This class is the controller assigned to the login scene</p>
 * <ul>
 *     Contains:
 *     <li>{@link #textField} let the user insert its name</li>
 *     <li>{@link #errorMessage} shows error messages to the user (eg. username format not allowed, username already taken)</li>
 *     <li>{@link #labelList} shows name of the players that joined the lobby</li>
 *     <li>{@link #vBOXList} show assistant images of the players that joined the lobby</li>
 *     <li>{@link #button1} allow the user to send its username</li>
 *     <li>{@link #group} contains all the FXML objects</li>
 * </ul>
 */
public class LoginSceneController extends SceneController {

    @FXML
    private TextField textField;
    @FXML Label errorMessage;
    @FXML
    private ArrayList<Label> labelList;
    @FXML
    private ArrayList<VBox> vBOXList;
    @FXML
    private Button button1;

    @FXML
    private Group group;


    private boolean loginDone;
    private String name;


    /**
     * <p>Called by {@link #button1} to set {@link #name} and notify Gui that user has sent a name</p>
     * <p>Checks if the name is well formatted</p>
     */
    public synchronized void sendName(){

    String input = textField.getText();
        if (!input.contains(" ") && !input.isEmpty()){
            name = input;
            loginDone=true;
            notifyAll();
        }
        else {
            printErrorMessage("The name field cannot be empty or contain spaces");
        }
    }


    public String getName() {
        return name.toUpperCase();
    }

    /**
     * Called by Gui. Updates the lobby names
     * @param userNames list of usernames to insert into the lobby
     */
    public void update(List<String> userNames){

        Platform.runLater(()->{
            resizeScreen(group);
        });

        Platform.runLater(() -> textField.setOnKeyPressed((event -> {if(event.getCode() == KeyCode.ENTER) {sendName();}})));

        Platform.runLater(()-> {
            for(int i=0; i< userNames.size(); i++){
                labelList.get(i).setText(userNames.get(i));
                vBOXList.get(i).setVisible(true);
            }
        });

        if(loginDone){
            Platform.runLater(() ->{
                textField.setVisible(false);
                button1.setVisible(false);
            });
        }

    }

    /**
     * Called by Gui
     * @return if user has sent its name correctly
     */
    public boolean isLoginDone() {
        return loginDone;
    }

    /**
     * Set an error message to {@link #errorMessage} and enable {@link #textField} again
     * @param message
     */
    @Override
    public void printErrorMessage(String message){
        errorMessage.setText(message);
        loginDone = false;
        Platform.runLater(() ->{
            textField.setVisible(true);
            button1.setVisible(true);
        });
    }

    /**
     * Assigned to {@link #textField} when is clicked. It removes the error message
     */
    public void cancelMessage(){
        errorMessage.setText("");
    }
}
