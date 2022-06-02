package it.polimi.ingsw.exceptions;

public class EndOfChangesException extends RuntimeException{
    public EndOfChangesException(){super("end of Changes");}
    public EndOfChangesException(String msg){super(msg);}
}
