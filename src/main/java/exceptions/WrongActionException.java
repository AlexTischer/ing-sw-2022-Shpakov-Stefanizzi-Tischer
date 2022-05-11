package exceptions;

public class WrongActionException extends RuntimeException{
    public WrongActionException(){super("This action is not allowed");}
    public WrongActionException(String msg){super(msg);}
}
