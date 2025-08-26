package com.ecommerce.action.customer;

import com.ecommerce.model.entities.Customer;
import com.ecommerce.service.CustomerService;

import java.util.Scanner;

public class EmailUpdater implements FieldUpdater{
    private final CustomerService customerService;

    public EmailUpdater(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public boolean update(Customer customer, Scanner scanner) {
        System.out.print("📧 Enter your new email: ");
        String newEmail = scanner.nextLine().trim().toLowerCase();

        if (newEmail.isBlank() || !newEmail.matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$")) {
            System.out.println("⚠️ Invalid email format.");
            return false;
        }

        if (customerService.existsEmail(newEmail)) {
            System.out.println("⚠️ This email is already taken.");
            return false;
        }

        customerService.getCustomerMap().remove(customer.getEmail().toLowerCase());
        customer.setEmail(newEmail);
        customerService.getCustomerMap().put(newEmail, customer);

        System.out.println("✅ Email updated successfully.");
        return true;
    }

}
