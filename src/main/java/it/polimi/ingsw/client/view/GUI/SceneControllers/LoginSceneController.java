package it.polimi.ingsw.client.view.GUI.SceneControllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;


import java.util.ArrayList;
import java.util.List;

public class LoginSceneController extends SceneController {

    @FXML
    private TextField textField;
    @FXML
    private Label lbl1, lbl2, lbl3, lbl4, errorMessage;
    @FXML
    private Button button1;
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
        ArrayList<Label> labels = new ArrayList<Label>();

        labels.add(lbl1);
        labels.add(lbl2);
        labels.add(lbl3);
        labels.add(lbl4);

        List<String> names = new ArrayList<>(userNames);

        for (int i=0; i<names.size(); i++){
            int finalI = i;
            Platform.runLater(() -> labels.get(finalI).setText(names.get(finalI)));
        }
        for (int i=names.size(); i< labels.size(); i++){
            int finalI = i;
            Platform.runLater(() -> labels.get(finalI).setText(""));
        }

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
