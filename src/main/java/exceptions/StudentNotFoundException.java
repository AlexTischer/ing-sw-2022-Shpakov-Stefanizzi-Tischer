package exceptions;

public class StudentNotFoundException extends RuntimeException{
    public StudentNotFoundException() {
        super("Wasn`t able to find student of that color");
    }
}
