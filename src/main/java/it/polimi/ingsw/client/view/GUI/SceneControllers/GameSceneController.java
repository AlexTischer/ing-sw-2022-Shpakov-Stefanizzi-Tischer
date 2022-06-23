package it.polimi.ingsw.client.view.GUI.SceneControllers;

import it.polimi.ingsw.client.model.ClientGameBoard;
import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.SchoolBoard;
import it.polimi.ingsw.server.model.TowerColor;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;


import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.server.model.Color.*;

public class GameSceneController extends SceneController {

    //TODO implement every method


    //JAVAFX
    @FXML
    private ArrayList<Group> playersList;
    @FXML
    private Group root, islands, assistants, clouds, characters;
    @FXML
    private Pane zoomedAssistant;
    @FXML
    private Text characterDescription, dialogText;


    //JAVAFX children indexes

    //player group
    int children_assistantPlayed = 5;
    int children_nameLabel = 6;
    int children_imageOfCoin = 7;
    int children_numOfCoins = 8;


    //schoolboard
    int children_imageOfSchoolBoard = 0;
    int children_entrance = 1;
    int children_diningRoom = 2;
    int children_professors = 3;
    int children_towerTable = 4;


    private boolean askingDone = false; //Boolean used to say to the controller thread that all the questions to the user have been answered TODO: change this description
    private int assistantRank;
    private int chosenAction = 1;
    private int characterNumber;
    private int destination;
    private int cloudNumber;
    private int motherNatureSteps;
    private Color studentColor;
    private int positionOfMotherNature;
    private boolean choice;
    private int numOfIslands = 12;
    private boolean firstModelShown = false;



    public void showModel(ClientGameBoard gameBoard){
        if(!firstModelShown){
            showFirstModel(gameBoard);
            firstModelShown = true;
        }
        else{
            updateModel(gameBoard);
        }
    }

    private void showFirstModel(ClientGameBoard gameBoard) {
        
        Platform.runLater(()-> {

            resizeScreen(root);

            //setting playground according to num of players

            //TODO: adding instead of removing
            for(int i=4; i>gameBoard.getPlayers().size(); i--){

                //removing unnecessary schoolboards
                playersList.get(i-1).getChildren().remove(0,playersList.get(i-1).getChildren().size());

                //removing unnecessary clouds
                clouds.getChildren().remove(i-1);
            }

            //TODO: adding instead of removing
            //removing objects for advanced settings
            if(!gameBoard.getAdvancedSettings()){

                characters.getChildren().removeAll();

                for(int i=0; i<gameBoard.getPlayers().size(); i++){
                    playersList.get(i).getChildren().remove(children_numOfCoins);
                    playersList.get(i).getChildren().remove(children_imageOfCoin);
                }
            }





            //setting objects on schoolboards
            int schoolBoardIndex = 1;
            for(int p = 0; p <gameBoard.getPlayers().size(); p++){

                //checking if player with index p is client player
                if(!gameBoard.getPlayers().get(p).getName().equalsIgnoreCase(gameBoard.getClientName())) {

                    //if not, show player schoolboard and name starting from schoolboard with index 1
                    //setting nameLabel
                    ((Label)playersList.get(schoolBoardIndex).getChildren().get(children_nameLabel))
                            .setText(gameBoard.getPlayers().get(p).getName());

                    //filling schoolBoard with objects
                    fillSchoolBoard(gameBoard, p, schoolBoardIndex);
                    schoolBoardIndex++;
                }
                else{

                    //show schoolboard of client on schoolboard with index 0
                    ((Label)playersList.get(0).getChildren().get(children_nameLabel)).setText(gameBoard.getClientName());
                    fillSchoolBoard(gameBoard, p,0);
                }
            }


            //updating position of islands
            if(numOfIslands!=gameBoard.getIslands().size()){
                numOfIslands = gameBoard.getIslands().size();
                int islandDim;

                for(int i=0; i<numOfIslands; i++){

                    if(gameBoard.getIslands().get(i).getNumOfIslands()>1){
                        islandDim=150;
                    }
                    else{
                        islandDim=60;
                    }

                    islands.getChildren().get(i).setLayoutX(calculateIslandPosition(numOfIslands,i, islandDim)[0]);
                    islands.getChildren().get(i).setLayoutY(calculateIslandPosition(numOfIslands,i, islandDim)[1]);
                    islands.getChildren().get(i).setScaleX(islandDim/60.0);
                    islands.getChildren().get(i).setScaleY(islandDim/60.0);
                }
            }

            //setting objects on islands
            for(int i=0; i<gameBoard.getIslands().size(); i++){


                //students
                ArrayList<Color> students = gameBoard.getIslands().get(i).getStudentsAsArray();

                for(int s=0; s < students.size(); s++) {
                    ((GridPane) ((Pane)islands.getChildren().get(i)).getChildren().get(1))
                            .add(loadImageView(students.get(s).student,
                                    15, 15), studentOnIslandColumn(s), studentOnIslandRow(s));
                }

                //towers
                for(int t =0; t<gameBoard.getIslands().get(i).getNumOfTowers();t++){
                    ((GridPane)((Pane)islands.getChildren().get(i)).getChildren().get(2))
                            .add(loadImageView(gameBoard.getIslands().get(i).getTowersColor().tower,
                                    25,25),towerOnIslandRow(t),towerOnIslandColumn(t));
                }

                //mother nature
                if(i==gameBoard.getPositionOfMotherNature()){
                    ((GridPane) ((Pane)islands.getChildren().get(i)).getChildren().get(1))
                            .add(loadImageView("/images/misc/mother_nature.png",
                                    25,25),2,1);
                }

            }

            //setting students on clouds
            for(int c=0; c<clouds.getChildren().size(); c++){

                ArrayList<Color> students = gameBoard.getClouds().get(c).getStudents();
                int student = 0;

                for(int i=0; i<2 && student<students.size(); i++){
                    for(int j=0; j<2 && student<students.size(); j++){
                        ((GridPane)((Pane) clouds.getChildren().get(c)).getChildren().get(1))
                                .add(loadImageView(students.get(student).student, 15,15), j,i);
                        student++;
                    }
                }
            }

            //initializing assistants
            for(int i=0;i<assistants.getChildren().size(); i++){
                int finalI = i;

                assistants.getChildren().get(finalI).setOnMouseEntered(event -> {showAssistant(finalI+1);});
                assistants.getChildren().get(finalI).setOnMouseExited(event -> {removeShowAssistant();});
            }


            //setting played characters
            if(gameBoard.getAdvancedSettings()) {
                for (int i = 0; i < gameBoard.getPlayedCharacters().length; i++) {

                    String finalDescription = gameBoard.getPlayedCharacters()[i].getDescription();
                    characters.getChildren().get(i).setOnMouseEntered(mouseEvent -> {
                        characterDescription.setText(finalDescription);
                    });
                    characters.getChildren().get(i).setOnMouseExited(mouseEvent -> {
                        characterDescription.setText("");
                    });

                    ((Pane) characters.getChildren().get(i)).getChildren()
                            .add(loadImageView(getCharacterPath(gameBoard.getPlayedCharacters()[i].getId()), 80, 120));

                    ((Pane) characters.getChildren().get(i)).getChildren().get(1).toBack();


                    fillCharacter(gameBoard, i);
                }
            }





        });

    }

