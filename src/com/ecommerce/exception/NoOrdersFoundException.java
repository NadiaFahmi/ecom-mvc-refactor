package com.ecommerce.exception;

public class NoOrdersFoundException extends RuntimeException{
    public NoOrdersFoundException(String message) {
        super(message);
    }
}
