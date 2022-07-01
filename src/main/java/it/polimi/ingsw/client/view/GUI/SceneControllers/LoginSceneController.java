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

    public boolean isLoginDone() {
        return loginDone;
    }

    @Override
    public void printErrorMessage(String message){
        errorMessage.setText(message);
        loginDone = false;
        Platform.runLater(() ->{
            textField.setVisible(true);
            button1.setVisible(true);
        });
    }

    public void cancelMessage(){
        errorMessage.setText("");
    }
}
