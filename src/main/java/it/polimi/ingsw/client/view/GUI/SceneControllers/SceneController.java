package it.polimi.ingsw.client.view.GUI.SceneControllers;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;

/**
 * This is the class from which all scene controller inherits
 */
public class SceneController {

    /**
     * Prints an error message
     * @param message
     */
    public void printErrorMessage(String message){
    }

    /**
     * Prints a generic message
     * @param message
     */
    public void printMessage(String message){

    }

    /**
     * Take all the objects of a FXML scene and resize that according to the size of the user screen
     * @param group containing all objects of the FXML
     */
    public void resizeScreen(Group group){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenHeight = screenSize.height;
        double scale = screenHeight/720.0;
        group.setScaleX(scale);
        group.setScaleY(scale);
    }

    /**
     * Create an {@link ImageView} containing an {@link Image} with the given image file and sized with the given height and width
     * @param path where the image is located
     * @param width of the image
     * @param height of the image
     * @return {@link ImageView}
     */
    public ImageView loadImageView(String path, int width, int height){

        Image image = new Image(path);

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);

        return imageView;
    }

    /**
     * Create an {@link Image} for the given image file
     * @param path where the image file is located
     * @return {@link Image}
     */
    public Image loadImage(String path){


        Image image = new Image(path);

        return image;
    }

}
