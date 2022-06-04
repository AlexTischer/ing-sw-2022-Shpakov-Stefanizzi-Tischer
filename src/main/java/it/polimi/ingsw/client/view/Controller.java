package it.polimi.ingsw.client.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

//implemento initializable per poter popolare la choicebox con i miei dati
public class Controller implements Initializable {

    @FXML
    private Label myLabel;
    @FXML
    //devo inserire anche il tipo di dato (string)
    private ChoiceBox<String> myChoiceBox = new ChoiceBox<>();

    //items to populate the choicebox
    private String[] food = {"pizza","sushi","ramen"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //ora choicebox puù accettare una collection
        myChoiceBox.getItems().addAll(food);
        myChoiceBox.setOnAction(this::getFood);
    }
    public void getFood(ActionEvent event){
        String myFood = myChoiceBox.getValue();
        myLabel.setText(myFood);
    }
}