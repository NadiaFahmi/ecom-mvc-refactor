package main.java.view;

import main.java.model.entities.CartItem;

import java.util.List;
import java.util.Scanner;

public class CartView {

    private Scanner scanner;

    public CartView(Scanner scanner) {
        this.scanner = scanner;
    }

    public int productIdPrompt() {
        int count = 2;
        while (count > 0) {
            System.out.print("Enter Product ID: ");
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter valid number");
                count--;
            }
        }
        return -1;
    }

    public int productIdRemovePrompt() {

        while (true) {
            System.out.print("Enter Product ID to remove or q to cancel: ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("q")) {
                System.out.println("Removal cancelled");
                return -1;
            }
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid Number");
            }

            return -1;
        }
    }

    public int newQuantityPrompt() {
        System.out.print("Enter New Quantity: ");
        return Integer.parseInt(scanner.nextLine().trim());
    }

    public int quantityPrompt() {
        int count = 2;
        while (count > 0) {
            System.out.print("Enter Quantity (or '-1' to cancel): ");
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid number.");
                count--;
            }
        }
        return -1;
    }
    public void showQuantityUpdated() {
        System.out.println("Quantity successfully updated");
    }


    public void display(List<CartItem> items) {
        System.out.println("🛍️ Cart Contents:");
        for (CartItem item : items) {
            System.out.printf("- %s x%d = $%.2f%n",
                    item.getName(),
                    item.getQuantity(),
                    item.getTotalPrice());
        }
    }
    public void showSuccessMessage() {
        System.out.println("Product has been added to your cart");
    }
    public void showErrorMessage(String message)
    {
        System.out.println(message);
    }
    public void showProductError(){
        System.out.println("Invalid product selected. Please try again");
    }
    public void showTotalCartPrice(double total){
        System.out.println("Total cart price = "+"$"+total);
    }
    public void showRemovedMessage(){
        System.out.println("Product removed successfully");
    }
}
