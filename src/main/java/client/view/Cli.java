package client.view;

import server.model.Color;

import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;

public class Cli extends View {

    private Scanner stdin = new Scanner(System.in);

    @Override
    public int askNumOfPlayers() {
        int numOfPlayers = 0;
        while (true) {
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
        while (true) {
            System.out.println("Do you want to play with advanced settings? (y/n)");
            advancedSettings = stdin.nextLine();
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
    public String askName() {
        System.out.println("Please insert your name: ");
        return stdin.nextLine();
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public int askAssistant() {
        System.out.println("Choose an Assistant to play (insert the rank)");
        int input;

        while(true){
            input = stdin.nextInt();
            if(0<input && input<=10){
                return input;
            }
            else {
                System.out.println("Incorrect value, try again");
            }
        }
    }

    @Override
    public int chooseActionStudent(boolean characterActivated) {
        if(characterActivated==false){
            System.out.println("Choose an action:");
            System.out.println("1. Move student     2. Activate character");
            int input;

            while(true){
                input = stdin.nextInt();
                if(input == 1 || input == 2){
                    return input;
                }
                else {
                    System.out.println("Incorrect value, try again");
                }
            }
        }
        else {
            System.out.println("Choose an action:");
            System.out.println("1. Move student");
            int input;

            while(true){
                input = stdin.nextInt();
                if(input == 1){
                    return input;
                }
                else {
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

    public int askStudentDestination(){

        System.out.println("Insert the number of the island you want to move your student or " +
                "insert 0 to move it to your dining room");

        int input;

        while(true){

            input = stdin.nextInt();
            if(0 <= input && input <= 12){
                return input;
            }
            else {
                System.out.println("Incorrect value, try again");
            }
        }
    }


}
