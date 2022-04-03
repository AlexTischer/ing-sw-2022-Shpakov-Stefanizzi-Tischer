package exceptions;

public class NoEnoughStudentsException extends Exception{
    public NoEnoughStudentsException() {
        super("Not Enough Students");
    }
}
