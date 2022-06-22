package it.polimi.ingsw.client.view.GUI.SceneControllers;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;

public class SceneController {

    public void printErrorMessage(String message){
    }

    public void resizeScreen(Group group){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenHeight = screenSize.height;
        double scale = screenHeight/720.0;
        group.setScaleX(scale);
        group.setScaleY(scale);
    }

    public ImageView loadImage(String path, int width, int height){
        Image image = new Image(getClass().getResourceAsStream(path));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);

        return imageView;
    }

}
