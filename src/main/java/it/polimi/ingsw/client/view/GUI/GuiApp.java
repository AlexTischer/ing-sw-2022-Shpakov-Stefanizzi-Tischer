package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.view.GUI.SceneControllers.ConfigurationSceneController;
import it.polimi.ingsw.client.view.GUI.SceneControllers.LoginSceneController;
import it.polimi.ingsw.client.view.GUI.SceneControllers.SceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GuiApp extends Application {

    private static Scene scene;

    private static Map<String, SceneController> controllersMap = new HashMap<>() {{
        put(FXMLPaths.gameConfiguration, new ConfigurationSceneController());
        put(FXMLPaths.gameLogin, new LoginSceneController());
    }};
    private static SceneController currentController;

    @Override
    public void start(Stage stage) throws Exception {
        synchronized (GuiApp.class) {
            FXMLLoader fxmlLoader = new FXMLLoader(GuiApp.class.getResource(FXMLPaths.waitingForConfiguration));
            Parent root = fxmlLoader.load();
            scene = new Scene(root);
            stage.setTitle("Eriantys");
            stage.setScene(scene);
            stage.show();

            stage.setOnCloseRequest(event -> {
                event.consume();
                logout(stage);
            });

            GuiApp.class.notifyAll();
        }

    }
    public static void main() {
        launch();
    }

    public static void setRoot(String path) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(GuiApp.class.getResource(path));
        loader.setController(controllersMap.get(path));
        currentController = loader.getController();
        scene.setRoot(loader.load());
    }

    public static SceneController getCurrentController(){
        return currentController;
    }

    public void logout(Stage stage){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quit");
        alert.setHeaderText("Are you sure to quit?");

        if(alert.showAndWait().get() == ButtonType.OK) {
            stage.close();
            System.exit(0);
        }
    }

}
