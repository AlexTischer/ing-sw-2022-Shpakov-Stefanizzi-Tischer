package it.polimi.ingsw.exceptions;

public class NoEnoughTowersException extends RuntimeException{
    public NoEnoughTowersException() {
        super("Not Enough Towers");
    }
}
