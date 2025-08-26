package com.ecommerce.service;

public interface TransactionViewer {
    void viewAllTransactions();
    void viewTransactionsByUser(String email);

}
