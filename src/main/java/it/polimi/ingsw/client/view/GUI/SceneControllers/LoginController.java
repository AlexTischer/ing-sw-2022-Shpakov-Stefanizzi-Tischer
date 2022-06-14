package it.polimi.ingsw.client.view.GUI.SceneControllers;

import javafx.fxml.FXML;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LoginController extends SceneController {

    @FXML
    private TextField textField;
    @FXML
    private Label lbl1, lbl2, lbl3, lbl4;
    private boolean loginDone;
    private String name = "PIPPO";

    public synchronized void sendName(){
        name = textField.getText();
        loginDone=true;
        notifyAll();
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
            if(names.get(0)!=null) {
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
}
