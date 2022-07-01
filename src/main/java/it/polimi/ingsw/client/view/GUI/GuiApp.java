package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.view.GUI.SceneControllers.SceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

import static it.polimi.ingsw.client.view.GUI.FXMLPaths.WaitingForConfigurationScene;


/**
 * <p>This class is responsible for starting the stage of JavaFX and manage scenes</p>
 * <ul>
 *     Contains:
 *     <li>{@link #scene} attribute, to save the current scene </li>
 *     <li>{@link #stage} attribute, to save the current stage </li>
 *     <li>{@link #currentController} attribute, to save the current Scene Controller </li>
 * </ul>
 */
public class GuiApp extends Application {

    private static Scene scene;
    private static SceneController currentController;
    private static Stage stage;

    /**
     * Starts the JavaFx stage with the default scene "Waiting for configuration"
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        synchronized (GuiApp.class) {
            this.stage=stage;
            FXMLLoader fxmlLoader = new FXMLLoader(GuiApp.class.getResource(WaitingForConfigurationScene.getPath()));
            Parent root = fxmlLoader.load();
            scene = new Scene(root);
            stage.setTitle("Eriantys");
            stage.setScene(scene);
            stage.getIcons().add(new Image("images/misc/Icon.jpg"));

            stage.setMaximized(true);
            //stage.setFullScreen(true);
            stage.setResizable(false);
            stage.setFullScreenExitHint("");
            //stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

            stage.show();

            stage.setOnCloseRequest(event -> {
                event.consume();
                logout(stage);
            });

            GuiApp.class.notifyAll();
        }

    }

    /**
     * Called by {@link Gui} to invoke GuiApp {@link #start(Stage)} method
     */
    public static void main() {
        launch();
    }

    /**
     * Changes the scene
     * @param path for the fxml of the scene
     * @throws IOException
     */
    public static void setRoot(String path) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(GuiApp.class.getResource(path));
        loader.setController(FXMLPaths.get(path).getController());
        currentController = loader.getController();
        scene.setRoot(loader.load());
    }

    /**
     *
     * @return current controller that is managing current scene
     */
    public static SceneController getCurrentController(){
        return currentController;
    }

    /**
     * Prompts an Alert that ask the user if it is certain to quit
     * @param stage the current stage
     */
    private void logout(Stage stage){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quit");
        alert.setHeaderText("Are you sure to quit?");

        if(alert.showAndWait().get() == ButtonType.OK) {
            stage.close();
            System.exit(0);
        }
    }

    /**
     * Prompts an Alert to inform the user that the game is ended, showing the result and asking if he wants to quit
     * @param message
     */
    public static void endGame(String message){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("The match is over");
        alert.setHeaderText(message + "\n Do you want to quit?");

        if(alert.showAndWait().get() == ButtonType.OK) {
            stage.close();
            System.exit(0);
        }
    }





}
