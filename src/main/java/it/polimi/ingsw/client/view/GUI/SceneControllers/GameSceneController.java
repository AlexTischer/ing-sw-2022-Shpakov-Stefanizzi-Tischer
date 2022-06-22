package it.polimi.ingsw.client.view.GUI.SceneControllers;

import it.polimi.ingsw.client.model.ClientGameBoard;
import it.polimi.ingsw.server.model.Color;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;


import java.awt.*;
import java.util.ArrayList;

import static it.polimi.ingsw.server.model.Color.*;

public class GameSceneController extends SceneController {

    //TODO implement every method


    @FXML
    private ArrayList<Group> playersList;
    @FXML
    private ArrayList<Pane> islandsList;
    @FXML
    private ArrayList<GridPane> diningRoomList, professorsList;
    @FXML
    private ArrayList<ArrayList<Pane>> entrancesList, towerTablesList;

    @FXML
    private Group islands, assistants, clouds, characters;
    @FXML
    private Pane zoomedAssistant;

    @FXML
    private Label characterDescription;
    @FXML
    private Group group;



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

        this.positionOfMotherNature=gameBoard.getPositionOfMotherNature();

        Platform.runLater(()-> {

            resizeScreen(group);

            //setting playground according to num of players
            for(int i=4; i>gameBoard.getPlayers().size(); i--){

                //removing unnecessary schoolboards
                playersList.get(i-1).getChildren().remove(0,playersList.get(i-1).getChildren().size());

                //removing unnecessary clouds
                clouds.getChildren().remove(i-1);
            }


            //setting objects on schoolboards
            int schoolBoardIndex = 1;
            for(int p = 0; p <gameBoard.getPlayers().size(); p++){

                //checking if player with index p is client player
                if(!gameBoard.getPlayers().get(p).getName().equalsIgnoreCase(gameBoard.getClientName())) {

                    //if not, show player schoolboard starting from schoolboard with index 1
                    fillSchoolBoard(gameBoard, p, schoolBoardIndex);
                    schoolBoardIndex++;
                }
                else{
                    //show schoolboard of client on schoolboard with index 0
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
                    ((GridPane) islandsList.get(i).getChildren().get(1))
                            .add(loadImage(students.get(s).student,
                                    15, 15), studentOnIslandColumn(s), studentOnIslandRow(s));
                }

                //towers
                for(int t =0; t<gameBoard.getIslands().get(i).getNumOfTowers();t++){
                    ((GridPane)((Pane)islands.getChildren().get(i)).getChildren().get(2))
                            .add(loadImage(gameBoard.getIslands().get(i).getTowersColor().tower,
                                    25,25),towerOnIslandRow(t),towerOnIslandColumn(t));
                }

                //mother nature
                if(i==positionOfMotherNature){
                    ((GridPane) islandsList.get(i).getChildren().get(1))
                            .add(loadImage("/images/misc/mother_nature.png",
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
                                .add(loadImage(students.get(student).student, 15,15), j,i);
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
            for(int i=0; i<characters.getChildren().size(); i++) {

                int finalId = gameBoard.getPlayedCharacters()[i].getId();
                characters.getChildren().get(i).setOnMouseEntered(mouseEvent -> {characterDescription.setText(getCharacterDescription(finalId));});
                characters.getChildren().get(i).setOnMouseExited(mouseEvent -> {characterDescription.setText("");});

                ((Pane) characters.getChildren().get(i)).getChildren()
                        .add(loadImage(getCharacterPath(gameBoard.getPlayedCharacters()[i].getId()), 80, 120));

                ((Pane) characters.getChildren().get(i)).getChildren().get(1).toBack();


                fillCharacter(gameBoard, i);

            }
        });

    }

    private void updateModel(ClientGameBoard gameBoard){

        //updating schoolboards

    }


    private void fillCharacter(ClientGameBoard gameBoard, int index){

        if(gameBoard.getPlayedCharacters()[index].getNoEntryTiles()!=0){
            int tile=0;
            for(int i=0; i<2 && tile<gameBoard.getPlayedCharacters()[index].getNoEntryTiles(); i++){
                for(int j=0; j<3 && tile<gameBoard.getPlayedCharacters()[index].getNoEntryTiles(); j++){

                    ((GridPane)((Pane) characters.getChildren().get(index)).getChildren().get(1))
                            .add(loadImage("/images/misc/deny_island.png",15,15),j,i);

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
                            .add(loadImage(students[student].student,15,15),j,i);

                    student++;
                }
            }
        }
    }


    /* SHOWS PLAYER SCHOOLBOARD
       p = player index in array of players
       s = schoolBoard index in schoolBoardsList */
    private void fillSchoolBoard(ClientGameBoard gameBoard, int p, int s){
        //entrance
        ArrayList<Color> entrance = gameBoard.getPlayers().get(p).getSchoolBoard().getEntrance();


        for (int student = 0; student < entrance.size(); student++) {
            entrancesList.get(s).get(student).getChildren().add(loadImage(entrance.get(student).student, 20, 20));
            //((Pane)((Group) playersList.get(p).getChildren().get(1)).getChildren().get(student)).getChildren()
            //        .add(loadImage(entrance.get(student).student, 20, 20));
        }


        //diningRoom
        for (int student = 0; student < gameBoard.getPlayers().get(p).getSchoolBoard().getDiningRoom().get(GREEN); student++) {
            diningRoomList.get(s).add(loadImage(GREEN.student, 15, 15), student, 0);
        }
        for (int student = 0; student < gameBoard.getPlayers().get(p).getSchoolBoard().getDiningRoom().get(RED); student++) {
            diningRoomList.get(s).add(loadImage(RED.student, 15, 15), student, 0);
        }
        for (int student = 0; student < gameBoard.getPlayers().get(p).getSchoolBoard().getDiningRoom().get(YELLOW); student++) {
            diningRoomList.get(s).add(loadImage(YELLOW.student, 15, 15), student, 0);
        }
        for (int student = 0; student < gameBoard.getPlayers().get(p).getSchoolBoard().getDiningRoom().get(PINK); student++) {
            diningRoomList.get(s).add(loadImage(PINK.student, 15, 15), student, 0);
        }
        for (int student = 0; student < gameBoard.getPlayers().get(p).getSchoolBoard().getDiningRoom().get(BLUE); student++) {
            diningRoomList.get(s).add(loadImage(BLUE.student, 15, 15), student, 0);
        }


        //professors
        int row=0;
        for(Color color : gameBoard.getPlayers().get(p).getSchoolBoard().getProfessors().keySet()){
            if(gameBoard.getPlayers().get(p).getSchoolBoard().getProfessors().get(color)==1){
                professorsList.get(s).add(loadImage(color.professor, 25, 25), 0, row);
            }
            row++;
        }

        //towers
        for (int tower = 0; tower < gameBoard.getPlayers().get(p).getSchoolBoard().getNumOfTowers(); tower++) {
            towerTablesList.get(s).get(tower).getChildren().add(loadImage(gameBoard.getPlayers().get(p).getTowerColor().tower, 30, 30));
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
                .add(loadImage(getAssistantPath(rank),(int)zoomedAssistant.getWidth(),(int)zoomedAssistant.getHeight()));
    }

    public void removeShowAssistant(){
        zoomedAssistant.getChildren().remove(0);
    }

    public String getCharacterDescription(int id) {
        String[] description = {
                "Take 1 Student from this card and place it on an Island of your choice",
                "You get 2 more influence points",
                "When calculating influence, Towers won't count",
                "When calculating influence, student's color of your choice won't count",
                "Place a No Entry tile on an island of your choice. The first time \nMother Nature ends her movement there, influence will not be calculated.",
                "You can get the professor even if you have the same number \nof students as the player who currently controls that professor",
                "You can swap up to 2 students between your entrance \nand your dining room",
                "You can move Mother Nature up to 2 steps more than \nwhat's indicated by the Assistant card you played",
                "Take up to 3 students from this card and replace them \nwith the same number of students from your entrance",
                "Take 1 student from this card and place it in your dining room",
                "Every player (including yourself) must return 3 students \nof a color of your choice from their dining room to the bag",
                "Choose an Island and resolve it as if Mother Nature \nhad ended her movement there"};

        return description[id - 1];
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
        /*TODO: disable students*/
        notifyAll();
    }

    public synchronized void selectAssistant(int assistantRank){
        askingDone=true;
        this.assistantRank= assistantRank;
        /*TODO: disable assistant cards*/
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
        /*TODO: print: Select an assistant from your Deck*/
        /*TODO: enable Assistants and set onMouseClickAction to selectAssistant(index+1)*/
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
        int h = 720; //TODO: =islandsPane.getHeight
        int w = 1280; //TODO: =islandsPane.getWidth

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

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        System.out.println(screenHeight + " " + screenWidth);

    }

}
