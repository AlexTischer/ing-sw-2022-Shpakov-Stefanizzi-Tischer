package it.polimi.ingsw.client.model;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.packets.ActivateCharacterPacket;
import it.polimi.ingsw.packets.Packet;
import it.polimi.ingsw.server.model.Color;

import java.io.Serializable;

public class ClientCharacter implements Serializable {

    private int cost;
    private String description;
    private int id;

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getNoEntryTiles() {
        //characters 2,3,6,8 don't have noEntryTiles so default value is -1
        return -1;
    }

    public void setNoEntryTiles(int noEntryTiles) {
       return;
    }

    public Color[] getStudents() {
        //characters 2,3,6,8 don't have any students so default value is null
        return null;
    }

    public void setStudents(Color[] students) {
        return;
    }

    public ActivateCharacterPacket createPacket(View view){
        throw new UnsupportedOperationException("This character doesn't need activation");
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId(){
        return this.id;
    }
}