    private void updateModel(ClientGameBoard gameBoard){

        Platform.runLater(()-> {

            //updating players
            //TODO: show if player is disconnected

            int playerIndex = 1;
            for(int p = 0; p <gameBoard.getPlayers().size(); p++){

                //checking if player with index p is client player
                if(!gameBoard.getPlayers().get(p).getName().equalsIgnoreCase(gameBoard.getClientName())) {

                    //if not, show player schoolboard and name starting from schoolboard with index 1

                    //filling schoolBoard with objects
                    refillSchoolBoard(gameBoard, p, playerIndex);

                    //updating played assistant
                    if(gameBoard.getPlayers().get(p).getPlayedAssistant()!=null) {

                        ((ImageView)((Pane)playersList.get(playerIndex).getChildren().get(children_assistantPlayed)).getChildren().get(0))
                                .setImage(loadImage(getAssistantPath(gameBoard.getPlayers().get(p).getPlayedAssistant().getRank())));
                    }
                    else{
                        ((ImageView)((Pane)playersList.get(playerIndex).getChildren().get(children_assistantPlayed)).getChildren().get(0))
                                .setImage(null);
                    }

                    //updating coins
                    ((Label)playersList.get(p).getChildren().get(children_numOfCoins))
                            .setText(Integer.toString(gameBoard.getPlayers().get(p).getCoins()));


                    playerIndex++;

                }
                else{

                    //show schoolboard of client with index 0
                    ((Label)playersList.get(0).getChildren().get(children_nameLabel)).setText(gameBoard.getClientName());

                    refillSchoolBoard(gameBoard, p,0);

                    //updating played assistant
                    if(gameBoard.getPlayers().get(p).getPlayedAssistant()!=null) {

                        ((ImageView)((Pane)playersList.get(0).getChildren().get(children_assistantPlayed)).getChildren().get(0))
                                .setImage(loadImage(getAssistantPath(gameBoard.getPlayers().get(p).getPlayedAssistant().getRank())));
                    }
                    else{
                        ((ImageView)((Pane)playersList.get(0).getChildren().get(children_assistantPlayed)).getChildren().get(0))
                                .setImage(null);
                    }

                    //updating coins
                    ((Label)playersList.get(p).getChildren().get(children_numOfCoins))
                            .setText(Integer.toString(gameBoard.getPlayers().get(p).getCoins()));
                }
            }


            //updating position of islands
            if(numOfIslands!=gameBoard.getIslands().size()){
                int islandsToRemove = numOfIslands - gameBoard.getIslands().size();

                //removing merged islands
                for(int i=0; i<islandsToRemove; i++){
                    islands.getChildren().remove(islands.getChildren().size()-1);
                }

                numOfIslands = gameBoard.getIslands().size();

                int islandDim;
                for(int i=0; i<numOfIslands; i++){

                    if(gameBoard.getIslands().get(i).getNumOfIslands()>1){
                        islandDim=100;
                    }
                    else{
                        islandDim=60;
                    }

                    islands.getChildren().get(i).setLayoutX(calculateIslandPosition(numOfIslands,i, islandDim)[0]);
                    islands.getChildren().get(i).setLayoutY(calculateIslandPosition(numOfIslands,i, islandDim)[1]);
                    islands.getChildren().get(i).setScaleX(islandDim/60.0);
                    islands.getChildren().get(i).setScaleY(islandDim/60.0);
                }
            }

            //setting objects on islands
            for(int i=0; i<gameBoard.getIslands().size(); i++){

                //removing previous images
                if(!(((GridPane) ((Pane)islands.getChildren().get(i)).getChildren().get(1)).getChildren()).isEmpty()) {
                    ((GridPane) ((Pane) islands.getChildren().get(i)).getChildren().get(1)).getChildren()
                            .remove(0, ((GridPane) ((Pane) islands.getChildren().get(i)).getChildren().get(1)).getChildren().size());
                }
                if(!(((GridPane) ((Pane)islands.getChildren().get(i)).getChildren().get(2)).getChildren()).isEmpty()) {
                    ((GridPane) ((Pane) islands.getChildren().get(i)).getChildren().get(2)).getChildren()
                            .remove(0, ((GridPane) ((Pane) islands.getChildren().get(i)).getChildren().get(2)).getChildren().size());
                }


                //students
                ArrayList<Color> students = gameBoard.getIslands().get(i).getStudentsAsArray();

                for(int s=0; s < students.size(); s++) {
                    ((GridPane) ((Pane)islands.getChildren().get(i)).getChildren().get(1))
                            .add(loadImageView(students.get(s).student,
                                    15, 15), studentOnIslandColumn(s), studentOnIslandRow(s));
                }

                //towers
                for(int t =0; t<gameBoard.getIslands().get(i).getNumOfTowers();t++){
                    ((GridPane)((Pane)islands.getChildren().get(i)).getChildren().get(2))
                            .add(loadImageView(gameBoard.getIslands().get(i).getTowersColor().tower,
                                    25,25),towerOnIslandRow(t),towerOnIslandColumn(t));
                }

                //mother nature
                if(i==gameBoard.getPositionOfMotherNature()){
                    ((GridPane) ((Pane)islands.getChildren().get(i)).getChildren().get(1))
                            .add(loadImageView("/images/misc/mother_nature.png",
                                    25,25),2,1);
                }

            }

            //setting students on clouds
            for(int c=0; c<clouds.getChildren().size(); c++){

                ArrayList<Color> students = gameBoard.getClouds().get(c).getStudents();
                int student = 0;

                //removing previous images
                if(!(((GridPane)((Pane) clouds.getChildren().get(c)).getChildren().get(1)).getChildren().isEmpty())) {
                    ((GridPane) ((Pane) clouds.getChildren().get(c)).getChildren().get(1)).getChildren()
                            .remove(0, ((GridPane) ((Pane) clouds.getChildren().get(c)).getChildren().get(1)).getChildren().size());
                }

                for(int i=0; i<2 && student<students.size(); i++){
                    for(int j=0; j<2 && student<students.size(); j++){
                        ((GridPane)((Pane) clouds.getChildren().get(c)).getChildren().get(1))
                                .add(loadImageView(students.get(student).student, 15,15), j,i);
                        student++;
                    }
                }
            }


            //updating played characters
            if(gameBoard.getAdvancedSettings()) {
                for (int i = 0; i < gameBoard.getPlayedCharacters().length; i++) {

                    refillCharacter(gameBoard, i);

                }
            }
        });


    }


