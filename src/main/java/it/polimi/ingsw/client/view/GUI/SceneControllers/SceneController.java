package it.polimi.ingsw.client.view.GUI.SceneControllers;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SceneController {

    public void printErrorMessage(String message){
    }

    public ImageView loadImage(String path, int width, int height){
        Image image = new Image(getClass().getResourceAsStream(path));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);

        return imageView;
    }

}
