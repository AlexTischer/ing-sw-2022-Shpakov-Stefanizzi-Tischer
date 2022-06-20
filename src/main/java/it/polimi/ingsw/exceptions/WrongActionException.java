package it.polimi.ingsw.exceptions;

/**Extends RuntimeException. Gets raised when an action is semantically correct but cannot be done at this moment*/
public class WrongActionException extends RuntimeException{
    public WrongActionException(){super("This action is not allowed");}
    public WrongActionException(String msg){super(msg);}
}