    private void fillCharacter(ClientGameBoard gameBoard, int index){

        if(gameBoard.getPlayedCharacters()[index].getNoEntryTiles()!=0){
            int tile=0;
            for(int i=0; i<2 && tile<gameBoard.getPlayedCharacters()[index].getNoEntryTiles(); i++){
                for(int j=0; j<3 && tile<gameBoard.getPlayedCharacters()[index].getNoEntryTiles(); j++){

                    ((GridPane)((Pane) characters.getChildren().get(index)).getChildren().get(1))
                            .add(loadImageView("/images/misc/deny_island.png",15,15),j,i);

                    tile++;
                }
            }
        }

        if(gameBoard.getPlayedCharacters()[index].getStudents()!=null){
            Color[] students = gameBoard.getPlayedCharacters()[index].getStudents();
            int student=0;
            for(int i=0; i<2 && student<students.length; i++){
                for(int j=0; j<3 && student<students.length; j++){

                    ((GridPane)((Pane) characters.getChildren().get(index)).getChildren().get(1))
                            .add(loadImageView(students[student].student,15,15),j,i);

                    student++;
                }
            }
        }
    }

    private void refillCharacter(ClientGameBoard gameBoard, int index){

        //removing previous images
        for(int i=0; i<characters.getChildren().size(); i++){

            if(!(((GridPane)((Pane) characters.getChildren().get(index)).getChildren().get(1)).getChildren().isEmpty())) {
                ((GridPane) ((Pane) characters.getChildren().get(index)).getChildren().get(1)).getChildren()
                        .remove(0, ((GridPane) ((Pane) characters.getChildren().get(index)).getChildren().get(1)).getChildren().size() - 1);
            }
        }

        if(gameBoard.getPlayedCharacters()[index].getNoEntryTiles()!=0){
            int tile=0;
            for(int i=0; i<2 && tile<gameBoard.getPlayedCharacters()[index].getNoEntryTiles(); i++){
                for(int j=0; j<3 && tile<gameBoard.getPlayedCharacters()[index].getNoEntryTiles(); j++){

                    ((GridPane)((Pane) characters.getChildren().get(index)).getChildren().get(1))
                            .add(loadImageView("/images/misc/deny_island.png",15,15),j,i);

                    tile++;
                }
            }
        }

        if(gameBoard.getPlayedCharacters()[index].getStudents()!=null){
            Color[] students = gameBoard.getPlayedCharacters()[index].getStudents();
            int student=0;
            for(int i=0; i<2 && student<students.length; i++){
                for(int j=0; j<3 && student<students.length; j++){

                    ((GridPane)((Pane) characters.getChildren().get(index)).getChildren().get(1))
                            .add(loadImageView(students[student].student,15,15),j,i);

                    student++;
                }
            }
        }
    }


