package it.polimi.ingsw.exceptions;

public class NoEnoughCoinsException extends RuntimeException{
    public NoEnoughCoinsException(){super("This player does not have enough coins");}
    public NoEnoughCoinsException(String msg){super(msg);}
}
