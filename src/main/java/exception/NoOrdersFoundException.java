package main.java.exception;

public class NoOrdersFoundException extends RuntimeException{
    public NoOrdersFoundException(String message) {
        super(message);
    }
}
