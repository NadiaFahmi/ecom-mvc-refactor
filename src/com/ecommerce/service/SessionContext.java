package com.ecommerce.service;

public class SessionContext {

    private static String currentEmail;

    public static void setLoggedInEmail(String email) {
        currentEmail = email;
    }

    public static String getLoggedInEmail() {
        return currentEmail;
    }
}
