package com.ecommerce.exception;

import java.time.LocalDate;

public class NoOrdersForDateException extends RuntimeException{
    public NoOrdersForDateException(LocalDate date){
        super("No order for date: " +date);
    }
}
