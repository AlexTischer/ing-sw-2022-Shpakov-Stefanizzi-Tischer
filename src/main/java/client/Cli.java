package client;

import client.controller.ClientController;

import java.util.Locale;
import java.util.Scanner;

public class Cli extends View {

    private Scanner stdin = new Scanner(System.in);

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
        return stdin.nextLine();
    }

    public void printMessage(String message){
        System.out.println(message);
    }
}
