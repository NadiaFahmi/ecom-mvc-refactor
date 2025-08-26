package com.ecommerce.view;
import com.ecommerce.action.admin.*;
import com.ecommerce.service.AdminService;

import java.util.*;

public class AdminDashboard {

    private final Map<String, AdminAction> actions = new LinkedHashMap<>();

    public AdminDashboard() {
        System.out.println("👥 User Management:");
        actions.put("1", new ViewAllUsersAction());
        actions.put("2", new FilterUsersByKeywordAction());
        actions.put("3", new GetUsersByBalanceRangeAction());
        System.out.println("📦 Order Management:");
        actions.put("4", new ViewAllTransactionsAction());
        actions.put("5", new FilterOrdersByDateAction());
        actions.put("6", new ViewTransactionsByUserAction());
        System.out.println("🛍️ Product Management:");
        actions.put("7", new FilterProductsByCategoryAction());
        actions.put("8", new AddProductAction());
        actions.put("9", new UpdateProductAction());
        actions.put("10", new RemoveProductAction());
        actions.put("11", new ListAllProductsAction());
        System.out.println("🚪 Exit");
        System.out.println("0. Exit");

    }


public void launch(Scanner scanner, AdminService adminService) {

    System.out.println("👋 Welcome, " + adminService.getAdmin().getName());

    while (true) {
        System.out.println("\n👑 Admin Dashboard — Choose an action:");

        System.out.println("👥 User Management:");
        System.out.println("1 - View All Users");
        System.out.println("2 - Search Users by Keyword");
        System.out.println("3 - Filter Users by Balance");

        System.out.println("📦 Order Management:");
        System.out.println("4 - View All Transactions");
        System.out.println("5 - Filter Orders by Date");
        System.out.println("6 - View Transactions by Email");

        System.out.println("🛍️ Product Management:");
        System.out.println("7 - Filter Products by Category");
        System.out.println("8 - Add New Product");
        System.out.println("9 - Update Product");
        System.out.println("10 - Remove Product");
        System.out.println("11 - List All Products");

        System.out.println("🚪 Other:");
        System.out.println("0 - Logout and Exit Dashboard");
        System.out.println("exit - Logout and Exit Dashboard");

        System.out.print("\nYour choice: ");
        String choice = scanner.nextLine().trim();

        if (choice.equals("0") || choice.equalsIgnoreCase("exit")) {
            System.out.println("\n👋 Logging out of Admin Dashboard. Have a productive day!");
            break;
        }

        AdminAction action = actions.get(choice);
        if (action != null) {
            action.execute(adminService, scanner);
        } else {
            System.out.println("⚠️ Invalid choice. Please try again.");
        }
    }
}
    private String getActionLabel(AdminAction action) {

        return switch (action.getClass().getSimpleName()) {
            case "ViewAllUsersAction" -> "View All Users";
            case "FilterUsersByKeywordAction" -> "Search Users by Keyword";
            case "GetUsersByBalanceRangeAction" -> "Filter Users by Balance";
            case "ViewAllTransactionsAction" -> "View All Transactions";
            case "FilterOrdersByDateAction" -> "Filter Orders by Date";
            case "ViewTransactionsByUserAction" -> "View Transactions by Email";
            case "FilterProductsByCategoryAction" -> "Filter Products by Category";
            case "AddProductAction" -> "Add New Product";
            case "UpdateProductAction" -> "Update Product";
            case "RemoveProductAction" -> "Remove Product";
            case "ListAllProductsAction" -> "List All Products";

            default -> "❓ Unknown Action";
        };

    }
}