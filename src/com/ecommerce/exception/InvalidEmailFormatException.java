package com.ecommerce.exception;

public class InvalidEmailFormatException extends RuntimeException{

    public InvalidEmailFormatException(String message){
        super(message);
    }
}
