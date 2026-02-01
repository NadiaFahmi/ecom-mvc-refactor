package main.java.view;

import main.java.controller.CustomerController;
import main.java.model.entities.Customer;

import java.util.Scanner;

public class CustomerUpdateView {

    private CustomerController customerController;

    public CustomerUpdateView(CustomerController customerController) {
        this.customerController = customerController;
    }

    public void launchUpdateMenu(Customer customer, Scanner scanner) {
        if (customer == null) {
            System.out.println("❌ No customer selected.");
            return;
        }

        while (true) {
            System.out.println("""
            \n🔧 Update Menu:
            1. Update Name
            2. Update Email
            3. Update Address
            4. Update Balance
            5. Change Password
            0. Exit
            """);

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
//                case "1" -> customerController.updateName(customer.getId());
                case "1" -> customerController.updateName(customer);

                case "2" -> customerController.updateCustomerEmail(customer);

//                case "3" -> customerController.updateAddress(customer.getId());
                case "3" -> customerController.updateAddress(customer);

//                case "4" -> customerController.updateBalance(customer.getId());
                case "4" -> customerController.updateBalance(customer);

//                case "5" -> customerController.updatePassword(customer.getId());
                case "5" -> customerController.updatePassword(customer);

                case "0" -> {
                    System.out.println("👋 Exiting update menu.");
                    return;
                }
                default -> System.out.println("⚠️ Invalid choice.");
            }
        }
    }
}