    /* SHOWS PLAYER SCHOOLBOARD
       p = player index in array of players
       s = schoolBoard index in schoolBoardsList */
    private void fillSchoolBoard(ClientGameBoard gameBoard, int p, int s) {

        //entrance
        ArrayList<Color> entrance = gameBoard.getPlayers().get(p).getSchoolBoard().getEntrance();
        for (int student = 0; student < entrance.size(); student++) {
            ((Pane) ((Group) playersList.get(s).getChildren().get(children_entrance)).getChildren().get(student)).getChildren()
                    .add(loadImageView(entrance.get(student).student, 20, 20));
        }


        //diningRoom
        /*
        int diningRow =0;
        for(Color c : gameBoard.getPlayers().get(p).getSchoolBoard().getDiningRoom().keySet()){
            for(int student = 0; student<gameBoard.getPlayers().get(p).getSchoolBoard().getDiningRoom().get(c);student++){
                ((GridPane)playersList.get(s).getChildren().get(children_diningRoom))
                        .add(loadImageView(c.student,15,15), student, diningRow);
            }
            diningRow++;
        }



        //professors
        int professorsRow =0;
        for(Color color : gameBoard.getPlayers().get(p).getSchoolBoard().getProfessors().keySet()){
            if(gameBoard.getPlayers().get(p).getSchoolBoard().getProfessors().get(color)==1){
                ((GridPane)playersList.get(s).getChildren().get(children_professors))
                        .add(loadImageView(color.professor, 25, 25), 0, professorsRow);
            }
            professorsRow++;
        }
         */


        //diningRoom
        for (int student = 0; student < gameBoard.getPlayers().get(p).getSchoolBoard().getDiningRoom().get(GREEN); student++) {
            ((GridPane) playersList.get(s).getChildren().get(children_diningRoom))
                    .add(loadImageView(GREEN.student, 15, 15), student, 0);
        }
        for (int student = 0; student < gameBoard.getPlayers().get(p).getSchoolBoard().getDiningRoom().get(RED); student++) {
            ((GridPane) playersList.get(s).getChildren().get(children_diningRoom))
                    .add(loadImageView(RED.student, 15, 15), student, 1);
        }
        for (int student = 0; student < gameBoard.getPlayers().get(p).getSchoolBoard().getDiningRoom().get(YELLOW); student++) {
            ((GridPane) playersList.get(s).getChildren().get(children_diningRoom))
                    .add(loadImageView(YELLOW.student, 15, 15), student, 2);
        }
        for (int student = 0; student < gameBoard.getPlayers().get(p).getSchoolBoard().getDiningRoom().get(PINK); student++) {
            ((GridPane) playersList.get(s).getChildren().get(children_diningRoom))
                    .add(loadImageView(PINK.student, 15, 15), student, 3);
        }
        for (int student = 0; student < gameBoard.getPlayers().get(p).getSchoolBoard().getDiningRoom().get(BLUE); student++) {
            ((GridPane) playersList.get(s).getChildren().get(children_diningRoom))
                    .add(loadImageView(BLUE.student, 15, 15), student, 4);
        }



        //professors
        if (gameBoard.getPlayers().get(p).getSchoolBoard().getProfessors().get(GREEN) == 1) {
            ((GridPane) playersList.get(s).getChildren().get(children_professors))
                    .add(loadImageView(GREEN.professor, 25, 25), 0, 0);
        }
        if (gameBoard.getPlayers().get(p).getSchoolBoard().getProfessors().get(RED) == 1) {
            ((GridPane) playersList.get(s).getChildren().get(children_professors))
                    .add(loadImageView(RED.professor, 25, 25), 0, 1);
        }

        if (gameBoard.getPlayers().get(p).getSchoolBoard().getProfessors().get(YELLOW) == 1) {
            ((GridPane) playersList.get(s).getChildren().get(children_professors))
                    .add(loadImageView(YELLOW.professor, 25, 25), 0, 2);
        }
        if (gameBoard.getPlayers().get(p).getSchoolBoard().getProfessors().get(PINK) == 1) {
            ((GridPane) playersList.get(s).getChildren().get(children_professors))
                    .add(loadImageView(PINK.professor, 25, 25), 0, 3);
        }
        if (gameBoard.getPlayers().get(p).getSchoolBoard().getProfessors().get(BLUE) == 1) {
            ((GridPane) playersList.get(s).getChildren().get(children_professors))
                    .add(loadImageView(BLUE.professor, 25, 25), 0, 4);
        }

        //towers
        for (int tower = 0; tower < gameBoard.getPlayers().get(p).getSchoolBoard().getNumOfTowers(); tower++) {

            ((Pane) ((Group) playersList.get(s).getChildren().get(children_towerTable)).getChildren().get(tower)).getChildren()
                    .add(loadImageView(gameBoard.getPlayers().get(p).getTowerColor().tower, 30, 30));
        }
    }

