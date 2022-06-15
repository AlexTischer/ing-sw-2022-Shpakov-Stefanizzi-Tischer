package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.model.ClientGameBoard;
import it.polimi.ingsw.client.view.GUI.SceneControllers.ConfigurationController;
import it.polimi.ingsw.client.view.GUI.SceneControllers.LoginController;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.model.Color;
import javafx.application.Platform;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;

public class Gui extends View {

    private String string = null;
    private int chosenAction;
    private int num;
    private Color studentColor = null;
    private boolean done;
    private ConfigurationController configurationController;
    private LoginController loginController;


    public Gui(){
        synchronized (GuiApp.class){
            new Thread(()-> {
                GuiApp.main();
            }).start();
            try {
                GuiApp.class.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //TODO: In each method, check if destination is compatible with moved element

    public synchronized void nameSelected(ActionEvent ae){ //called by student selection via mouse click
        done=true;
        notifyAll();
    }

    public synchronized void studentSelected(ActionEvent ae){ //called by student selection via mouse click
        chosenAction = 1;
        done=true;
        //TODO studentColor=GUI.selectedColor
        notifyAll();
    }

    public synchronized void characterSelected(ActionEvent ae){ //called by character selection via mouse click
        chosenAction = 2;
        done=true;
        //TODO character=GUI.selectedChar
        notifyAll();
    }

    @Override
    public synchronized int askNumOfPlayers() {

        //TODO activate Configuration scene

        Platform.runLater(()->{
            synchronized (this) {
                try {
                    System.out.println("setting configuration page");
                    GuiApp.setRoot("/Configuration.fxml");
                    notifyAll();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        try {
            wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Platform.runLater(()-> {
            synchronized (this) {
                configurationController = (ConfigurationController) GuiApp.getCurrentController();
                System.out.println(configurationController.toString());
                this.notifyAll();
            }
        });

        while(configurationController==null){
            try {
                System.out.println("waiting configurationController");
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("configController received, notified");


        synchronized (configurationController) {
            while (!configurationController.isConfigurationDone()) {
                try {
                    configurationController.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return configurationController.getNumOfPlayers();

    }

    @Override
    public String askAdvancedSettings() {
        return configurationController.getAdvancedSettings();
    }

    @Override
    public void printMessage(String message) {
        Platform.runLater(() -> {
            GuiApp.getCurrentController().printErrorMessage(message);
        });
    }

    @Override
    public synchronized String askName() {

        synchronized (loginController) {
            while (!loginController.isLoginDone()) {
                try {
                    loginController.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return loginController.getName();
    }

    @Override
    public int askAssistant() {
        return 0;
    }

    @Override
    public int chooseActionStudent(boolean characterActivated) {

        while(!done){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return chosenAction;
    }

    @Override
    public Color askStudentColor() {

        while(!done){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        done=false;

        return studentColor;
    }

    @Override
    public int askStudentDestination() {
        return 0;
    }

    @Override
    public void showModel(ClientGameBoard gameBoard) {

    }

    @Override
    public int chooseActionMotherNature(boolean characterActivated) {
        return 0;
    }

    @Override
    public int askMotherNatureSteps() {
        return 0;
    }

    @Override
    public int chooseActionClouds(boolean characterActivated) {
        return 0;
    }

    @Override
    public int askCloudNumber() {
        return 0;
    }

    @Override
    public int askCharacterNumber() {
        return 0;
    }

    @Override
    public int askIslandNumber() {
        return 0;
    }

    @Override
    public synchronized void showLobby(List<String> userNames) {
        if(loginController==null) {
            Platform.runLater(() -> {
                try {
                    System.out.println("setting login page");
                    GuiApp.setRoot("/Login.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            Platform.runLater(() -> {
                synchronized (this) {
                    loginController = (LoginController) GuiApp.getCurrentController();
                    System.out.println(loginController.toString());
                    this.notifyAll();
                }
            });
            while (loginController == null) {
                try {
                    System.out.println("waiting loginController");
                    this.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("loginController received, notified");
        }

        loginController.update(userNames);
    }
    @Override
    public boolean askBoolean(String message) {
        return false;
    }
}
