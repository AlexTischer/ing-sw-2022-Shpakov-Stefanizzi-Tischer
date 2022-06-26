package it.polimi.ingsw.exceptions;

public class GameSuspendedException extends RuntimeException{
    public GameSuspendedException(String msg){
        super(msg);
    }
}
