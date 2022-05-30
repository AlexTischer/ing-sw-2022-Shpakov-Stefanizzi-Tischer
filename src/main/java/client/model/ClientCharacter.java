package client.model;

import server.model.Color;

import java.io.Serializable;

public class ClientCharacter implements Serializable {

    private int cost;
    private int noEntryTiles;
    private Color[] students;

    private int id;

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getNoEntryTiles() {
        return noEntryTiles;
    }

    public void setNoEntryTiles(int noEntryTiles) {
        this.noEntryTiles = noEntryTiles;
    }

    public Color[] getStudents() {
        return students;
    }

    public void setStudents(Color[] students) {
        this.students = students;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
