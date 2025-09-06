package com.ecommerce.service;
import com.ecommerce.action.customer.*;
import com.ecommerce.model.entities.Customer;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UpdateService {
    private final Map<Integer, FieldUpdater> updaters = new HashMap<>();
    private final CustomerService customerService;

    public UpdateService(CustomerService customerService) {
        this.customerService = customerService;
        updaters.put(1, new NameUpdater());
        updaters.put(2, new EmailUpdater(customerService));
        updaters.put(3, new PasswordUpdater());
        updaters.put(4, new AddressUpdater());
        updaters.put(5, new BalanceUpdater());
    }

    public  void launchUpdateMenu(Customer customer, Scanner scanner) {
        while (true) {
            System.out.println("\n🔧 What would you like to update?");
            System.out.println("1. Name\n2. Email\n3. Password\n4. Address\n5. Add Funds\n6. Exit");
            System.out.print("Choose an option: ");
            String input = scanner.nextLine().trim();

            if (input.equals("6")) {
                System.out.println("👋 Exiting update menu.");
                break;
            }

            try {
                int choice = Integer.parseInt(input);
                FieldUpdater updater = updaters.get(choice);
                if (updater != null) {
                    boolean updated = updater.update(customer, scanner);
                    if (updated) customerService.saveCustomers();
                } else {
                    System.out.println("⚠️ Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Please enter a valid number.");
            }
        }
    }


}
