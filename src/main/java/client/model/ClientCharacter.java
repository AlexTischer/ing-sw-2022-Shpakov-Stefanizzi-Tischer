package client.model;

import server.model.Color;

public class ClientCharacter {

    private int cost;
    private int noEntryTiles;
    private Color[] students;

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
}