    private void refillSchoolBoard(ClientGameBoard gameBoard, int p, int s){

        //entrance
        ArrayList<Color> entrance = gameBoard.getPlayers().get(p).getSchoolBoard().getEntrance();


        int entrancePane =0;
        for (int student = 0; student < entrance.size(); student++) {
            ((ImageView)((Pane)((Group) playersList.get(s).getChildren().get(children_entrance)).getChildren().get(student)).getChildren().get(0))
                    .setImage(loadImage(entrance.get(student).student));
            entrancePane++;
        }
        for(int i = entrancePane; i<((Group)playersList.get(s).getChildren().get(children_entrance)).getChildren().size(); i++){
            if(!(((Pane)((Group) playersList.get(s).getChildren().get(children_entrance)).getChildren().get(i)).getChildren().isEmpty())) {
                ((ImageView) ((Pane) ((Group) playersList.get(s).getChildren().get(children_entrance)).getChildren().get(i)).getChildren().get(0))
                        .setImage(null);
            }
        }


        /*
        //diningRoom
        //removing previous images
        ((GridPane)playersList.get(s).getChildren().get(children_diningRoom)).getChildren().removeAll();
        int diningRow =0;
        for(Color c : gameBoard.getPlayers().get(p).getSchoolBoard().getDiningRoom().keySet()){
            for(int student = 0; student<gameBoard.getPlayers().get(p).getSchoolBoard().getDiningRoom().get(c);student++){
                ((GridPane)playersList.get(s).getChildren().get(children_diningRoom))
                        .add(loadImageView(c.student,15,15), student, diningRow);
            }
            diningRow++;
        }



        //professors
        //removing previous images
        ((GridPane)playersList.get(s).getChildren().get(children_professors)).getChildren().removeAll();
        int professorsRow =0;
        for(Color color : gameBoard.getPlayers().get(p).getSchoolBoard().getProfessors().keySet()){
            if(gameBoard.getPlayers().get(p).getSchoolBoard().getProfessors().get(color)==1){
                ((GridPane)playersList.get(s).getChildren().get(children_professors))
                        .add(loadImageView(color.professor, 25, 25), 0, professorsRow);
            }
            professorsRow++;
        }

         */

        //diningRoom
        //removing previous images
        if(!(((GridPane) playersList.get(s).getChildren().get(children_diningRoom)).getChildren().isEmpty())) {
            ((GridPane) playersList.get(s).getChildren().get(children_diningRoom)).getChildren()
                    .remove(0, ((GridPane) playersList.get(s).getChildren().get(children_diningRoom)).getChildren().size());
        }

        for (int student = 0; student < gameBoard.getPlayers().get(p).getSchoolBoard().getDiningRoom().get(GREEN); student++) {
            ((GridPane) playersList.get(s).getChildren().get(children_diningRoom))
                    .add(loadImageView(GREEN.student, 15, 15), student, 0);
        }
        for (int student = 0; student < gameBoard.getPlayers().get(p).getSchoolBoard().getDiningRoom().get(RED); student++) {
            ((GridPane) playersList.get(s).getChildren().get(children_diningRoom))
                    .add(loadImageView(RED.student, 15, 15), student, 1);
        }
        for (int student = 0; student < gameBoard.getPlayers().get(p).getSchoolBoard().getDiningRoom().get(YELLOW); student++) {
            ((GridPane) playersList.get(s).getChildren().get(children_diningRoom))
                    .add(loadImageView(YELLOW.student, 15, 15), student, 2);
        }
        for (int student = 0; student < gameBoard.getPlayers().get(p).getSchoolBoard().getDiningRoom().get(PINK); student++) {
            ((GridPane) playersList.get(s).getChildren().get(children_diningRoom))
                    .add(loadImageView(PINK.student, 15, 15), student, 3);
        }
        for (int student = 0; student < gameBoard.getPlayers().get(p).getSchoolBoard().getDiningRoom().get(BLUE); student++) {
            ((GridPane) playersList.get(s).getChildren().get(children_diningRoom))
                    .add(loadImageView(BLUE.student, 15, 15), student, 4);
        }


        //professors
        //removing previous images
        if(!(((GridPane) playersList.get(s).getChildren().get(children_professors)).getChildren()).isEmpty()) {
            ((GridPane) playersList.get(s).getChildren().get(children_professors)).getChildren()
                    .remove(((GridPane) playersList.get(s).getChildren().get(children_professors)).getChildren().size() - 1);
        }

        if (gameBoard.getPlayers().get(p).getSchoolBoard().getProfessors().get(GREEN) == 1) {
            ((GridPane) playersList.get(s).getChildren().get(children_professors))
                    .add(loadImageView(GREEN.professor, 25, 25), 0, 0);
        }
        if (gameBoard.getPlayers().get(p).getSchoolBoard().getProfessors().get(RED) == 1) {
            ((GridPane) playersList.get(s).getChildren().get(children_professors))
                    .add(loadImageView(RED.professor, 25, 25), 0, 1);
        }

        if (gameBoard.getPlayers().get(p).getSchoolBoard().getProfessors().get(YELLOW) == 1) {
            ((GridPane) playersList.get(s).getChildren().get(children_professors))
                    .add(loadImageView(YELLOW.professor, 25, 25), 0, 2);
        }
        if (gameBoard.getPlayers().get(p).getSchoolBoard().getProfessors().get(PINK) == 1) {
            ((GridPane) playersList.get(s).getChildren().get(children_professors))
                    .add(loadImageView(PINK.professor, 25, 25), 0, 3);
        }
        if (gameBoard.getPlayers().get(p).getSchoolBoard().getProfessors().get(BLUE) == 1) {
            ((GridPane) playersList.get(s).getChildren().get(children_professors))
                    .add(loadImageView(BLUE.professor, 25, 25), 0, 4);
        }


        //towers
        int towerPane =0;
        for (int tower = 0; tower < gameBoard.getPlayers().get(p).getSchoolBoard().getNumOfTowers(); tower++) {
            ((ImageView)((Pane)((Group) playersList.get(s).getChildren().get(children_towerTable)).getChildren().get(tower)).getChildren().get(0))
                    .setImage(loadImage(gameBoard.getPlayers().get(p).getTowerColor().tower));
            towerPane++;
        }

        //removing previous images
        for(int i = towerPane; i<((Group)playersList.get(p).getChildren().get(children_towerTable)).getChildren().size(); i++){
            if(!(((Pane)((Group) playersList.get(s).getChildren().get(children_towerTable)).getChildren().get(i)).getChildren().isEmpty())) {
                ((ImageView) ((Pane) ((Group) playersList.get(s).getChildren().get(children_towerTable)).getChildren().get(i)).getChildren().get(0))
                        .setImage(null);
            }
        }
    }



