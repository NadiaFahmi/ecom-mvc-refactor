package com.ecommerce.exception;

public class InvalidBalanceException extends RuntimeException{

    public InvalidBalanceException(String message) {
        super(message);
    }
}
