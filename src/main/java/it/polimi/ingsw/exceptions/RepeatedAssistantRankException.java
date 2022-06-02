package it.polimi.ingsw.exceptions;

public class RepeatedAssistantRankException extends RuntimeException{
    public RepeatedAssistantRankException(){super("Another player already played an Assistant card with the same rank");}
}
