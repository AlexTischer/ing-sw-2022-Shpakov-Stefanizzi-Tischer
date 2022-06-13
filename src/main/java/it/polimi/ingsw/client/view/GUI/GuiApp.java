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

public class GuiApp extends Application {

    private static Scene scene;

    private static SceneController currentController;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(GuiApp.class.getResource("/SplashScreen.fxml"));
        Parent root = fxmlLoader.load();
        scene = new Scene(root);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
    public static void main() {
        launch();
    }

    public static void setRoot(String path) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(GuiApp.class.getResource(path));
        loader.setController(new ConfigurationController());
        currentController = loader.getController();
        scene.setRoot(loader.load());

    }
    public static SceneController getCurrentController(){
        return currentController;
    }
    //PROVA
    /*
    public static void setScene2() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(GuiApp.class.getResource("/Login.fxml"));
        loader.setController(new LoginController());
        scene.setRoot(loader.load());
    }

     */

}
