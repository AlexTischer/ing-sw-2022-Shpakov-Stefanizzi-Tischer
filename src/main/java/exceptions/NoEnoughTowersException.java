package exceptions;

public class NoEnoughTowersException extends RuntimeException{
    public NoEnoughTowersException() {
        super("Not Enough Towers");
    }
}
