package com.ecommerce.action.customer;

import com.ecommerce.model.entities.Customer;

import java.util.Scanner;

public interface FieldUpdater {
    boolean update(Customer customer, Scanner scanner);
}
