package it.polimi.ingsw.exceptions;

public class NumOfStudentsExceeded extends RuntimeException{
    public NumOfStudentsExceeded(){super("Max number of students reached");}
    public NumOfStudentsExceeded(String msg){super(msg);}
}
