package client.cli;

import client.RequestSender;
import shared.Request;
import shared.Response;
import java.util.InputMismatchException;

import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Scanner;

public class ReportScreen {

    public static void manageReports(RequestSender sender, Scanner scanner, String branch) {
        while (true) {
            try {
                System.out.println("\n ---REPORTS MANAGEMENT ---");
                System.out.println("1. Display Report By Specific Date");
                System.out.println("2. Display All Reports");
                System.out.println("3. Delete Specific Report By Date");
                System.out.println("4. Go Back To Main Menu");
                System.out.print("Enter your choice: ");
        
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1 -> {
                        displayReportByDate(scanner, sender, branch);
                        return;
                    }
                    case 2 -> {
                        displayAllReports(sender, branch);
                        return;
                    }
                    case 3 -> {deleteReportByDate(scanner, sender, branch);
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please enter a number between 1 and 4.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number between 1 and 4.");
                scanner.nextLine(); 
            }
        }
    }

    private static void displayReportByDate(Scanner scanner, RequestSender sender, String branch) {
        String day = ScreensUtils.getDate(scanner);

        Request request = new Request("GET_REPORT", new Object[]{branch, day});
        Response response = sender.sendRequest(request);

        if (response.isSuccessful()) {
            System.out.println(response.getMessage());
            Map<String, Object> report = (Map<String, Object>) response.getData();
            System.out.println("\n--- REPORT ---\n");
            displayReport(report);
        } else {
            System.out.println("Error: " + response.getMessage());
        }
    }

    private static void displayAllReports(RequestSender sender, String branch) {
        Request request = new Request("VIEW_REPORTS", branch);
        Response response = sender.sendRequest(request);
        if (response.isSuccessful()) {
            System.out.println(response.getMessage());
            Map<String, Map<String, Object>> reports = (Map<String, Map<String, Object>>) response.getData();
            System.out.println("\n--- ALL REPORTS ---\n");
            reports.forEach((date, report) -> {
                displayReport(report);
            });
        } else {
            System.out.println("Error: " + response.getMessage());
        }
    }

    private static void deleteReportByDate(Scanner scanner, RequestSender sender, String branch) {
        String day = ScreensUtils.getDate(scanner);

        Request request = new Request("DELETE_REPORT", new Object[]{branch, day});
        Response response = sender.sendRequest(request);
        if (response.isSuccessful()) {
            System.out.println(response.getMessage());
        } else {
            System.out.println("Error: " + response.getMessage());
        }
    }

    public static void displayReport(Map<String, Object> report) {
        StringBuilder st = new StringBuilder();
        st.append("Branch: ").append(report.get("branch")).append("\n")
          .append("Date: ").append(report.get("date")).append("\n")
          .append("-----------------------------------\n")
          .append("Phones                  Total Sales\n")
          .append("-----------------------------------\n");
    
        // Use Double instead of Integer
        Map<String, Double> salesData = (Map<String, Double>) report.get("salesData");
        for (Map.Entry<String, Double> entry : salesData.entrySet()) {
            st.append(String.format("%-20s %10.2f\n", entry.getKey(), entry.getValue()));
        }
    
        st.append("-----------------------------------\n")
          .append("Total Sales for Branch: ").append(report.get("totalSales")).append(" Devices").append("\n");
    
        System.out.println(st);
    }
}    
