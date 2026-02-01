package main.java.view;

import java.util.Scanner;

public class SignUpView {
    private final Scanner scanner;

    public SignUpView(Scanner scanner) {
        this.scanner = scanner;
    }

    public String promptName() {
        System.out.print("📝 Enter your name: ");
        return scanner.nextLine().trim();
    }

    public String promptEmail() {
        System.out.print("📧 Enter your email: ");
        return scanner.nextLine().trim().toLowerCase();
    }

    public String promptPassword() {
        System.out.print("🔒 Enter your password: ");

        return scanner.nextLine().trim();
    }

    public double promptBalance() {
        System.out.print("💰 Enter your balance: ");
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid balance. Defaulting to 0.");
            return 0;
        }
    }

    public String promptAddress() {
        System.out.print("🏠 Enter your address: ");

        return scanner.nextLine().trim();
    }

    public void showSuccess(String name) {

        System.out.println("✅ Registration complete! Welcome, " + name + ".");
        System.out.println("👤 Welcome to Nadia’s Shop!");
    }
    public void showError(String message){
        System.out.println(message);
    }

}
