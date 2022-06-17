package it.polimi.ingsw.client.view.GUI.SceneControllers;

import it.polimi.ingsw.client.model.ClientGameBoard;
import it.polimi.ingsw.client.view.GUI.LayoutObjects;
import it.polimi.ingsw.client.view.GUI.LayoutProperties;
import it.polimi.ingsw.server.model.Color;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class GameSceneController extends SceneController {


    @FXML
    private GridPane layoutGridPane;

    //TODO implement every method

    private boolean askingDone = false; //Boolean used to say to the controller thread that all the questions to the user have been answered TODO: change this description
    private int assistantRank;

    public synchronized void selectStudent(Color color){

    }

    public synchronized void selectCharacter(int index){

    }

    public synchronized void selectAssistant(int rank){
        askingDone=true;
        assistantRank= rank;
        notifyAll();
    }

    public synchronized void selectIsland(int index){

    }

    public synchronized void selectCloud(int index){

    }

    public synchronized void selectMotherNature(){}

    public synchronized void selectDining(){

    }

    public synchronized void cancelOperation(){

    }

    public synchronized void askAssistant(){

    }

    public boolean isAskingDone(){
        return askingDone;
    }

    public int getAssistantRank(){
        return assistantRank;
    }


    public void showModel(ClientGameBoard gameBoard) {

            placeSchoolBoards(gameBoard);
    }

    public void placeSchoolBoards(ClientGameBoard clientGameBoard){

        Platform.runLater(()-> {

            ArrayList<LayoutProperties> schoolboardsProperties = LayoutObjects.getSchoolBoards();

            for(int i=0; i<clientGameBoard.getPlayers().size();i++){

                ImageView imageView = loadImage(schoolboardsProperties.get(i).getPath(),
                                                schoolboardsProperties.get(i).getImageWidth(),
                                                schoolboardsProperties.get(i).getImageHeight());

                layoutGridPane.add(imageView,schoolboardsProperties.get(i).getxPos(),schoolboardsProperties.get(i).getyPos());
            }

        });



    }

    public ImageView loadImage(String path, int width, int height){
        Image image = new Image(getClass().getResourceAsStream(path));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);

        return imageView;
    }
}
