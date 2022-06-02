package it.polimi.ingsw.client.model;

import it.polimi.ingsw.server.model.Color;
import it.polimi.ingsw.server.model.TowerColor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class ClientSchoolBoard implements Serializable {

    private ArrayList<Color> entrance;
    private Map<Color,Integer> diningRoom;
    private Map<Color,Integer> professors;
    private int numOfTowers;
    private TowerColor towersColor;

    public ArrayList<Color> getEntrance() {
        return entrance;
    }

    public void setEntrance(ArrayList<Color> entrance) {
        this.entrance = entrance;
    }

    public Map<Color, Integer> getDiningRoom() {
        return diningRoom;
    }

    public void setDiningRoom(Map<Color, Integer> diningRoom) {
        this.diningRoom = diningRoom;
    }

    public Map<Color, Integer> getProfessors() {
        return professors;
    }

    public void setProfessors(Map<Color, Integer> professors) {
        this.professors = professors;
    }

    public int getNumOfTowers() {
        return numOfTowers;
    }

    public void setNumOfTowers(int numOfTowers) {
        this.numOfTowers = numOfTowers;
    }

    public TowerColor getTowersColor() {
        return towersColor;
    }

    public void setTowersColor(TowerColor towersColor) {
        this.towersColor = towersColor;
    }
}
