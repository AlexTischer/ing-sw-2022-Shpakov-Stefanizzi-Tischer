package it.polimi.ingsw.client.view.GUI.SceneControllers;

import java.util.List;

public class LoginController {

    private boolean loginDone;
    private String name = "";

    public synchronized void sendName(){
        //TODO if(textField value != "") {name = textField value}
        notifyAll();
    }


    public String getName() {
        return name;
    }

    public void update(List<String> userNames){
        //TODO update lobby
    }
}
