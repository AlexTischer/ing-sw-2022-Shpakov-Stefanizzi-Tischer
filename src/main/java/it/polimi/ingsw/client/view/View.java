package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.model.ClientGameBoard;
import it.polimi.ingsw.server.model.Color;

import java.util.List;

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

    public abstract int askMotherNatureSteps();

    public abstract int chooseActionClouds(boolean characterActivated);

    public abstract int askCloudNumber();

    public abstract int askCharacterNumber();

    public abstract int askIslandNumber();

    public ClientController getController(){
        return controller;
    }

    public abstract void showLobby(List<String> userNames);

    public abstract boolean askBoolean(String message);

    public abstract void printEndGameMessage(String message);

    public void printErrorMessage(String message) {
    }

    public abstract Color askStudentColorFromCharacter();
}
