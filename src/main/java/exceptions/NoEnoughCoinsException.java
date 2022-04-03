package exceptions;

public class NoEnoughCoinsException extends RuntimeException{
    public NoEnoughCoinsException(){super("This player does not have enough coins");}
}
