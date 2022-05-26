package client.view.utils;

import client.model.*;
import server.model.Assistant;
import server.model.Color;
import server.model.TowerColor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class Printer {

    public void showModel(ArrayList<ClientIsland> islands, ArrayList<ClientSchoolBoard> schoolBoards,
                          ArrayList<ClientCloud> clouds, ArrayList<ClientCharacter> characters,
                          Assistant[] assistants){

        printIslands(islands);
        printSchoolBoards(schoolBoards);
        printClouds(clouds);
        printCharacters(characters);
        printAssistants(assistants);
    }

    public void clearConsole() throws IOException, InterruptedException {
        if(System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("windows")){
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        }
    }

    public void printIslands(ArrayList<ClientIsland> islands){
        printIslandLine1(islands);
        printIslandLine2(islands);
        printIslandLine3(islands);
        printIslandLine4(islands);
        printIslandLine5(islands);
    }

    public void printSchoolBoards(ArrayList<ClientSchoolBoard> schoolBoards){
        printSchoolBoardLine1(schoolBoards);
        printSchoolBoardLine2(schoolBoards);
        printSchoolBoardLine3(schoolBoards);
        printSchoolBoardLine4(schoolBoards);
        printSchoolBoardLine5(schoolBoards);
        printSchoolBoardLine6(schoolBoards);
        printSchoolBoardLine7(schoolBoards);
    }

    public void printClouds(ArrayList<ClientCloud> clientClouds){
        printCloudLine1(clientClouds);
        printCloudLine2(clientClouds);
        printCloudLine3(clientClouds);
        printCloudLine4(clientClouds);
    }

    public void printCharacters(ArrayList<ClientCharacter> characters){
        printCharacterLine1(characters);
        printCharacterLine2(characters);
        printCharacterLine3(characters);
        printCharacterLine4(characters);
        printCharacterLine5(characters);
    }

    public void printAssistants(Assistant[] assistants){
        printAssistantLine1(assistants);
        printAssistantLine2(assistants);
        printAssistantLine3(assistants);
        printAssistantLine4(assistants);
        printAssistantLine5(assistants);
    }






    private void printIslandStudents(int numOfStudents, Color color){
        if(numOfStudents!=0) {
            System.out.print(color.label);
            System.out.printf("%2s", numOfStudents);
            System.out.print(AnsiKeys.COLOR_RESET);
        }
        else {
            System.out.print("  ");
        }
    }

    private void printIslandLine1(ArrayList<ClientIsland> islands) {
        for (int i=0; i<islands.size(); i++) {
            System.out.print("╔══════╗");
            System.out.print(" ");
        }
        System.out.print("\n");
    }

    private void printIslandLine2(ArrayList<ClientIsland> islands) {
        for (ClientIsland island : islands) {
            System.out.print("║ MN ");

            if(island.isNoEntry()){
                System.out.print(AnsiKeys.COLOR_BRIGHT_RED);
                System.out.print("X");
                System.out.print(AnsiKeys.COLOR_RESET);
            }
            else {
                System.out.print(" ");
            }

            System.out.print(" ║");
            System.out.print(" ");
        }
        System.out.print("\n");
    }

    private void printIslandLine3(ArrayList<ClientIsland> islands) {
        for (ClientIsland island : islands) {

            System.out.print("║");

            System.out.print(island.getNumOfTowers());

            System.out.print("T"); // "⛫"  "♜"

            printIslandStudents(island.getStudents().get(Color.GREEN), Color.GREEN);
            printIslandStudents(island.getStudents().get(Color.RED), Color.RED);

            System.out.print("║");
            System.out.print(" ");
        }
        System.out.print("\n");
    }

    private void printIslandLine4(ArrayList<ClientIsland> islands) {
        for (ClientIsland island : islands) {

            System.out.print("║");

            printIslandStudents(island.getStudents().get(Color.YELLOW), Color.YELLOW);
            printIslandStudents(island.getStudents().get(Color.PINK), Color.PINK);
            printIslandStudents(island.getStudents().get(Color.BLUE), Color.BLUE);

            System.out.print("║");
            System.out.print(" ");
        }
        System.out.print("\n");
    }

    private void printIslandLine5(ArrayList<ClientIsland> islands){
        for (int i=0; i<islands.size(); i++) {
            System.out.print("╚══════╝");
            System.out.print(" ");
        }
        System.out.print("\n");
    }


    /*  SCHOOLBOARD_LAYOUT

        System.out.println("+----+------------+---+----+\n" +
                "| ii | pppppppppp | P | TT |\n" +
                "| ii | pppppppppp | P | TT |\n" +
                "| ii | pppppppppp | P | TT |\n" +
                "| ii | pppppppppp | P | TT |\n" +
                "| ii | pppppppppp | P | TT |\n" +
                "+----+------------+---+----+");

     */


    private void printEntranceStudents(ArrayList<Color> students, int position){
        if(students.size() >= position){
            System.out.print(students.get(position-1).label);
            //System.out.print("●");
            System.out.print("o");
            System.out.print(AnsiKeys.COLOR_RESET);
        }
        else {
            System.out.print(" ");
        }
    }

    private void printTowerTableItems(int numOfTowers, TowerColor towerColor, int position){
        if(numOfTowers>=position){

            if(towerColor == TowerColor.WHITE){
                System.out.print(AnsiKeys.COLOR_BACKGROUND_BLACK);
            }
            if(towerColor == TowerColor.BLACK){
                System.out.print(AnsiKeys.COLOR_BACKGROUND_WHITE);
            }

            System.out.print(towerColor.label);
            System.out.print("T");
            System.out.print(AnsiKeys.COLOR_RESET);
        }
        else {
            System.out.print(" ");
        }
    }

    private void printSchoolBoardLine1(ArrayList<ClientSchoolBoard> clientSchoolBoards){

        for(int i=0; i<clientSchoolBoards.size();i++){
            System.out.print("╔════╦════════════╦═══╦════╗");
            System.out.print("  ");
        }
        System.out.print("\n");
    }

    private void printSchoolBoardLine2(ArrayList<ClientSchoolBoard> clientSchoolBoards){

        for(ClientSchoolBoard clientSchoolBoard : clientSchoolBoards){

            System.out.print("║ ");
            //printing first & second entrance item (if they exist)
            printEntranceStudents(clientSchoolBoard.getEntrance(), 1);
            printEntranceStudents(clientSchoolBoard.getEntrance(), 2);

            System.out.print(" ║ ");

            //printing GREEN students in diningRoom
            System.out.print(Color.GREEN.label);
            for(int i=0; i<clientSchoolBoard.getDiningRoom().get(Color.GREEN); i++){
                //System.out.print("●");
                System.out.print("o");
            }
            System.out.print(AnsiKeys.COLOR_RESET);

            for(int i=10; i>clientSchoolBoard.getDiningRoom().get(Color.GREEN); i--){
                System.out.print(" ");
            }

            System.out.print(" ║ ");

            //printing GREEN professor (if exists)
            if(clientSchoolBoard.getProfessors().get(Color.GREEN) == 1){
                System.out.print(Color.GREEN.label);
                System.out.print("■");
                System.out.print(AnsiKeys.COLOR_RESET);
            }
            else{
                System.out.print(" ");
            }

            System.out.print(" ║ ");

            //printing first & second tower (if they exist)
            printTowerTableItems(clientSchoolBoard.getNumOfTowers(), clientSchoolBoard.getTowersColor(), 1);
            printTowerTableItems(clientSchoolBoard.getNumOfTowers(), clientSchoolBoard.getTowersColor(), 2);

            System.out.print(" ║");

            System.out.print("  ");
        }
        System.out.print("\n");
    }

    private void printSchoolBoardLine3(ArrayList<ClientSchoolBoard> clientSchoolBoards){

        for(ClientSchoolBoard clientSchoolBoard : clientSchoolBoards){

            System.out.print("║ ");
            //printing first & second entrance item (if they exist)
            printEntranceStudents(clientSchoolBoard.getEntrance(), 3);
            printEntranceStudents(clientSchoolBoard.getEntrance(), 4);

            System.out.print(" ║ ");

            //printing RED students in diningRoom
            System.out.print(Color.RED.label);
            for(int i=0; i<clientSchoolBoard.getDiningRoom().get(Color.RED); i++){
                //System.out.print("●");
                System.out.print("o");
            }
            System.out.print(AnsiKeys.COLOR_RESET);

            for(int i=10; i>clientSchoolBoard.getDiningRoom().get(Color.RED); i--){
                System.out.print(" ");
            }

            System.out.print(" ║ ");

            //printing RED professor (if exists)
            if(clientSchoolBoard.getProfessors().get(Color.RED) == 1){
                System.out.print(Color.RED.label);
                System.out.print("■");
                System.out.print(AnsiKeys.COLOR_RESET);
            }
            else{
                System.out.print(" ");
            }

            System.out.print(" ║ ");

            //printing first & second tower (if they exist)
            printTowerTableItems(clientSchoolBoard.getNumOfTowers(), clientSchoolBoard.getTowersColor(), 3);
            printTowerTableItems(clientSchoolBoard.getNumOfTowers(), clientSchoolBoard.getTowersColor(), 4);

            System.out.print(" ║");

            System.out.print("  ");
        }
        System.out.print("\n");
    }

    private void printSchoolBoardLine4(ArrayList<ClientSchoolBoard> clientSchoolBoards){

        for(ClientSchoolBoard clientSchoolBoard : clientSchoolBoards){

            System.out.print("║ ");
            //printing first & second entrance item (if they exist)
            printEntranceStudents(clientSchoolBoard.getEntrance(), 5);
            printEntranceStudents(clientSchoolBoard.getEntrance(), 6);

            System.out.print(" ║ ");

            //printing YELLOW students in diningRoom
            System.out.print(Color.YELLOW.label);
            for(int i=0; i<clientSchoolBoard.getDiningRoom().get(Color.YELLOW); i++){
                //System.out.print("●");
                System.out.print("o");
            }
            System.out.print(AnsiKeys.COLOR_RESET);

            for(int i=10; i>clientSchoolBoard.getDiningRoom().get(Color.YELLOW); i--){
                System.out.print(" ");
            }

            System.out.print(" ║ ");

            //printing GREEN professor (if exists)
            if(clientSchoolBoard.getProfessors().get(Color.YELLOW) == 1){
                System.out.print(Color.YELLOW.label);
                System.out.print("■");
                System.out.print(AnsiKeys.COLOR_RESET);
            }
            else{
                System.out.print(" ");
            }

            System.out.print(" ║ ");

            //printing first & second tower (if they exist)
            printTowerTableItems(clientSchoolBoard.getNumOfTowers(), clientSchoolBoard.getTowersColor(), 5);
            printTowerTableItems(clientSchoolBoard.getNumOfTowers(), clientSchoolBoard.getTowersColor(), 6);

            System.out.print(" ║");

            System.out.print("  ");
        }
        System.out.print("\n");
    }

    private void printSchoolBoardLine5(ArrayList<ClientSchoolBoard> clientSchoolBoards){

        for(ClientSchoolBoard clientSchoolBoard : clientSchoolBoards){

            System.out.print("║ ");
            //printing first & second entrance item (if they exist)
            printEntranceStudents(clientSchoolBoard.getEntrance(), 7);
            printEntranceStudents(clientSchoolBoard.getEntrance(), 8);

            System.out.print(" ║ ");

            //printing PINK students in diningRoom
            System.out.print(Color.PINK.label);
            for(int i=0; i<clientSchoolBoard.getDiningRoom().get(Color.PINK); i++){
                //System.out.print("●");
                System.out.print("o");
            }
            System.out.print(AnsiKeys.COLOR_RESET);

            for(int i=10; i>clientSchoolBoard.getDiningRoom().get(Color.PINK); i--){
                System.out.print(" ");
            }

            System.out.print(" ║ ");

            //printing GREEN professor (if exists)
            if(clientSchoolBoard.getProfessors().get(Color.PINK) == 1){
                System.out.print(Color.PINK.label);
                System.out.print("■");
                System.out.print(AnsiKeys.COLOR_RESET);
            }
            else{
                System.out.print(" ");
            }

            System.out.print(" ║ ");

            //printing first & second tower (if they exist)
            printTowerTableItems(clientSchoolBoard.getNumOfTowers(), clientSchoolBoard.getTowersColor(), 7);
            printTowerTableItems(clientSchoolBoard.getNumOfTowers(), clientSchoolBoard.getTowersColor(), 8);

            System.out.print(" ║");

            System.out.print("  ");
        }
        System.out.print("\n");
    }

    private void printSchoolBoardLine6(ArrayList<ClientSchoolBoard> clientSchoolBoards){

        for(ClientSchoolBoard clientSchoolBoard : clientSchoolBoards){

            System.out.print("║ ");
            //printing first & second entrance item (if they exist)
            printEntranceStudents(clientSchoolBoard.getEntrance(), 9);
            printEntranceStudents(clientSchoolBoard.getEntrance(), 10);

            System.out.print(" ║ ");

            //printing BLUE students in diningRoom
            System.out.print(Color.BLUE.label);
            for(int i=0; i<clientSchoolBoard.getDiningRoom().get(Color.BLUE); i++){
                //System.out.print("●");
                System.out.print("o");
            }
            System.out.print(AnsiKeys.COLOR_RESET);

            for(int i=10; i>clientSchoolBoard.getDiningRoom().get(Color.BLUE); i--){
                System.out.print(" ");
            }

            System.out.print(" ║ ");

            //printing GREEN professor (if exists)
            if(clientSchoolBoard.getProfessors().get(Color.BLUE) == 1){
                System.out.print(Color.BLUE.label);
                System.out.print("■");
                System.out.print(AnsiKeys.COLOR_RESET);
            }
            else{
                System.out.print(" ");
            }

            System.out.print(" ║ ");

            //printing first & second tower (if they exist)
            printTowerTableItems(clientSchoolBoard.getNumOfTowers(), clientSchoolBoard.getTowersColor(), 9);
            printTowerTableItems(clientSchoolBoard.getNumOfTowers(), clientSchoolBoard.getTowersColor(), 10);

            System.out.print(" ║");
            System.out.print("  ");
        }
        System.out.print("\n");
    }

    private void printSchoolBoardLine7(ArrayList<ClientSchoolBoard> clientSchoolBoards){

        for(int i=0; i<clientSchoolBoards.size();i++){
            System.out.print("╚════╩════════════╩═══╩════╝");
            System.out.print("  ");
        }
        System.out.print("\n");
    }

    /* CLOUD LAYOUT

        System.out.println("+--+\n" +
                        "|ss|\n" +
                        "|ss|\n" +
                        "+--+");

    */

    private void printCloudStudents(ArrayList<Color> students, int position){
        if(students.size() >= position){
            System.out.print(students.get(position-1).label);
            //System.out.print("●");
            System.out.print("o");
            System.out.print(AnsiKeys.COLOR_RESET);
        }
        else {
            System.out.print(" ");
        }
    }

    private void printCloudLine1(ArrayList<ClientCloud> clientClouds){

        for(int i=0; i<clientClouds.size(); i++){
            System.out.print("╔══╗");
            System.out.print("  ");
        }

        System.out.print("\n");
    }

    private void printCloudLine2(ArrayList<ClientCloud> clientClouds){

        for(ClientCloud clientCloud : clientClouds) {

            System.out.print("║");

            printCloudStudents(clientCloud.getStudents(), 1);
            printCloudStudents(clientCloud.getStudents(), 2);

            System.out.print("║");
            System.out.print("  ");
        }
        System.out.print("\n");
    }

    private void printCloudLine3(ArrayList<ClientCloud> clientClouds){

        for(ClientCloud clientCloud : clientClouds) {

            System.out.print("║");

            printCloudStudents(clientCloud.getStudents(), 3);
            printCloudStudents(clientCloud.getStudents(), 4);

            System.out.print("║");
            System.out.print("  ");
        }
        System.out.print("\n");
    }

    private void printCloudLine4(ArrayList<ClientCloud> clientClouds){

        for(int i=0; i<clientClouds.size(); i++){
            System.out.print("╚══╝");
            System.out.print("  ");
        }

        System.out.print("\n");
    }


    /*  CHARACTER LAYOUT

    public void printCharacter(){
        System.out.println("+------+\n" +
                "|ID   C|\n" +
                "| ssss |\n" +
                "|  ss  |\n" +
                "+------+");
    }

     */


    private void printCharacterStudents(Color[] students, int position){

        if(students.length >= position) {
            if (students[position - 1] != null) {
                System.out.print(students[position - 1].label);
                //System.out.print("●");
                System.out.print("o");
                System.out.print(AnsiKeys.COLOR_RESET);
            } else {
                System.out.print(" ");
            }
        }
        else {
            System.out.print(" ");
        }
    }

    private void printCharacterLine1(ArrayList<ClientCharacter> clientCharacters){
        for(int i=0; i<clientCharacters.size(); i++){
            System.out.print("╔══════╗");
            System.out.print("  ");
        }
        System.out.print("\n");
    }

    private void printCharacterLine2(ArrayList<ClientCharacter> clientCharacters){
        for(ClientCharacter clientCharacter : clientCharacters){
            System.out.print("║");

            System.out.print("ID");

            System.out.print("   ");

            System.out.print(Color.YELLOW.label);
            System.out.print(clientCharacter.getCost());
            System.out.print(AnsiKeys.COLOR_RESET);

            System.out.print("║");

            System.out.print("  ");

        }
        System.out.print("\n");
    }

    private void printCharacterLine3(ArrayList<ClientCharacter> clientCharacters){

        for(ClientCharacter clientCharacter : clientCharacters){

            System.out.print("║ ");

            if(clientCharacter.getStudents()!=null) {
                printCharacterStudents(clientCharacter.getStudents(), 1);
                printCharacterStudents(clientCharacter.getStudents(), 2);
                printCharacterStudents(clientCharacter.getStudents(), 3);
                printCharacterStudents(clientCharacter.getStudents(), 4);
            }
            else if(clientCharacter.getNoEntryTiles()!=-1){
                for(int i=0; i<clientCharacter.getNoEntryTiles(); i++){
                    System.out.print(AnsiKeys.COLOR_BRIGHT_RED);
                    System.out.print("X");
                    System.out.print(AnsiKeys.COLOR_RESET);
                }
                for(int i=4; i>clientCharacter.getNoEntryTiles();i--){
                    System.out.print(" ");
                }
            }
            else {
                System.out.print("    ");
            }

            System.out.print(" ║");

            System.out.print("  ");

        }
        System.out.print("\n");
    }

    private void printCharacterLine4(ArrayList<ClientCharacter> clientCharacters){

        for(ClientCharacter clientCharacter : clientCharacters){

            System.out.print("║ ");
            System.out.print(" ");

            if(clientCharacter.getStudents()!=null) {
                printCharacterStudents(clientCharacter.getStudents(), 5);
                printCharacterStudents(clientCharacter.getStudents(), 6);
            }
            else {
                System.out.print("  ");
            }
            System.out.print(" ");

            System.out.print(" ║");

            System.out.print("  ");
        }
        System.out.print("\n");
    }

    private void printCharacterLine5(ArrayList<ClientCharacter> clientCharacters){

        for(int i=0; i<clientCharacters.size(); i++){

            System.out.print("╚══════╝");
            System.out.print("  ");
        }
        System.out.print("\n");
    }


    /*  ASSISTANT LAYOUT

    public void printAssistant(){

        System.out.println("╔══╗\n" +
                "║R ║\n" +
                "║  ║\n" +
                "║SS║\n" +
                "╚══╝");
    }
    */

    private void printAssistantLine1(Assistant[] assistants){

        for(Assistant assistant : assistants){

            if(assistant != null) {
                System.out.print("╔══╗");
            }
            else {
                System.out.print("    ");
            }
            System.out.print(" ");
        }
        System.out.print("\n");

    }

    private void printAssistantLine2(Assistant[] assistants){

        for(Assistant assistant : assistants){

            if(assistant != null) {

                System.out.print("║");
                System.out.printf("%-2s", assistant.getRank());
                System.out.print("║");
            }
            else {
                System.out.print("    ");
            }
            System.out.print(" ");
        }
        System.out.print("\n");
    }

    private void printAssistantLine3(Assistant[] assistants){

        for(Assistant assistant : assistants){

            if(assistant != null){
                System.out.print("║  ║");
            }
            else  {
                System.out.print("    ");
            }
            System.out.print(" ");
        }
        System.out.print("\n");

    }

    private void printAssistantLine4(Assistant[] assistants){

        for(Assistant assistant : assistants){
            if(assistant != null){

                System.out.print("║");

                System.out.print("»");
                System.out.print(assistant.getMovements());

                System.out.print("║");

            }
            else {
                System.out.print("    ");
            }
            System.out.print(" ");
        }
        System.out.print("\n");
    }

    private void printAssistantLine5(Assistant[] assistants){

        for(Assistant assistant : assistants){

            if(assistant != null) {
                System.out.print("╚══╝");
            }
            else {
                System.out.print("    ");
            }
            System.out.print(" ");
        }
        System.out.print("\n");

    }

}
