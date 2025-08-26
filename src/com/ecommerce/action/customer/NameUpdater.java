package com.ecommerce.action.customer;

import com.ecommerce.model.entities.Customer;

import java.util.Scanner;

public class NameUpdater implements FieldUpdater{

    public boolean update(Customer customer, Scanner scanner) {
        System.out.print("📝 Enter your new name: ");
        String newName = scanner.nextLine().trim();
        if (newName.isBlank()) {
            System.out.println("⚠️ Name can't be empty.");
            return false;
        }
        customer.setName(newName);
        System.out.println("✅ Name updated.");
        return true;
    }

}
