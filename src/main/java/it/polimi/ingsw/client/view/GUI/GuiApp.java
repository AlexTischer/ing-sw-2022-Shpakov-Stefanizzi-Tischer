package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.view.GUI.SceneControllers.ConfigurationController;
import it.polimi.ingsw.client.view.GUI.SceneControllers.LoginController;
import it.polimi.ingsw.client.view.GUI.SceneControllers.SceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GuiApp extends Application {

    private static Scene scene;

    private static Map<String, SceneController> controllersMap = new HashMap<>() {{
        put("/Configuration.fxml", new ConfigurationController());
        put("/Login.fxml", new LoginController());
    }};
    private static SceneController currentController;

    @Override
    public void start(Stage stage) throws Exception {
        synchronized (GuiApp.class) {
            FXMLLoader fxmlLoader = new FXMLLoader(GuiApp.class.getResource("/SplashScreen.fxml"));
            Parent root = fxmlLoader.load();
            scene = new Scene(root);
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();
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

}
