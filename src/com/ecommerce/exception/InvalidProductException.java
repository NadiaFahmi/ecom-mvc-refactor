package com.ecommerce.exception;

public class InvalidProductException extends RuntimeException{
    public InvalidProductException(String message){
        super(message);
    }

}
