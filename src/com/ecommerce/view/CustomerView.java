package com.ecommerce.view;

import com.ecommerce.model.entities.Customer;

import java.util.Collection;

public class CustomerView {
    public void showAllCustomers(Collection<Customer> customers) {
        if (customers == null || customers.isEmpty()) {
            System.out.println("🚫 No customers found.");
            return;
        }

        System.out.println("👥 Total Users: " + customers.size());
        for (Customer customer : customers) {
            showCustomerSummary(customer);
        }
    }

    public void showCustomerSummary(Customer customer) {
        System.out.println("🆔 " + customer.getId() + " | " + customer.getName() + " | 💰 $" + customer.getBalance());
    }

}
