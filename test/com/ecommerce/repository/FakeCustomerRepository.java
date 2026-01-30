package com.ecommerce.repository;

import com.ecommerce.model.entities.Customer;

import java.util.*;

public class FakeCustomerRepository extends CustomerRepository{


    private Map<Integer, Customer> customers=new HashMap<>();
    int idCounter =1;

    public FakeCustomerRepository() {
        super("test_customers.txt");
    }

    @Override
    public Customer getCustomerById(int id) {
        return customers.get(id);
    }

    @Override
    public void saveCustomer(Customer customer) {
        if(customer.getId() == 0){
            customer.setId(idCounter++);
        }
        customers.put(customer.getId(), customer);
    }
    public Customer createCustomer(String name, String email, String password,
                                   double balance, String address){
        int id = idCounter++;
        Customer customer = new Customer(id,name,email,password,balance,address);
        customers.put(id,customer);
        return customer;
    }
public void load(){
}

    public int getIdCounter(){
        return idCounter;
    }

    public void clear(){
        customers.clear();
    }
    public Customer getCustomerByEmail(String email){
        return customers.values().stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst()
                .orElse(null);

    }
    public boolean isEmailTakenByAnother(String newEmail, int currentCustomerId){
        Customer existingEmail=getCustomerByEmail(newEmail);
        if(existingEmail == null){
            return false;
        }
        return existingEmail.getId() !=currentCustomerId;
    }

}
