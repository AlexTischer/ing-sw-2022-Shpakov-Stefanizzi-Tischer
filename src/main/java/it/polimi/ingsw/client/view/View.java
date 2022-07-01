package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.model.ClientGameBoard;
import it.polimi.ingsw.client.view.CLI.utils.Printer;
import it.polimi.ingsw.server.model.Color;

import java.util.List;


/**
 * <p>Interface implemented by {@link it.polimi.ingsw.client.view.CLI.Cli} and {@link it.polimi.ingsw.client.view.GUI.Gui} classes</p>
 * <p>It is used to standardize methods between CLi and Gui</p>
 */
public abstract class View {
    private ClientController controller;

    /**
     * <p>Allow to set the instance of {@link ClientController} to the View</p>
     * @param controller is the {@link ClientController}
     */
    public void attachController(ClientController controller){
        this.controller = controller;
    }

    /**
     * <p>Ask number of players to configure the match</p>
     * @return number of players
     */
    public abstract int askNumOfPlayers();

    /**
     * <p>Ask the user if wants to play with advanced settings</p>
     * @return a String that could be affirmative or negative
     */
    public abstract String askAdvancedSettings();

    /**
     * <p>Prints to the user a generic message sent from the game</p>
     * @param message
     */
    public abstract void printMessage(String message);

    /**
     * <p></p>Ask the user to insert the name he wants to play with</p>
     * @return the username inserted
     */
    public abstract String askName();


    /**
     * <p>Called when {@link ClientGameBoard} receives a current player change</p>
     * <p>This infrom the {@link ClientController} to start a new turn for the client</p>
     */
    public void startTurn() {
        controller.startTurn();
    }

    /**
     * <p>Ask the user to choose an Assistant to play</p>
     * @return the Assistant rank
     */
    public abstract int askAssistant();

    /**
     * <p>Ask the user the first actions of the action phase</p>
     * <p>Ask to move a student from entrance or to activate a character</p>
     * @param characterActivated if it's true, this method doesn't ask to activate a Character (because it was already activated in the same turn)
     * @return index of chosen action
     */
    public abstract int chooseActionStudent(boolean characterActivated);

    /**
     * <p>Ask the user the Color of the student he wants to operate on</p>
     * @return a {@link Color}
     */
    public abstract Color askStudentColor();

    /**
     * <p>Ask the user to choose a destination where to bring something</p>
     * @return index of the destination
     */
    public abstract int askStudentDestination();

    /**
     * <p>Shows to the user the entire Game Board and information to let take decisions and understand the game</p>
     * @param gameBoard is the {@link ClientGameBoard}
     */
    public abstract void showModel(ClientGameBoard gameBoard);

    /**
     * <p>Ask the user to move Mother Nature or to activate a Character</p>
     * @param characterActivated if it's true, this method doesn't ask to activate a Character (because it was already activated in the same turn)
     * @return index of the choice
     */
    public abstract int chooseActionMotherNature(boolean characterActivated);

    /**
     * <p>Ask the user the number of steps he wants to move Mother Nature</p>
     * @return number of steps
     */
    public abstract int askMotherNatureSteps();

    /**
     * <p>Ask the user to take students from a cloud or to activate a Character</p>
     * @param characterActivated if it's true, this method doesn't ask to activate a Character (because it was already activated in the same turn)
     * @return index of the choice
     */
    public abstract int chooseActionClouds(boolean characterActivated);

    /**
     * <p>Ask the user to choose the cloud he wants to use to take the students from</p>
     * @return index of the cloud
     */
    public abstract int askCloudNumber();

    /**
     * <p>Ask the user to choose the Character he wants to activate</p>
     * @return index of the Character
     */
    public abstract int askCharacterNumber();

    /**
     * <p>Ask the user to choose an Island</p>
     * @return index of the Island
     */
    public abstract int askIslandNumber();

    /**
     * <p>Get the instance of the {@link ClientController}</p>
     * @return instance of {@link ClientController}
     */
    public ClientController getController(){
        return controller;
    }


    /**
     * <p>Shows the lobby containing client's usernames that joined the game</p>
     * @param userNames list of usernames to show
     */
    public abstract void showLobby(List<String> userNames);

    /**
     * <p>Ask the user to answer a question in an affirmative or negative way</p>
     * @param message the question
     * @return true or false
     */
    public abstract boolean askBoolean(String message);

    /**
     * <p>Prints a message that indicate the end of the game</p>
     * @param message
     */
    public abstract void printEndGameMessage(String message);

    /**
     * <p>Prints a generic error message to the user</p>
     * @param message
     */
    public void printErrorMessage(String message) {
    }

    /**
     * <p>Ask the user to choose a student from a Character card</p>
     * @return {@link Color} of the student
     */
    public abstract Color askStudentColorFromCharacter();

    /**
     * <p>Ask the user to choose a Color</p>
     * @return {@link Color}
     */
    public abstract Color askStudentColorFromBox();

    /**
     * <p>Ask the user to choose a student from its Dining Room</p>
     * @return {@link Color} of the student
     */
    public abstract Color askStudentColorFromDiningRoom();

    /**
     * <p>Ask the user to choose a student from its Entrance</p>
     * @return {@link Color} of the student
     */
    public abstract Color askStudentColorFromEntrance();
}
