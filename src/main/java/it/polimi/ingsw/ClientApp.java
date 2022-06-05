package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.GUI;
import javafx.application.Application;

import java.io.IOException;

public class ClientApp {

    public static void main(String[] args) {

        //Application.launch(GUI.class);

        Client client = new Client("192.168.188.73", 46582);

        try {
            client.run();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}