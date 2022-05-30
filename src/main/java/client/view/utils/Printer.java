package client.view.utils;

import client.model.*;
import server.model.Assistant;
import server.model.Color;
import server.model.TowerColor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Printer {

    public void showModel(ClientGameBoard gameBoard){
        System.out.print("Islands:\n");
        printIslands(gameBoard.getIslands(), gameBoard.getPositionOfMotherNature());

        System.out.print("\nSchoolboards:\n");
        printSchoolBoards(gameBoard.getPlayers(), gameBoard.getCurrentPlayerName());

        System.out.print("\nClouds:\n");
        printClouds(gameBoard.getClouds());

        System.out.print("\nCharacters:\n");
        printCharacters(gameBoard.getPlayedCharacters());

        System.out.print("\nAssistants:\n");
        printAssistants(gameBoard.getPlayer(gameBoard.getClientName()).getAssistants());
    }

    public void clearConsole() throws IOException, InterruptedException {
        if(System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("windows")){
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        }
    }

    private void printColored(String str, String color){
        System.out.print(color);
        System.out.print(str);
        System.out.print(AnsiKeys.COLOR_RESET);
    }

    private void printIslands(List<ClientIsland> islands, int positionOfMotherNature){
        printIslandLine1(islands);
        printIslandLine2(islands, positionOfMotherNature);
        printIslandLine3(islands);
        printIslandLine4(islands);
        printIslandLine5(islands);
    }

    private void printSchoolBoards(ArrayList<ClientPlayer> clientPlayers, String currentPlayerName){
        printSchoolBoardLine1(clientPlayers, currentPlayerName);
        printSchoolBoardLine2(clientPlayers, currentPlayerName);
        printSchoolBoardLine3(clientPlayers, currentPlayerName);
        printSchoolBoardLine4(clientPlayers, currentPlayerName);
        printSchoolBoardLine5(clientPlayers, currentPlayerName);
        printSchoolBoardLine6(clientPlayers, currentPlayerName);
        printSchoolBoardLine7(clientPlayers, currentPlayerName);
        printSchoolBoardLine8(clientPlayers, currentPlayerName);
        printSchoolBoardLine9(clientPlayers, currentPlayerName);
        printSchoolBoardLine10(clientPlayers, currentPlayerName);
    }

    private void printClouds(List<ClientCloud> clientClouds){
        printCloudLine1(clientClouds);
        printCloudLine2(clientClouds);
        printCloudLine3(clientClouds);
        printCloudLine4(clientClouds);
    }

    private void printCharacters(ClientCharacter[] characters){
        printCharacterLine1(characters);
        printCharacterLine2(characters);
        printCharacterLine3(characters);
        printCharacterLine4(characters);
        printCharacterLine5(characters);
    }

    private void printAssistants(Assistant[] assistants){
        printAssistantLine1(assistants);
        printAssistantLine2(assistants);
        printAssistantLine3(assistants);
        printAssistantLine4(assistants);
        printAssistantLine5(assistants);
    }

    private void printIslandStudents(int numOfStudents, Color color){
        if(numOfStudents!=0) {
            printColored(String.format("%2s", numOfStudents), color.ansi);
        }
        else {
            System.out.print("  ");
        }
    }

    private void printIslandLine1(List<ClientIsland> islands) {
        for (int i=0; i<islands.size(); i++) {
            System.out.print("╔══════╗");
            System.out.print(" ");
        }
        System.out.print("\n");
    }

    private void printIslandLine2(List<ClientIsland> islands, int positionOfMotherNature) {
        int cnt = 0;
        for (ClientIsland island : islands) {

            System.out.print("║ ");

            if(cnt==positionOfMotherNature) {
                System.out.print("MN");
            }
            else{
                System.out.print("  ");
            }

            System.out.print(" ");


            if(island.isNoEntry()){
                printColored("X", AnsiKeys.COLOR_BRIGHT_RED);
            }
            else {
                System.out.print(" ");
            }

            System.out.print(" ║");
            System.out.print(" ");

            cnt++;
        }
        System.out.print("\n");
    }

    private void printIslandLine3(List<ClientIsland> islands) {
        for (ClientIsland island : islands) {

            System.out.print("║");

            if(island.getNumOfTowers()>0) {
                System.out.print(island.getNumOfTowers());
                System.out.print(AnsiKeys.COLOR_BACKGROUND_GREEN);
                printColored("T", island.getTowersColor().ansi);
            }
            else{
                System.out.print("  ");
            }

            printIslandStudents(island.getStudents().get(Color.GREEN), Color.GREEN);
            printIslandStudents(island.getStudents().get(Color.RED), Color.RED);

            System.out.print("║");
            System.out.print(" ");
        }
        System.out.print("\n");
    }

    private void printIslandLine4(List<ClientIsland> islands) {
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

    private void printIslandLine5(List<ClientIsland> islands){
        for (int i=0; i<islands.size(); i++) {
            System.out.print("╚══════╝");
            System.out.print(" ");
        }
        System.out.print("\n");
    }


    private void printEntranceStudents(List<Color> students, int position){
        if(students.size() >= position){
            printColored("o", students.get(position-1).ansi);
        }
        else {
            System.out.print(" ");
        }
    }

    private void printTowerTableItems(int numOfTowers, TowerColor towerColor, int position){
        if(numOfTowers>=position){

            System.out.print(towerColor.ansi);
            System.out.print("T");

        }
        else {
            System.out.print(" ");
        }
    }


   /* SCHOOLBOARD LAYOUT


    System.out.println("+-----------------------------------+\n" +      1
            "|Name.............  Coins: xx       |\n" +           2
            "|+----+------------+---+----+       |\n" +     done  3
            "|| ii | pppppppppp | P | TT |  +--+ |\n" +     done  4
            "|| ii | pppppppppp | P | TT |  |R | |\n" +     done  5
            "|| ii | pppppppppp | P | TT |  |  | |\n" +     done  6
            "|| ii | pppppppppp | P | TT |  |St| |\n" +     done  7
            "|| ii | pppppppppp | P | TT |  +--+ |\n" +     done  8
            "|+----+------------+---+----+       |\n" +     done  9
            "+-----------------------------------+");    10
}


    */






    private void printSchoolBoardLine1(ArrayList<ClientPlayer> clientPlayers, String currentPlayerName){

        String frameColor;

        for(ClientPlayer clientPlayer : clientPlayers){

            //setting frame color for current player
            if(clientPlayer.getName().equals(currentPlayerName)){
                frameColor = AnsiKeys.CURRENTPLAYER_FRAME_COLOR;
            }
            else {
                frameColor = AnsiKeys.COLOR_RESET;
            }

            printColored("╔══════════════════════════════════╗", frameColor);
            System.out.print("  ");
        }
        System.out.print("\n");

    }

    private void printSchoolBoardLine2(ArrayList<ClientPlayer> clientPlayers, String currentPlayerName){

        String frameColor;

        for(ClientPlayer clientPlayer : clientPlayers) {

            //setting frame color for current player
            if (clientPlayer.getName().equals(currentPlayerName)) {
                frameColor = AnsiKeys.CURRENTPLAYER_FRAME_COLOR;
            } else {
                frameColor = AnsiKeys.COLOR_RESET;
            }

            printColored("║", frameColor);

            System.out.print("Name: ");

            if(clientPlayer.getName().length()<=13){
                System.out.printf("%-14s", clientPlayer.getName());
            }
            else{
                System.out.print(clientPlayer.getName().substring(0,9));
                System.out.print("... ");
            }

            System.out.print("Coins: ");
            System.out.printf("%2s", clientPlayer.getCoins());

            printColored("     ║", frameColor);

            System.out.print("  ");
        }
        System.out.print("\n");
    }

    private void printSchoolBoardLine3(ArrayList<ClientPlayer> clientPlayers, String currentPlayerName){

        String frameColor;

        for(ClientPlayer clientPlayer : clientPlayers){

            //setting frame color for current player
            if(clientPlayer.getName().equals(currentPlayerName)){
                frameColor = AnsiKeys.CURRENTPLAYER_FRAME_COLOR;
            }
            else {
                frameColor = AnsiKeys.COLOR_RESET;
            }

            printColored("║", frameColor);
            System.out.print("╔════╦════════════╦═══╦════╗      ");

            printColored("║", frameColor);
            System.out.print("  ");
        }
        System.out.print("\n");
    }

    private void printSchoolBoardLine4(ArrayList<ClientPlayer> clientPlayers, String currentPlayerName){

        String frameColor;

        for(ClientPlayer clientPlayer : clientPlayers){

            //setting frame color for current player
            if(clientPlayer.getName().equals(currentPlayerName)){
                frameColor = AnsiKeys.CURRENTPLAYER_FRAME_COLOR;
            }
            else {
                frameColor = AnsiKeys.COLOR_RESET;
            }

            printColored("║", frameColor);

            System.out.print("║ ");
            //printing first & second entrance item (if they exist)
            printEntranceStudents(clientPlayer.getSchoolBoard().getEntrance(), 1);
            printEntranceStudents(clientPlayer.getSchoolBoard().getEntrance(), 2);

            System.out.print(" ║ ");

            //printing GREEN students in diningRoom
            System.out.print(Color.GREEN.ansi);
            for(int i=0; i<clientPlayer.getSchoolBoard().getDiningRoom().get(Color.GREEN); i++){
                //System.out.print("●");
                System.out.print("o");
            }
            System.out.print(AnsiKeys.COLOR_RESET);

            for(int i=10; i>clientPlayer.getSchoolBoard().getDiningRoom().get(Color.GREEN); i--){
                System.out.print(" ");
            }

            System.out.print(" ║ ");

            //printing GREEN professor (if exists)
            if(clientPlayer.getSchoolBoard().getProfessors().get(Color.GREEN) == 1){
                printColored("■", Color.GREEN.ansi);
            }
            else{
                System.out.print(" ");
            }

            System.out.print(" ║");

            //adding background
            System.out.print(AnsiKeys.COLOR_BACKGROUND_GREEN);
            System.out.print(" ");
            //printing first & second tower (if they exist)
            printTowerTableItems(clientPlayer.getSchoolBoard().getNumOfTowers(), clientPlayer.getSchoolBoard().getTowersColor(), 1);
            printTowerTableItems(clientPlayer.getSchoolBoard().getNumOfTowers(), clientPlayer.getSchoolBoard().getTowersColor(), 2);
            System.out.print(" ");
            System.out.print(AnsiKeys.COLOR_RESET);

            System.out.print("║ ");

            //printing played assistant if exists
            if(clientPlayer.getPlayedAssistant()!=null) {
                System.out.print("╔══╗");
            }
            else {
                System.out.print("    ");
            }

            printColored(" ║", frameColor);

            System.out.print("  ");
        }
        System.out.print("\n");
    }

    private void printSchoolBoardLine5(ArrayList<ClientPlayer> clientPlayers, String currentPlayerName){

        String frameColor;

        for(ClientPlayer clientPlayer : clientPlayers){

            //setting frame color for current player
            if(clientPlayer.getName().equals(currentPlayerName)){
                frameColor = AnsiKeys.CURRENTPLAYER_FRAME_COLOR;
            }
            else {
                frameColor = AnsiKeys.COLOR_RESET;
            }

            printColored("║", frameColor);

            System.out.print("║ ");
            //printing first & second entrance item (if they exist)
            printEntranceStudents(clientPlayer.getSchoolBoard().getEntrance(), 3);
            printEntranceStudents(clientPlayer.getSchoolBoard().getEntrance(), 4);

            System.out.print(" ║ ");

            //printing RED students in diningRoom
            System.out.print(Color.RED.ansi);
            for(int i=0; i<clientPlayer.getSchoolBoard().getDiningRoom().get(Color.RED); i++){
                //System.out.print("●");
                System.out.print("o");
            }
            System.out.print(AnsiKeys.COLOR_RESET);

            for(int i=10; i>clientPlayer.getSchoolBoard().getDiningRoom().get(Color.RED); i--){
                System.out.print(" ");
            }

            System.out.print(" ║ ");

            //printing RED professor (if exists)
            if(clientPlayer.getSchoolBoard().getProfessors().get(Color.RED) == 1){
                printColored("■", Color.RED.ansi);
            }
            else{
                System.out.print(" ");
            }

            System.out.print(" ║");

            //adding background
            System.out.print(AnsiKeys.COLOR_BACKGROUND_GREEN);
            System.out.print(" ");
            //printing first & second tower (if they exist)
            printTowerTableItems(clientPlayer.getSchoolBoard().getNumOfTowers(), clientPlayer.getSchoolBoard().getTowersColor(), 3);
            printTowerTableItems(clientPlayer.getSchoolBoard().getNumOfTowers(), clientPlayer.getSchoolBoard().getTowersColor(), 4);
            System.out.print(" ");
            System.out.print(AnsiKeys.COLOR_RESET);

            System.out.print("║ ");

            //printing played assistant if exists
            if(clientPlayer.getPlayedAssistant()!=null) {
                System.out.print("║");
                System.out.print(clientPlayer.getPlayedAssistant().getRank());
                System.out.print(" ║");
            }
            else {
                System.out.print("    ");
            }

            printColored(" ║", frameColor);

            System.out.print("  ");
        }
        System.out.print("\n");
    }

    private void printSchoolBoardLine6(ArrayList<ClientPlayer> clientPlayers, String currentPlayerName){

        String frameColor;

        for(ClientPlayer clientPlayer : clientPlayers){

            //setting frame color for current player
            if(clientPlayer.getName().equals(currentPlayerName)){
                frameColor = AnsiKeys.CURRENTPLAYER_FRAME_COLOR;
            }
            else {
                frameColor = AnsiKeys.COLOR_RESET;
            }

            printColored("║", frameColor);
            System.out.print("║ ");
            //printing first & second entrance item (if they exist)
            printEntranceStudents(clientPlayer.getSchoolBoard().getEntrance(), 5);
            printEntranceStudents(clientPlayer.getSchoolBoard().getEntrance(), 6);

            System.out.print(" ║ ");

            //printing YELLOW students in diningRoom
            System.out.print(Color.YELLOW.ansi);
            for(int i=0; i<clientPlayer.getSchoolBoard().getDiningRoom().get(Color.YELLOW); i++){
                //System.out.print("●");
                System.out.print("o");
            }
            System.out.print(AnsiKeys.COLOR_RESET);

            for(int i=10; i>clientPlayer.getSchoolBoard().getDiningRoom().get(Color.YELLOW); i--){
                System.out.print(" ");
            }

            System.out.print(" ║ ");

            //printing GREEN professor (if exists)
            if(clientPlayer.getSchoolBoard().getProfessors().get(Color.YELLOW) == 1){
                printColored("■", Color.YELLOW.ansi);
            }
            else{
                System.out.print(" ");
            }

            System.out.print(" ║");

            System.out.print(AnsiKeys.COLOR_BACKGROUND_GREEN);
            System.out.print(" ");
            //printing first & second tower (if they exist)
            printTowerTableItems(clientPlayer.getSchoolBoard().getNumOfTowers(), clientPlayer.getSchoolBoard().getTowersColor(), 5);
            printTowerTableItems(clientPlayer.getSchoolBoard().getNumOfTowers(), clientPlayer.getSchoolBoard().getTowersColor(), 6);
            System.out.print(" ");
            System.out.print(AnsiKeys.COLOR_RESET);

            System.out.print("║ ");

            //printing played assistant if exists
            if(clientPlayer.getPlayedAssistant()!=null) {
                System.out.print("║  ║");
            }
            else {
                System.out.print("    ");
            }

            printColored(" ║", frameColor);

            System.out.print("  ");
        }
        System.out.print("\n");
    }

    private void printSchoolBoardLine7(ArrayList<ClientPlayer> clientPlayers, String currentPlayerName){

        String frameColor;

        for(ClientPlayer clientPlayer : clientPlayers){

            //setting frame color for current player
            if(clientPlayer.getName().equals(currentPlayerName)){
                frameColor = AnsiKeys.CURRENTPLAYER_FRAME_COLOR;
            }
            else {
                frameColor = AnsiKeys.COLOR_RESET;
            }

            printColored("║", frameColor);
            System.out.print("║ ");
            //printing first & second entrance item (if they exist)
            printEntranceStudents(clientPlayer.getSchoolBoard().getEntrance(), 7);
            printEntranceStudents(clientPlayer.getSchoolBoard().getEntrance(), 8);

            System.out.print(" ║ ");

            //printing PINK students in diningRoom
            System.out.print(Color.PINK.ansi);
            for(int i=0; i<clientPlayer.getSchoolBoard().getDiningRoom().get(Color.PINK); i++){
                //System.out.print("●");
                System.out.print("o");
            }
            System.out.print(AnsiKeys.COLOR_RESET);

            for(int i=10; i>clientPlayer.getSchoolBoard().getDiningRoom().get(Color.PINK); i--){
                System.out.print(" ");
            }

            System.out.print(" ║ ");

            //printing GREEN professor (if exists)
            if(clientPlayer.getSchoolBoard().getProfessors().get(Color.PINK) == 1){
                printColored("■", Color.PINK.ansi);
            }
            else{
                System.out.print(" ");
            }

            System.out.print(" ║");

            //adding background
            System.out.print(AnsiKeys.COLOR_BACKGROUND_GREEN);
            System.out.print(" ");
            //printing first & second tower (if they exist)
            printTowerTableItems(clientPlayer.getSchoolBoard().getNumOfTowers(), clientPlayer.getSchoolBoard().getTowersColor(), 7);
            printTowerTableItems(clientPlayer.getSchoolBoard().getNumOfTowers(), clientPlayer.getSchoolBoard().getTowersColor(), 8);
            System.out.print(" ");
            System.out.print(AnsiKeys.COLOR_RESET);

            System.out.print("║ ");

            if(clientPlayer.getPlayedAssistant()!=null) {
                System.out.print("║");
                System.out.print("»");
                System.out.print(clientPlayer.getPlayedAssistant().getMovements());
                System.out.print("║");
            }
            else {
                System.out.print("    ");
            }

            printColored(" ║", frameColor);
            System.out.print("  ");
        }
        System.out.print("\n");
    }

    private void printSchoolBoardLine8(ArrayList<ClientPlayer> clientPlayers, String currentPlayerName){

        String frameColor;

        for(ClientPlayer clientPlayer : clientPlayers){

            //setting frame color for current player
            if(clientPlayer.getName().equals(currentPlayerName)){
                frameColor = AnsiKeys.CURRENTPLAYER_FRAME_COLOR;
            }
            else {
                frameColor = AnsiKeys.COLOR_RESET;
            }

            printColored("║", frameColor);

            System.out.print("║ ");
            //printing first & second entrance item (if they exist)
            printEntranceStudents(clientPlayer.getSchoolBoard().getEntrance(), 9);
            printEntranceStudents(clientPlayer.getSchoolBoard().getEntrance(), 10);

            System.out.print(" ║ ");

            //printing BLUE students in diningRoom
            System.out.print(Color.BLUE.ansi);
            for(int i=0; i<clientPlayer.getSchoolBoard().getDiningRoom().get(Color.BLUE); i++){
                //System.out.print("●");
                System.out.print("o");
            }
            System.out.print(AnsiKeys.COLOR_RESET);

            for(int i=10; i>clientPlayer.getSchoolBoard().getDiningRoom().get(Color.BLUE); i--){
                System.out.print(" ");
            }

            System.out.print(" ║ ");

            //printing GREEN professor (if exists)
            if(clientPlayer.getSchoolBoard().getProfessors().get(Color.BLUE) == 1){
                printColored("■", Color.BLUE.ansi);
            }
            else{
                System.out.print(" ");
            }

            System.out.print(" ║");

            //adding background
            System.out.print(AnsiKeys.COLOR_BACKGROUND_GREEN);
            System.out.print(" ");
            //printing first & second tower (if they exist)
            printTowerTableItems(clientPlayer.getSchoolBoard().getNumOfTowers(), clientPlayer.getSchoolBoard().getTowersColor(), 9);
            printTowerTableItems(clientPlayer.getSchoolBoard().getNumOfTowers(), clientPlayer.getSchoolBoard().getTowersColor(), 10);
            System.out.print(" ");
            System.out.print(AnsiKeys.COLOR_RESET);

            System.out.print("║ ");

            //printing played assistant if exists
            if(clientPlayer.getPlayedAssistant()!=null) {
                System.out.print("╚══╝");
            }
            else {
                System.out.print("    ");
            }

            printColored(" ║", frameColor);
            System.out.print("  ");
        }
        System.out.print("\n");
    }

    private void printSchoolBoardLine9(ArrayList<ClientPlayer> clientPlayers, String currentPlayerName){

        String frameColor;

        for(ClientPlayer clientPlayer : clientPlayers){

            //setting frame color for current player
            if(clientPlayer.getName().equals(currentPlayerName)){
                frameColor = AnsiKeys.CURRENTPLAYER_FRAME_COLOR;
            }
            else {
                frameColor = AnsiKeys.COLOR_RESET;
            }

            printColored("║", frameColor);
            System.out.print("╚════╩════════════╩═══╩════╝      ");
            printColored("║", frameColor);
            System.out.print("  ");
        }
        System.out.print("\n");
    }


    private void printSchoolBoardLine10(ArrayList<ClientPlayer> clientPlayers, String currentPlayerName){

        String frameColor;

        for(ClientPlayer clientPlayer : clientPlayers){

            //setting frame color for current player
            if(clientPlayer.getName().equals(currentPlayerName)){
                frameColor = AnsiKeys.CURRENTPLAYER_FRAME_COLOR;
            }
            else {
                frameColor = AnsiKeys.COLOR_RESET;
            }

            printColored("╚══════════════════════════════════╝", frameColor);
            System.out.print("  ");
        }
        System.out.print("\n");

    }



    private void printCloudStudents(ArrayList<Color> students, int position){
        if(students.size() >= position){
            printColored("o", students.get(position-1).ansi );
        }
        else {
            System.out.print(" ");
        }
    }

    private void printCloudLine1(List<ClientCloud> clientClouds){

        for(int i=0; i<clientClouds.size(); i++){
            System.out.print("╔══╗");
            System.out.print("  ");
        }

        System.out.print("\n");
    }

    private void printCloudLine2(List<ClientCloud> clientClouds){

        for(ClientCloud clientCloud : clientClouds) {

            System.out.print("║");

            printCloudStudents(clientCloud.getStudents(), 1);
            printCloudStudents(clientCloud.getStudents(), 2);

            System.out.print("║");
            System.out.print("  ");
        }
        System.out.print("\n");
    }

    private void printCloudLine3(List<ClientCloud> clientClouds){

        for(ClientCloud clientCloud : clientClouds) {

            System.out.print("║");

            printCloudStudents(clientCloud.getStudents(), 3);
            printCloudStudents(clientCloud.getStudents(), 4);

            System.out.print("║");
            System.out.print("  ");
        }
        System.out.print("\n");
    }

    private void printCloudLine4(List<ClientCloud> clientClouds){

        for(int i=0; i<clientClouds.size(); i++){
            System.out.print("╚══╝");
            System.out.print("  ");
        }

        System.out.print("\n");
    }





    private void printCharacterStudents(Color[] students, int position){

        if(students.length >= position) {
            if (students[position - 1] != null) {
                printColored("o", students[position - 1].ansi);
            } else {
                System.out.print(" ");
            }
        }
        else {
            System.out.print(" ");
        }
    }

    private void printCharacterLine1(ClientCharacter[] clientCharacters){
        for(int i=0; i<clientCharacters.length; i++){
            System.out.print("╔══════╗");
            System.out.print("  ");
        }
        System.out.print("\n");
    }

    private void printCharacterLine2(ClientCharacter[] clientCharacters){
        for(ClientCharacter clientCharacter : clientCharacters){
            System.out.print("║");

            System.out.print("C");
            System.out.printf("%-2s", clientCharacter.getId());

            System.out.print("  ");

            System.out.print(Color.YELLOW.ansi);
            System.out.print(clientCharacter.getCost());
            System.out.print(AnsiKeys.COLOR_RESET);

            System.out.print("║");

            System.out.print("  ");

        }
        System.out.print("\n");
    }

    private void printCharacterLine3(ClientCharacter[] clientCharacters){

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
                    printColored("X", AnsiKeys.COLOR_BRIGHT_RED);
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

    private void printCharacterLine4(ClientCharacter[] clientCharacters){

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

    private void printCharacterLine5(ClientCharacter[] clientCharacters){

        for(int i=0; i<clientCharacters.length; i++){

            System.out.print("╚══════╝");
            System.out.print("  ");
        }
        System.out.print("\n");
    }




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
