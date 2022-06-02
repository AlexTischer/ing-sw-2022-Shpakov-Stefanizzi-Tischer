package it.polimi.ingsw.client.model;

import it.polimi.ingsw.server.model.Color;

import java.io.Serializable;
import java.util.ArrayList;

public class ClientCloud implements Serializable {

    private ArrayList<Color> students;


    public ArrayList<Color> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Color> students) {
        this.students = students;
    }
}


