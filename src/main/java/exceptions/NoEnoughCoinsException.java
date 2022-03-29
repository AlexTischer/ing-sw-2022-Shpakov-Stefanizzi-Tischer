package exceptions;

public class NoEnoughCoinsException extends Exception{
    public NoEnoughCoinsException(){super("This player does not have enough coins");}
}
