package it.polimi.ingsw.client.model;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.packets.ActivateCharacterPacket;
import it.polimi.ingsw.packets.Packet;
import it.polimi.ingsw.server.model.Color;

import java.io.Serializable;

public class ClientCharacter implements Serializable {

    private int cost;
    private int noEntryTiles;
    private Color[] students;
    private String description;

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

    public ActivateCharacterPacket createPacket(View view){
        throw new UnsupportedOperationException();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
