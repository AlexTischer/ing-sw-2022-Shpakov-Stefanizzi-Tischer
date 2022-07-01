package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.model.ClientGameBoard;
import it.polimi.ingsw.client.view.CLI.utils.AnsiKeys;
import it.polimi.ingsw.client.view.CLI.utils.Printer;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.model.Color;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Cli extends View {

    private Scanner stdin = new Scanner(System.in);
    private Printer printer = new Printer();


    @Override
    public void showLobby(List<String> userNames) {
        try {
            printer.clearConsole();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        printer.showLobby(userNames);
    }

    //all ask methods return index starting from 1 upto number of certain objects
    @Override
    public int askNumOfPlayers() {
        int numOfPlayers = 0;
        while(true){
            try {
                System.out.println("Please insert number of players (2, 3, 4):\n");
                numOfPlayers = Integer.parseInt(stdin.nextLine());
                stdin.reset();
                break;
            } catch (NumberFormatException e) {
                System.out.println("Incorrect number of players value. Please try again");
            }
        }
        return numOfPlayers;
    }

    @Override
    public String askAdvancedSettings() {
        String advancedSettings;
        while(true) {
            System.out.println("Do you want to play with advanced settings? (y/n)");
            advancedSettings= stdin.nextLine();
            if (advancedSettings.toLowerCase(Locale.ROOT).equals("y")) {
                advancedSettings = "true";
                break;
            } else if (advancedSettings.toLowerCase(Locale.ROOT).equals("n")) {
                advancedSettings = "false";
                break;
            } else
                System.out.println("Incorrect advanced settings response. Please try again");
        }
        return advancedSettings;
    }

    @Override
    public String askName(){
        System.out.println("Please insert your name: ");
        String input = stdin.nextLine().toUpperCase();
        if (!input.contains(" ") && !input.isEmpty()){
            return input;
        }
        else {
            System.out.println("The name cannot be empty or contain spaces");
            return askName();
        }
    }

    public void printMessage(String message){
        System.out.println(message);
    }

    public void printErrorMessage(String message){
        System.out.print(AnsiKeys.COLOR_BRIGHT_RED);
        System.out.println(message);
        System.out.print(AnsiKeys.COLOR_RESET);
    }

    @Override
    public Color askStudentColorFromCharacter() {
        return askStudentColor();
    }

    @Override
    public Color askStudentColorFromBox(){
        return askStudentColor();
    }

    public Color askStudentColorFromDiningRoom(){
        return askStudentColor();
    }

    public Color askStudentColorFromEntrance(){
        return askStudentColor();
    }

    public int askAssistant() {
        System.out.println("Choose an Assistant to play (insert the rank)");
        int input;

        while(true){
            try {
                input = Integer.parseInt(stdin.nextLine());
                if (0 < input && input <= 10) {
                    return input;
                } else {
                    throw new NumberFormatException();
                }
            }
            catch (NumberFormatException e){
                System.out.println("Incorrect value, try again");
            }
        }
    }

    @Override
    public int chooseActionStudent(boolean characterActivated) {
        if(!characterActivated){
            System.out.println("Choose an action:");
            System.out.println("1. Move student     2. Activate character");
            int input;

            while(true){
                try {
                    input = Integer.parseInt(stdin.nextLine());
                    if (input == 1 || input == 2) {
                        return input;
                    }
                    else {
                        throw new NumberFormatException();
                    }
                }
                catch (NumberFormatException e){
                    System.out.println("Incorrect value, try again");
                }
            }
        }
        else {
            System.out.println("Choose an action:");
            System.out.println("1. Move student");
            int input;

            while(true){
                try{
                    input = Integer.parseInt(stdin.nextLine());
                    if(input == 1){
                        return input;
                    }
                    else {
                        throw new NumberFormatException();}
                }
                catch (NumberFormatException e){
                    System.out.println("Incorrect value, try again");
                }
            }
        }
    }

    public Color askStudentColor() {

        System.out.println("Choose the color:");
        System.out.println("red / green / yellow / pink / blue");
        String input;
        Color color = null;

        while (true) {

            input = stdin.nextLine().toLowerCase();

            if (input.equals("red") || input.equals("green") ||
                    input.equals("yellow") || input.equals("pink") || input.equals("blue")) {

                return Color.getColorByLabel(input).get();
            }
            else {
                System.out.println("Incorrect value, try again");
            }
        }
    }

    public int askIslandNumber(){

        int input;

        while(true){

            try {
                input = Integer.parseInt(stdin.nextLine());
                if(0 < input && input <= getController().getGameBoard().getIslands().size()){
                    return input;
                }
                else
                    throw new NumberFormatException();
            }
            catch (NumberFormatException e){
                System.out.println("Incorrect value, try again");
            }

        }
    }

    public int askStudentDestination(){

        System.out.println("Insert the number of the island you want to move your student or " +
                "insert 0 to move it to your dining room");

        int input;

        while(true){

            try {
                input = Integer.parseInt(stdin.nextLine());
                if(0 <= input && input <= getController().getGameBoard().getIslands().size()){
                    return input;
                }
                else
                    throw new NumberFormatException();
            }
            catch (NumberFormatException e){
                System.out.println("Incorrect value, try again");
            }

        }
    }



    public void showModel(ClientGameBoard gameBoard){
        try {
            printer.clearConsole();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        printer.showModel(gameBoard);
    }

    public int chooseActionMotherNature(boolean characterActivated){
        if(!characterActivated){
            System.out.println("Choose an action:");
            System.out.println("1. Move Mother Nature     2. Activate character");
            int input;

            while(true){
                try {
                    input = Integer.parseInt(stdin.nextLine());
                    if (input == 1 || input == 2) {
                        return input;
                    }
                    else {
                        throw new NumberFormatException();
                    }
                }
                catch (NumberFormatException e){
                    System.out.println("Incorrect value, try again");
                }
            }
        }
        else {
            System.out.println("Choose an action:");
            System.out.println("1. Move Mother Nature");
            int input;

            while(true){
                try{
                    input = Integer.parseInt(stdin.nextLine());
                    if(input == 1){
                        return input;
                    }
                    else {
                        throw new NumberFormatException();
                    }
                }
                catch (NumberFormatException e){
                    System.out.println("Incorrect value, try again");
                }
            }
        }
    }

    public int askCloudNumber() {
        System.out.println("Insert the number of the cloud you want to use");

        int input;

        while(true){

            try {
                input = Integer.parseInt(stdin.nextLine());

                if(0 < input && input <= getController().getGameBoard().getClouds().size()){
                    return input;
                }
                else
                    throw new NumberFormatException();
            }
            catch (NumberFormatException e){
                System.out.println("Incorrect value, try again");
            }

        }
    }

    public int chooseActionClouds(boolean characterActivated) {
        if(characterActivated==false){
            System.out.println("Choose an action:");
            System.out.println("1. Use Cloud to refill Entrance     2. Activate character");
            int input;

            while(true){
                try {
                    input = Integer.parseInt(stdin.nextLine());
                    if (input == 1 || input == 2) {
                        return input;
                    }
                    else {
                        throw new NumberFormatException();
                    }
                }
                catch (NumberFormatException e){
                    System.out.println("Incorrect value, try again");
                }
            }
        }
        else {
            System.out.println("Choose an action:");
            System.out.println("1. Use Cloud to refill Entrance");
            int input;

            while(true){
                try{
                    input = Integer.parseInt(stdin.nextLine());
                    if(input == 1){
                        return input;
                    }
                    else {
                        throw new NumberFormatException();
                    }
                }
                catch (NumberFormatException e){
                    System.out.println("Incorrect value, try again");
                }
            }
        }
    }

    public int askMotherNatureSteps() {
        System.out.println("Insert the number of steps you want Mother Nature to do");

        int input;

        while(true){

            try {
                input = Integer.parseInt(stdin.nextLine());
                if (input <= 0)
                    throw new NumberFormatException();

                return input;
            }
            catch (NumberFormatException e){
                System.out.println("Incorrect value, try again");
            }

        }
    }

    public int askCharacterNumber() {

        System.out.println("Insert the number of the character (1/2/3) you want to buy or -1 to cancel");

        int input;

        while(true){

            try {
                input = Integer.parseInt(stdin.nextLine());

                if(0 < input && input <= 3 || input==-1){
                    return input;
                }
                else
                    throw new NumberFormatException();
            }
            catch (NumberFormatException e){
                System.out.println("Incorrect value, try again");
            }
        }
    }

    public boolean askBoolean(String message) {
        String answer;
        boolean booleanAnswer;
        while(true) {
            System.out.println(message + " (y/n)");
            answer = stdin.nextLine();
            if (answer.toLowerCase(Locale.ROOT).equals("y")) {
                booleanAnswer = true;
                break;
            } else if (answer.toLowerCase(Locale.ROOT).equals("n")) {
                booleanAnswer = false;
                break;
            } else
                System.out.println("Incorrect response. Please try again");
        }
        return booleanAnswer;
    }



    public void printEndGameMessage(String message) {
        printMessage(message);
    }
}
