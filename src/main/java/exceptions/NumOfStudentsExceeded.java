package exceptions;

public class NumOfStudentsExceeded extends RuntimeException{
    public NumOfStudentsExceeded(){super("Max number of students reached exception");}
}
