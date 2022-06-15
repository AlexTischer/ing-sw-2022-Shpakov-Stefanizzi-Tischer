package it.polimi.ingsw.client.view.GUI.SceneControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class LoginController extends SceneController {

    @FXML
    private TextField textField;
    @FXML
    private Label lbl1, lbl2, lbl3, lbl4, errorMessage;
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
            printErrorMessage("The name field cannot be empty");
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

        for (Label l : labels){
            if(!names.isEmpty()) {
                l.setText(names.remove(0));
            }
            else {
                l.setText("");
            }
        }
    }

    public boolean isLoginDone() {
        return loginDone;
    }

    @Override
    public void printErrorMessage(String message){
        errorMessage.setText(message);
        loginDone = false;
    }

    public void cancelMessage(){

        errorMessage.setText("");

    }
}
