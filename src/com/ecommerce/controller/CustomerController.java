package com.ecommerce.controller;
import com.ecommerce.model.entities.Customer;

import com.ecommerce.service.CustomerService;

import java.util.Scanner;
public class CustomerController {

    private  CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
    public void registerCustomer(Customer customer) {

        customerService.registerCustomer(customer);
    }

    public void listAllCustomers() {
        for (Customer c : customerService.listAllCustomers()) {
            System.out.println(c);
        }
    }

    public void getCustomerByEmail(String email) {
        Customer customer = customerService.getCustomerByEmail(email);
        if (customer != null) {
            System.out.println(customer);
        } else {
            System.out.println("❌ No customer found with email: " + email);
        }
    }

    public void findCustomerById(int id) {
            Customer customer = customerService.findCustomerById(id);
            System.out.println(customer);

    }

    public void updateEmail(int customerId, String newEmail) {
        Customer customer = customerService.findCustomerById(customerId);
        customerService.updateCustomerEmail(customer, newEmail);
    }

    public void updateName(int customerId, String newName) {
        Customer customer = customerService.findCustomerById(customerId);
        customerService.updateCustomerName(customer, newName);
    }

    public void updateAddress(int customerId, String newAddress) {
        Customer customer = customerService.findCustomerById(customerId);
        customerService.updateCustomerAddress(customer, newAddress);
    }

    public void updateBalance(int customerId, double amount) {
        Customer customer = customerService.findCustomerById(customerId);
        customerService.updateCustomerBalance(customer, amount);
    }

    public void changePassword(int customerId, String currentPassword, String newPassword, String confirmPassword) {
        Customer customer = customerService.findCustomerById(customerId);
        customerService.updatePassword(customer, currentPassword, newPassword, confirmPassword);
    }

    public void resetPassword(int customerId, String inputEmail, String newPassword, String confirmPassword) {
        Customer customer = customerService.findCustomerById(customerId);
        customerService.resetPassword(customer, inputEmail, newPassword, confirmPassword);
    }

    public void delete(int customerId) {
        customerService.deleteCustomer(customerId);
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
            6. Reset Password
            0. Exit
            """);

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> {
                    System.out.print("Enter new name: ");
                    String name = scanner.nextLine();
                    updateName(customer.getId(), name);
                }
                case "2" -> {
                    System.out.print("Enter new email: ");
                    String email = scanner.nextLine();
                    updateEmail(customer.getId(), email);
                }
                case "3" -> {
                    System.out.print("Enter new address: ");
                    String address = scanner.nextLine();
                    updateAddress(customer.getId(), address);
                }
                case "4" -> {
                    System.out.print("Enter amount to adjust balance: ");
                    double amount = Double.parseDouble(scanner.nextLine());
                    updateBalance(customer.getId(), amount);
                }
                case "5" -> {
                    System.out.print("Enter current password: ");
                    String current = scanner.nextLine();
                    System.out.print("Enter new password: ");
                    String newPass = scanner.nextLine();
                    System.out.print("Confirm new password: ");
                    String confirm = scanner.nextLine();
                    changePassword(customer.getId(), current, newPass, confirm);
                }
                case "6" -> {
                    System.out.print("Enter your email: ");
                    String inputEmail = scanner.nextLine();
                    System.out.print("Enter new password: ");
                    String newPass = scanner.nextLine();
                    System.out.print("Confirm new password: ");
                    String confirm = scanner.nextLine();
                    resetPassword(customer.getId(), inputEmail, newPass, confirm);
                }
                case "0" -> {
                    System.out.println("👋 Exiting update menu.");
                    return;
                }
                default -> System.out.println("⚠️ Invalid choice.");
            }
        }
    }


}
