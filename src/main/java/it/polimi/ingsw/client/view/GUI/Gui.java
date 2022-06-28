package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.model.ClientGameBoard;
import it.polimi.ingsw.client.view.GUI.SceneControllers.ConfigurationSceneController;
import it.polimi.ingsw.client.view.GUI.SceneControllers.GameSceneController;
import it.polimi.ingsw.client.view.GUI.SceneControllers.LoginSceneController;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.Player;
import javafx.application.Platform;

import java.io.IOException;
import java.util.List;

import static it.polimi.ingsw.client.view.GUI.FXMLPaths.*;

public class Gui extends View {

    private ConfigurationSceneController configurationSceneController;
    private LoginSceneController loginSceneController;
    private GameSceneController gameSceneController;


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

    @Override
    public synchronized int askNumOfPlayers() {

        Platform.runLater(()->{
            synchronized (this) {
                try {
                    System.out.println("setting configuration page");
                    GuiApp.setRoot(ConfigurationScene.getPath());
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
                configurationSceneController = (ConfigurationSceneController) GuiApp.getCurrentController();
                System.out.println(configurationSceneController.toString());
                this.notifyAll();
            }
        });

        while(configurationSceneController == null){
            try {
                System.out.println("waiting configurationSceneController");
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("configController received, notified");


        synchronized (configurationSceneController) {
            while (!configurationSceneController.isConfigurationDone()) {
                try {
                    configurationSceneController.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return configurationSceneController.getNumOfPlayers();

    }

    @Override
    public String askAdvancedSettings() {
        return configurationSceneController.getAdvancedSettings();
    }

    @Override
    public void printErrorMessage(String message) {
        Platform.runLater(() -> {
            GuiApp.getCurrentController().printErrorMessage(message);
        });
    }

    public void printMessage(String message){
        Platform.runLater(()->{
            //GuiApp.getCurrentController().printMessage(message);
        });
    }

    @Override
    public synchronized String askName() {

        synchronized (loginSceneController) {
            while (!loginSceneController.isLoginDone()) {
                try {
                    loginSceneController.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return loginSceneController.getName();
    }

    @Override
    public synchronized int askAssistant() {

        gameSceneController.askAssistant();

        synchronized (gameSceneController) {
            while (!gameSceneController.isAskingDone()) {
                try {
                    System.out.println("waiting assistantRank");
                    gameSceneController.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        gameSceneController.setAskingDone(false);
        return gameSceneController.getAssistantRank();
    }


    @Override
    public synchronized Color askStudentColor() {

        if(!gameSceneController.isAskingDone()) {
            gameSceneController.askStudentColor();
        }

        synchronized (gameSceneController) {
            while (!gameSceneController.isAskingDone()) {
                try {
                    System.out.println("waiting studentColor");
                    gameSceneController.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        gameSceneController.setAskingDone(false);
        return gameSceneController.getStudentColor();
    }

    @Override
    public synchronized Color askStudentColorFromCharacter() {
        //TEST
        System.out.println("GUI: sono in askStudentColorFromCharacter");
        System.out.println("Asking Done è " + gameSceneController.isAskingDone());

        if(!gameSceneController.isAskingDone()) {
            gameSceneController.askStudentColorFromCharacter();
        }

        synchronized (gameSceneController) {
            //TEST
            System.out.println("GUI: askStudentColorFromCharacter: sono dentro synchronized ");
            while (!gameSceneController.isAskingDone()) {
                try {
                    System.out.println("waiting studentColor from character");
                    gameSceneController.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        //TEST
        System.out.println("GUI: sto uscendo da askStudentColorFromCharacter");
        gameSceneController.setAskingDone(false);
        return gameSceneController.getStudentColor();
    }

    @Override
    public synchronized Color askStudentColorFromBox() {
        //TEST
        System.out.println("GUI: sono in askStudentColorFromBox");
        System.out.println("Asking Done è " + gameSceneController.isAskingDone());

        if(!gameSceneController.isAskingDone()) {
            gameSceneController.askStudentColorFromBox();
        }

        synchronized (gameSceneController) {
            //TEST
            System.out.println("GUI: askStudentColorFromBox: sono dentro synchronized ");
            while (!gameSceneController.isAskingDone()) {
                try {
                    System.out.println("waiting studentColor from box");
                    gameSceneController.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        gameSceneController.hideAskColorBox();

        //TEST
        System.out.println("GUI: sto uscendo da askStudentColorFromBox");
        gameSceneController.setAskingDone(false);
        return gameSceneController.getStudentColor();
    }

    @Override
    public synchronized Color askStudentColorFromDiningRoom() {
        //TEST
        System.out.println("GUI: sono in askStudentColorFromDiningRoom");
        System.out.println("Asking Done è " + gameSceneController.isAskingDone());

        if(!gameSceneController.isAskingDone()) {
            gameSceneController.askStudentColorFromDiningRoom();
        }

        synchronized (gameSceneController) {
            //TEST
            System.out.println("GUI: askStudentColorFromDiningRoom: sono dentro synchronized ");
            while (!gameSceneController.isAskingDone()) {
                try {
                    System.out.println("waiting studentColor from diningRoom");
                    gameSceneController.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        //TEST
        System.out.println("GUI: sto uscendo da askStudentColorFromDiningRoom");
        gameSceneController.setAskingDone(false);
        return gameSceneController.getStudentColor();
    }

    @Override
    public synchronized int askStudentDestination() {
        if(!gameSceneController.isAskingDone()) {
            gameSceneController.askStudentDestination();

        }

        synchronized (gameSceneController) {
            while (!gameSceneController.isAskingDone()) {
                try {
                    System.out.println("waiting studentDestination");
                    gameSceneController.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }


        gameSceneController.setAskingDone(false);
        return gameSceneController.getStudentDestination();
    }

    @Override
    public synchronized void showModel(ClientGameBoard gameBoard) {
        if(gameSceneController ==null) {
            Platform.runLater(() -> {
                synchronized (this) {
                    try {
                        System.out.println("setting game page");
                        GuiApp.setRoot(GameScene.getPath());
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

            Platform.runLater(() -> {
                synchronized (this) {
                    gameSceneController = (GameSceneController) GuiApp.getCurrentController();
                    System.out.println(gameSceneController.toString());
                    this.notifyAll();
                }
            });
            while (gameSceneController == null) {
                try {
                    System.out.println("waiting gameSceneController");
                    this.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("gameSceneController received, notified");
        }

        gameSceneController.showModel(gameBoard);

    }

    @Override
    public synchronized int chooseActionStudent(boolean characterActivated) {
            if (!characterActivated) {
            gameSceneController.chooseActionStudent();

            synchronized (gameSceneController) {
                while (!gameSceneController.isAskingDone()) {
                    try {
                        System.out.println("GUI: chooseActionStudent: waiting studentAction");
                        gameSceneController.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                //TEST
                System.out.println("GUI: chooseActionStudent: exiting");
            }

            //TEST
                System.out.println("GUI: chooseActionStudent: sto ritornando scelta " + gameSceneController.getChosenAction());
            return gameSceneController.getChosenAction();
        }
        else {
                //TEST
                System.out.println("sono entrato nell'else return 1");
            return 1;
        }
    }

    @Override
    public synchronized int chooseActionMotherNature(boolean characterActivated) {
        if (!characterActivated) {
            gameSceneController.chooseActionMotherNature();

            synchronized (gameSceneController) {
                while (!gameSceneController.isAskingDone()) {
                    try {
                        System.out.println("waiting motherNatureAction");
                        gameSceneController.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }


            return gameSceneController.getChosenAction();
        }
        else {
            return 1;
        }
    }

    @Override
    public synchronized int askMotherNatureSteps() {
        if(!gameSceneController.isAskingDone()) {
            gameSceneController.askMotherNatureSteps();
        }

        synchronized (gameSceneController) {
            while (!gameSceneController.isAskingDone()) {
                try {
                    System.out.println("waiting MotherNatureSteps");
                    gameSceneController.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        gameSceneController.setAskingDone(false);
        return gameSceneController.getMotherNatureSteps();
    }

    @Override
    public synchronized int chooseActionClouds(boolean characterActivated) {

        if (!characterActivated) {
            gameSceneController.chooseActionCloud();


            synchronized (gameSceneController) {
                while (!gameSceneController.isAskingDone()) {
                    try {
                        System.out.println("waiting cloudAction");
                        gameSceneController.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }


            return gameSceneController.getChosenAction();
        }
        else {
            return 1;
        }
    }

    @Override
    public int askCloudNumber() {

        if(!gameSceneController.isAskingDone()) {
            gameSceneController.askCloudNumber();
        }

        synchronized (gameSceneController) {
            while (!gameSceneController.isAskingDone()) {
                try {
                    System.out.println("waiting Cloud Number");
                    gameSceneController.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return gameSceneController.getCloudNumber();
    }

    @Override
    public int askCharacterNumber() {

        if(!gameSceneController.isAskingDone()) {
            gameSceneController.askCharacterNumber();
        }


            while (!gameSceneController.isAskingDone()) {
                try {
                    System.out.println("waiting Character Number");
                    gameSceneController.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }


        gameSceneController.setAskingDone(false);
        return gameSceneController.getCharacterNumber();
    }

    @Override
    public int askIslandNumber() {
        //TEST
        System.out.println("GUI: sono in askIslandNumber, isAskingDone vale " + gameSceneController.isAskingDone());
        if(!gameSceneController.isAskingDone()) {
            gameSceneController.askIslandNumber();
        }

        synchronized (gameSceneController) {
            while (!gameSceneController.isAskingDone()) {
                try {
                    System.out.println("waiting StudentDestination");
                    gameSceneController.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        gameSceneController.setAskingDone(false);
        return gameSceneController.getIslandNumber();
    }

    @Override
    public synchronized void showLobby(List<String> userNames) {
        if(loginSceneController ==null) {
            Platform.runLater(() -> {
                try {
                    System.out.println("setting login page");
                    GuiApp.setRoot(LoginScene.getPath());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            Platform.runLater(() -> {
                synchronized (this) {
                    loginSceneController = (LoginSceneController) GuiApp.getCurrentController();
                    System.out.println(loginSceneController.toString());
                    this.notifyAll();
                }
            });
            while (loginSceneController == null) {
                try {
                    System.out.println("waiting loginSceneController");
                    this.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("loginSceneController received, notified");
        }

        loginSceneController.update(userNames);
    }

    @Override
    public boolean askBoolean(String message) {
        if(!gameSceneController.isAskingDone()) {
            gameSceneController.askBoolean();
        }

        synchronized (gameSceneController) {
            while (!gameSceneController.isAskingDone()) {
                try {
                    System.out.println("waiting Boolean");
                    gameSceneController.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        gameSceneController.setAskingDone(false);
        gameSceneController.hideYesNoButtons();
        return gameSceneController.getChoice();
    }

    @Override
    public void printEndGameMessage(String message) {
        printMessage(message);
        Platform.runLater(()-> GuiApp.endGame(message));
    }
}
