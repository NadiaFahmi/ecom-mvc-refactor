package com.ecommerce.exception;

public class InvalidProductQuantityException extends RuntimeException{
    public InvalidProductQuantityException(String message){
        super(message);
    }

}
