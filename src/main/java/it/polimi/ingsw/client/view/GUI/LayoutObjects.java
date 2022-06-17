package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.view.GUI.LayoutProperties;

import java.util.ArrayList;

public class LayoutObjects {

    public static ArrayList<LayoutProperties> getSchoolBoards(){
        LayoutProperties schoolboard1 = new LayoutProperties(420,179,7,14,"/images/schoolboard/Schoolboard.png");
        LayoutProperties schoolboard2 = new LayoutProperties(123,264,0,8,"/images/schoolboard/Schoolboard270.png");
        LayoutProperties schoolboard3 = new LayoutProperties(264,123,10,0,"/images/schoolboard/Schoolboard.png");
        LayoutProperties schoolboard4 = new LayoutProperties(123,264,26,4,"/images/schoolboard/Schoolboard90.png");
        ArrayList<LayoutProperties> schoolboards = new ArrayList<>();

        schoolboards.add(schoolboard1);
        schoolboards.add(schoolboard2);
        schoolboards.add(schoolboard3);
        schoolboards.add(schoolboard4);

        return schoolboards;

    }

}
