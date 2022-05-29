package client.view;


import client.model.*;
import client.view.utils.Printer;
import junit.framework.TestCase;
import server.model.Assistant;
import server.model.AssistantType;
import server.model.Color;
import server.model.TowerColor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CliTest {

    public static void main(String args[]) throws IOException, InterruptedException {
        Printer printer = new Printer();

        ArrayList<ClientIsland> islands = new ArrayList<ClientIsland>();
        ClientIsland island1 = new ClientIsland();
        island1.setNumOfIslands(1);
        Map<Color, Integer> students = new HashMap<>();
        students.put(Color.BLUE, 2);
        students.put(Color.GREEN, 1);
        students.put(Color.PINK,12);
        students.put(Color.RED, 5);
        students.put(Color.YELLOW,7);
        island1.setStudents(students);
        island1.setNumOfTowers(1);
        island1.setTowersColor(TowerColor.GREY);
        island1.setNoEntry(true);

        ClientIsland island2 = new ClientIsland();
        island2.setNumOfIslands(1);
        Map<Color, Integer> students2 = new HashMap<>();
        students2.put(Color.BLUE, 0);
        students2.put(Color.GREEN, 4);
        students2.put(Color.PINK,5);
        students2.put(Color.RED, 1);
        students2.put(Color.YELLOW,0);
        island2.setStudents(students2);
        island2.setNumOfTowers(1);
        island2.setTowersColor(TowerColor.GREY);

        islands.add(island1);
        islands.add(island2);
        islands.add(island2);
        islands.add(island2);
        islands.add(island2);
        islands.add(island2);
        islands.add(island2);
        islands.add(island2);
        islands.add(island2);
        islands.add(island2);
        islands.add(island2);
        islands.add(island2);


        ClientSchoolBoard clientSchoolBoard = new ClientSchoolBoard();

        ArrayList<Color> entrance = new ArrayList<>();
        entrance.add(Color.RED);
        entrance.add(Color.BLUE);
        entrance.add(Color.YELLOW);
        clientSchoolBoard.setEntrance(entrance);

        Map<Color,Integer> diningRoom = new HashMap<>();
        diningRoom.put(Color.GREEN, 3);
        diningRoom.put(Color.RED, 0);
        diningRoom.put(Color.YELLOW, 7);
        diningRoom.put(Color.PINK, 5);
        diningRoom.put(Color.BLUE,10);
        clientSchoolBoard.setDiningRoom(diningRoom);

        Map<Color, Integer> professors = new HashMap<>();
        professors.put(Color.BLUE, 1);
        professors.put(Color.GREEN, 1);
        professors.put(Color.RED, 0);
        professors.put(Color.YELLOW, 1);
        professors.put(Color.PINK, 0);
        clientSchoolBoard.setProfessors(professors);

        clientSchoolBoard.setTowersColor(TowerColor.GREY);

        clientSchoolBoard.setNumOfTowers(6);

        ArrayList<ClientSchoolBoard> clientSchoolBoards = new ArrayList<>();
        clientSchoolBoards.add(clientSchoolBoard);
        clientSchoolBoards.add(clientSchoolBoard);
        clientSchoolBoards.add(clientSchoolBoard);
        clientSchoolBoards.add(clientSchoolBoard);


        ArrayList<ClientCloud> clientClouds = new ArrayList<>();

        ClientCloud cloud1 = new ClientCloud();
        ArrayList<Color> studentsIsland1 = new ArrayList<>();
        cloud1.setStudents(studentsIsland1);
        clientClouds.add(cloud1);

        ClientCloud cloud2 = new ClientCloud();
        ArrayList<Color> studentsIsland2 = new ArrayList<>();
        studentsIsland2.add(Color.GREEN);
        cloud2.setStudents(studentsIsland2);
        clientClouds.add(cloud2);

        ClientCloud cloud3 = new ClientCloud();
        ArrayList<Color> studentsIsland3 = new ArrayList<>();
        studentsIsland3.add(Color.GREEN);
        studentsIsland3.add(Color.YELLOW);
        studentsIsland3.add(Color.RED);
        cloud3.setStudents(studentsIsland3);
        clientClouds.add(cloud3);

        ClientCloud cloud4 = new ClientCloud();
        ArrayList<Color> studentsIsland4 = new ArrayList<>();
        studentsIsland4.add(Color.YELLOW);
        studentsIsland4.add(Color.YELLOW);
        studentsIsland4.add(Color.YELLOW);
        studentsIsland4.add(Color.YELLOW);
        cloud4.setStudents(studentsIsland4);
        clientClouds.add(cloud4);



        ArrayList<ClientCharacter> characters = new ArrayList<>();

        ClientCharacter character1 = new ClientCharacter();
        ClientCharacter character2 = new ClientCharacter();
        ClientCharacter character3 = new ClientCharacter();

        character1.setCost(1);
        character2.setCost(2);
        character3.setCost(3);

        Color[] students11 = new Color[6];
        students11[0] = Color.BLUE;
        students11[1] = Color.BLUE;
        students11[2] = Color.YELLOW;
        students11[3] = Color.RED;
        students11[4] = Color.PINK;
        students11[5] = Color.GREEN;


        character1.setStudents(students11);
        character2.setNoEntryTiles(4);

        characters.add(character1);
        characters.add(character2);
        characters.add(character3);


        Assistant[] assistants = new Assistant[10];

        assistants[0] = new Assistant(1,1, AssistantType.ONE);
        assistants[1] = new Assistant(2,1,AssistantType.ONE);
        assistants[2] = new Assistant(3,2,AssistantType.ONE);
        assistants[3] = new Assistant(4,2,AssistantType.ONE);
        assistants[4] = new Assistant(5,3,AssistantType.ONE);
        assistants[5] = new Assistant(6,3,AssistantType.ONE);
        assistants[6] = new Assistant(7,4,AssistantType.ONE);
        assistants[7] = new Assistant(8,4,AssistantType.ONE);
        assistants[8] = new Assistant(9,5,AssistantType.ONE);
        assistants[9] = new Assistant(10,5,AssistantType.ONE);

        printer.clearConsole();
        //System.out.print("\n");

        ClientGameBoard clientGameBoard = new ClientGameBoard();
        clientGameBoard.setIslands(islands);
        clientGameBoard.setClouds(clientClouds);

        //printer.showModel();
    }

}
