package it.polimi.ingsw.exceptions;

public class NoEnoughEntryTilesException extends RuntimeException{
    public NoEnoughEntryTilesException(){super("This character does not have enough entry tiles");}
}
