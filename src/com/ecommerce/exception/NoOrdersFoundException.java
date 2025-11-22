package com.ecommerce.exception;

import java.time.LocalDate;

public class NoOrdersFoundException extends RuntimeException{
    public NoOrdersFoundException(LocalDate date){
        super("No order for date: " +date);
    }
}
