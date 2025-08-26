package com.ecommerce.action.customer;

import com.ecommerce.model.entities.Customer;

import java.util.Scanner;

public class AddressUpdater implements FieldUpdater{
    public boolean update(Customer customer, Scanner scanner) {
        System.out.print("📍 Enter your new address: ");
        String address = scanner.nextLine().trim();
        if (address.isBlank()) {
            System.out.println("⚠️ Address can't be empty.");
            return false;
        }
        customer.setAddress(address);
        System.out.println("✅ Address updated.");
        return true;
    }


}
