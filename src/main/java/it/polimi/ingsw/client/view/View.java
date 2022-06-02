package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.model.ClientGameBoard;
import it.polimi.ingsw.server.model.Color;

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

    public abstract void showModel(ClientGameBoard gameBoard);

    public abstract int chooseActionMotherNature(boolean characterActivated);
}
