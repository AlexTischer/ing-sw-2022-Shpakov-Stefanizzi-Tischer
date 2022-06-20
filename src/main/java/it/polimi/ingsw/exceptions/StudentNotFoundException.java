package it.polimi.ingsw.exceptions;

public class StudentNotFoundException extends RuntimeException{
    public StudentNotFoundException() {
        super("Wasn`t able to find student of that color");
    }
    public StudentNotFoundException(String msg) {
        super(msg);
    }
}
