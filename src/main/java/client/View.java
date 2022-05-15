package client;

import client.controller.ClientController;

public class View {
    private ClientController controller;

    public void attachController(ClientController controller){
        this.controller = controller;
    }
}
