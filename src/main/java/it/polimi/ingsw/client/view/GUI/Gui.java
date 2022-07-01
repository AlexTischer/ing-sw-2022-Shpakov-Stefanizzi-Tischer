package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.model.ClientGameBoard;
import it.polimi.ingsw.client.view.GUI.SceneControllers.ConfigurationSceneController;
import it.polimi.ingsw.client.view.GUI.SceneControllers.GameSceneController;
import it.polimi.ingsw.client.view.GUI.SceneControllers.LoginSceneController;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.model.Color;
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
                this.notifyAll();
            }
        });

        while(configurationSceneController == null){
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }



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
            GuiApp.getCurrentController().printMessage(message);
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


        if(!gameSceneController.isAskingDone()) {
            gameSceneController.askStudentColorFromCharacter();
        }

        synchronized (gameSceneController) {

            while (!gameSceneController.isAskingDone()) {
                try {
                    gameSceneController.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }


        gameSceneController.setAskingDone(false);
        return gameSceneController.getStudentColor();
    }

    public synchronized Color askStudentColorFromEntrance() {

        if(!gameSceneController.isAskingDone()) {
            gameSceneController.askStudentColorFromEntrance();
        }

        synchronized (gameSceneController) {

            while (!gameSceneController.isAskingDone()) {
                try {
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
    public synchronized Color askStudentColorFromBox() {


        if(!gameSceneController.isAskingDone()) {
            gameSceneController.askStudentColorFromBox();
        }

        synchronized (gameSceneController) {

            while (!gameSceneController.isAskingDone()) {
                try {
                    gameSceneController.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        gameSceneController.hideAskColorBox();


        gameSceneController.setAskingDone(false);
        return gameSceneController.getStudentColor();
    }

    @Override
    public synchronized Color askStudentColorFromDiningRoom() {


        if(!gameSceneController.isAskingDone()) {
            gameSceneController.askStudentColorFromDiningRoom();
        }

        synchronized (gameSceneController) {

            while (!gameSceneController.isAskingDone()) {
                try {
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
    public synchronized int askStudentDestination() {
        if(!gameSceneController.isAskingDone()) {
            gameSceneController.askStudentDestination();

        }

        synchronized (gameSceneController) {
            while (!gameSceneController.isAskingDone()) {
                try {
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
                    this.notifyAll();
                }
            });
            while (gameSceneController == null) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
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
    public synchronized int chooseActionMotherNature(boolean characterActivated) {
        if (!characterActivated) {
            gameSceneController.chooseActionMotherNature();

            synchronized (gameSceneController) {
                while (!gameSceneController.isAskingDone()) {
                    try {
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
                    gameSceneController.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        gameSceneController.setAskingDone(false);
        return gameSceneController.getCloudNumber();
    }

    @Override
    public int askCharacterNumber() {

        gameSceneController.setAskingDone(false);
        return gameSceneController.getCharacterNumber();
    }

    @Override
    public int askIslandNumber() {

        if(!gameSceneController.isAskingDone()) {
            gameSceneController.askIslandNumber();
        }

        synchronized (gameSceneController) {
            while (!gameSceneController.isAskingDone()) {
                try {
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
                    GuiApp.setRoot(LoginScene.getPath());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            Platform.runLater(() -> {
                synchronized (this) {
                    loginSceneController = (LoginSceneController) GuiApp.getCurrentController();
                    this.notifyAll();
                }
            });
            while (loginSceneController == null) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
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
