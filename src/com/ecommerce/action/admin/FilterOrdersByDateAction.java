package com.ecommerce.action.admin;
import com.ecommerce.service.AdminService;

import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class FilterOrdersByDateAction implements AdminAction{


    @Override
    public void execute(AdminService adminService, Scanner scanner)


        {

            String adminEmail = adminService.getAdmin().getEmail();
            System.out.println("🔐 Action triggered by admin: " + adminEmail);

            System.out.print("📅 Enter start date (YYYY-MM-DD): ");
            String fromInput = scanner.nextLine().trim();

            System.out.print("📅 Enter end date (YYYY-MM-DD): ");
            String toInput = scanner.nextLine().trim();

            try {
                LocalDate fromDate = LocalDate.parse(fromInput);
                LocalDate toDate = LocalDate.parse(toInput);

                if (fromDate.isAfter(toDate)) {
                    System.out.println("⚠️ Start date cannot be after end date.");
                    return;
                }

                adminService.filterOrdersByDateRange(fromDate, toDate);

            } catch (DateTimeParseException e) {
                System.out.println("❌ Invalid date format. Please enter both dates in the format YYYY-MM-DD.");
            }
        }

    }

