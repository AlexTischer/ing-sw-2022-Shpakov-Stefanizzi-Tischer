package it.polimi.ingsw.client.view.GUI;

/*public class FXMLPaths {

    public static String gameConfiguration = "/fxml/Configuration.fxml";
    public static String gameLogin = "/fxml/Login.fxml";
    public static String waitingForConfiguration = "/fxml/WaitingForConfiguration.fxml";
    public static String game = "/fxml/Game.fxml";


}*/

import it.polimi.ingsw.client.view.GUI.SceneControllers.ConfigurationSceneController;
import it.polimi.ingsw.client.view.GUI.SceneControllers.GameSceneController;
import it.polimi.ingsw.client.view.GUI.SceneControllers.LoginSceneController;
import it.polimi.ingsw.client.view.GUI.SceneControllers.SceneController;


public enum FXMLPaths{

    ConfigurationScene("/fxml/Configuration.fxml", new ConfigurationSceneController()),
    GameScene("/fxml/Game.fxml", new GameSceneController()),
    WaitingForConfigurationScene("/fxml/WaitingForConfiguration.fxml", new SceneController()),
    LoginScene("/fxml/Login.fxml", new LoginSceneController());

    private String path;
    private SceneController controller;

    public String getPath(){
        return path;
    }
    public SceneController getController(){
        return controller;
    }

    public static FXMLPaths get(String path) {
        for(FXMLPaths p : FXMLPaths.values()){
            if(p.path.equals(path)){
                return p;
            }
        }
        throw new IllegalArgumentException();
    }

    FXMLPaths(String path, SceneController controller){
        this.path = path;
        this.controller = controller;
    }

}