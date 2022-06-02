package it.polimi.ingsw.exceptions;

public class NoEnoughStudentsException extends RuntimeException{
    public NoEnoughStudentsException() {
        super("Not Enough Students");
    }
}