    /*METHODS FOR POSITIONING STUDENTS*/
    private int studentOnIslandRow(int num){
        Integer[] positions = {1,2,0,1,0,2,2,0,0,1,1};
        return positions[num];
    }

    private int studentOnIslandColumn(int num){
        Integer[] positions = {1,2,1,3,3,1,3,0,4,0,4};
        return positions[num];
    }

    private int towerOnIslandRow(int num){
        Integer[] positions = {1,1,1,1,1,0,0,0,0,0};
        return positions[num];

    }

    private int towerOnIslandColumn(int num){
        Integer[] positions = {1,3,2,0,4,1,3,2,0,4};
        return positions[num];

    }


    /*METHODS ASSIGNED TO ASSISTANTS DURING INITIALIZATION*/
    public void showAssistant(int rank){
        zoomedAssistant.getChildren()
                .add(loadImageView(getAssistantPath(rank),(int)zoomedAssistant.getWidth(),(int)zoomedAssistant.getHeight()));
    }

    public void removeShowAssistant(){
        zoomedAssistant.getChildren().remove(0);
    }

    private String getAssistantPath(int rank){
        String[] paths = {
                "/images/assistants/Assistente (1).png",
                "/images/assistants/Assistente (2).png",
                "/images/assistants/Assistente (3).png",
                "/images/assistants/Assistente (4).png",
                "/images/assistants/Assistente (5).png",
                "/images/assistants/Assistente (6).png",
                "/images/assistants/Assistente (7).png",
                "/images/assistants/Assistente (8).png",
                "/images/assistants/Assistente (9).png",
                "/images/assistants/Assistente (10).png",
        };

        return paths[rank-1];
    }


