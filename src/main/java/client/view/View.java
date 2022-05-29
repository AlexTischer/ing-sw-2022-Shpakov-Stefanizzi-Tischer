package client.view;

import client.controller.ClientController;
import server.model.Color;

public abstract class View {
    private ClientController controller;

    public void attachController(ClientController controller){
        this.controller = controller;
    }

    public abstract int askNumOfPlayers();

    public abstract String askAdvancedSettings();
    public abstract void printMessage(String message);

    public abstract String askName();

    public void startTurn() {
        controller.startTurn();
    }

    public abstract int askAssistant();

    public abstract int chooseActionStudent(boolean characterActivated);

    public abstract Color askStudentColor();

    public abstract int askStudentDestination();
}