    private String getCharacterPath(int id){
        String[] paths = {
                "/images/characters/Character1.jpg",
                "/images/characters/Character2.jpg",
                "/images/characters/Character3.jpg",
                "/images/characters/Character4.jpg",
                "/images/characters/Character5.jpg",
                "/images/characters/Character6.jpg",
                "/images/characters/Character7.jpg",
                "/images/characters/Character8.jpg",
                "/images/characters/Character9.jpg",
                "/images/characters/Character10.jpg",
                "/images/characters/Character11.jpg",
                "/images/characters/Character12.jpg",
        };
        return paths[id-1];
    }

    public synchronized void selectStudent(Color studentColor){
        askingDone=true;
        chosenAction = 1;
        this.studentColor = studentColor;
        /*TODO: disable students*/
        notifyAll();
    }

    public synchronized void selectCharacter(int index){
        askingDone=true;
        chosenAction = 2;
        this.characterNumber = index;
        /*TODO: disable Characters*/
        notifyAll();
    }

    public synchronized void selectAssistant(int assistantRank){
        askingDone=true;
        this.assistantRank= assistantRank;

        Platform.runLater(()->{

            //disabling the selected assistant to avoid clicks on next selections
            assistants.getChildren().get(assistantRank-1).setVisible(false);

            //removing its image
            //((ImageView)((Pane)assistants.getChildren().get(assistantRank-1)).getChildren().get(0)).setImage(null);

            //adding its image to playedAssistant Pane
            //((ImageView)((Pane)playersList.get(0).getChildren().get(children_assistantPlayed)).getChildren().get(0))
            //        .setImage(loadImage(getAssistantPath(assistantRank)));

            //removing event on MouseClick
            for(int i=0; i<assistants.getChildren().size(); i++){
                assistants.getChildren().get(i).setOnMouseClicked(mouseEvent -> {});
            }

            dialogText.setText("");

        });

        notifyAll();
    }

    public synchronized void selectDestination(int index){
        askingDone=true;
        // TODO: this.destination = index;
        /*TODO: disable islands and dining room*/
        notifyAll();
    }

    public synchronized void selectCloud(int index){
        askingDone=true;
        // TODO: this.cloudNumber = index;
        /*TODO: disable clouds*/
        notifyAll();
    }

    public synchronized void selectMotherNature(int selectedIsland){
        askingDone=true;
        // TODO: this.motherNatureSteps = selectedIsland-currentIsland;
        /*TODO: disable assistant cards*/
        notifyAll();
    }

    public synchronized void selectBoolean(boolean choice){
        askingDone=true;
        this.choice=choice;
        notifyAll();
    }

    public synchronized void askAssistant(){
        askingDone=false;
        Platform.runLater(()->{
            dialogText.setText("Select an assistant from your Deck");

            for(int i=0; i<assistants.getChildren().size(); i++){
                int finalI = i;
                assistants.getChildren().get(i).setOnMouseClicked(mouseEvent -> {selectAssistant(finalI+1);});
            }
        });

    }

    public void askStudentColor() {
        askingDone=false;
        /*TODO: print: Select a student*/
        /*TODO: enable students and set onMouseClickAction to selectStudent(color)*/
    }

    public void askIslandNumber() {
        askingDone=false;
        /*TODO: print: Select an island*/
        /*TODO: enable islands and set onMouseClickAction to selectDestination(index+1)*/
    }

    public void askStudentDestination() {
        askingDone=false;
        /*TODO: print: Select an island*/
        /*TODO: enable islands and set onMouseClickAction to selectDestination(index+1)*/
        /*TODO: enable diningRoom and set onMouseClickAction to selectDestination(0)*/
    }

    public void askMotherNatureSteps() {
        askingDone=false;
        /*TODO: print: Select the island you want to move MotherNature to*/
        /*TODO: enable assistantSteps islands after currentIsland and set onMouseClickAction to selectMotherNature(index)*/
    }

    public void askCloudNumber() {
        askingDone=false;
        /*TODO: print: Select a cloud*/
        /*TODO: enable clouds and set onMouseClickAction to selectCloud(index+1)*/
    }

    public void askCharacterNumber() {
        askingDone=false;
        /*TODO: print: Select a Character*/
        /*TODO: enable characters and cancelButton and set onMouseClickAction to selectCharacter(index+1) for characters, selectCharacter(-1) for cancelButton*/
    }

    public void askBoolean() {
        askingDone=false;
        /*TODO: enable tickButton and cancelButton and set onMouseClickAction to selectBoolean(true) for tickButton and to selectBoolean(false) for cancelButton*/
    }

    public void chooseActionStudent() {
        /*TODO: print: Select a student from your entrance to move it, or select a Character to buy and use it*/
        /*TODO: enable Entrance students and set onMouseClickAction to selectStudent(color)*/
        /*TODO: enable Characters and set onMouseClickAction to selectCharacter(index+1)*/
        /*Platform.runLater(()->{
            dialogText.setText("Select a student from your entrance to move it, or select a Character to buy and use it");

            for(Color color : st){
                int finalI = i;
                assistants.getChildren().get(i).setOnMouseClicked(mouseEvent -> {selectAssistant(finalI+1);});
            }
        });*/
    }

    public void chooseActionMotherNature() {
        /*TODO: print: Select a student from your entrance to move it, or select a Character to buy and use it*/
        /*TODO: enable Entrance students and set onMouseClickAction to selectStudent(color)*/
        /*TODO: enable Characters and set onMouseClickAction to selectCharacter(index+1)*/
    }

    public void chooseActionCloud() {
        /*TODO: print: Select a cloud to use it, or select a Character to buy and use it*/
        /*TODO: enable clouds and set onMouseClickAction to selectCloud(index+1)*/
        /*TODO: enable Characters and set onMouseClickAction to selectCharacter(index+1)*/
    }

    public boolean isAskingDone(){
        return askingDone;
    }

    public int getAssistantRank(){
        return assistantRank;
    }

    public int getChosenAction() {
        return chosenAction;
    }

    public Color getStudentColor() {
        return RED;
    }

    public int getIslandNumber() {
        return destination;
    }

    public int getMotherNatureSteps() {
        return motherNatureSteps;
    }

    public int getCloudNumber(){
        return cloudNumber;
    }

    public int getCharacterNumber() {
        return characterNumber;
    }

    public int getStudentDestination() {
        return destination;
    }

    public boolean getChoice() {
        return choice;
    }


    public int[] calculateIslandPosition(int n, int islandIndex, int dim){

        int a=400;
        int b=150;
        int x;
        int y;
        int h = 720;
        int w = 1080;

        y= (int) (b*Math.sin(2*Math.PI*islandIndex/n)+h/2-dim/2 - 30);
        x= (int) (a*Math.cos(2*Math.PI*islandIndex/n)+w/2-dim/2);

        return new int[]{x, y};
    }

    public static void main(String args[]){


        /*
        GameSceneController gameSceneController = new GameSceneController();

        for(int i =0; i<12; i++){
            int[] coordinate = gameSceneController.calculateIslandPosition(12,i,60);
            System.out.println(coordinate[0] +", "+ coordinate[1]);
        }

         */


        /*
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        System.out.println(screenHeight + " " + screenWidth);

         */


        Map<String, Integer> map = new HashMap<>();
        map.put("GREEN", 0);
        map.put("RED", 0);
        map.put("BLUE", 0);
        map.put("YELLOW", 0);
        map.put("PINKKKKKKKKKK", 0);

        for(int i=0; i<10; i++){
            System.out.println(map.keySet());
        }


    }

}
