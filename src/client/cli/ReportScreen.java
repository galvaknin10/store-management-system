package client.cli;

import shared.Request;
import shared.Response;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import client.utils.RequestSender;


public class ReportScreen {

    public static void manageReports(RequestSender sender, Scanner scanner, String branch) {
        while (true) {
            try {
                System.out.println("\n─────────────────────");
                System.out.println("REPORTS MANAGEMENT");
                System.out.println("───────────────────────");
                System.out.println("1. Display Report By Specific Date");
                System.out.println("2. Display All Reports");
                System.out.println("3. Delete Specific Report By Date");
                System.out.println("4. Go Back To Main Menu");
                System.out.print("Enter your choice: ");
            
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear the buffer
                switch (choice) {
                    case 1 -> {
                        displayReportByDate(scanner, sender, branch);
                        return; // Exit after displaying the report by date
                    }
                    case 2 -> {
                        displayAllReports(sender, branch);
                        return; // Exit after displaying all reports
                    }
                    case 3 -> {
                        deleteReportByDate(scanner, sender, branch);
                        return; // Exit after deleting the report
                    }
                    case 4 -> {
                        System.out.println("Returning to the main menu...");
                        return; // Exit and go back to the main menu
                    }
                    default -> System.out.println("Invalid choice. Please enter a number between 1 and 4.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number between 1 and 4.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
    
    private static void displayReportByDate(Scanner scanner, RequestSender sender, String branch) {
        String day = getDate(scanner);
    
        // Send request to get the report for the specified day
        Request request = new Request("GET_REPORT", new Object[]{branch, day});
        Response response = sender.sendRequest(request);
    
        if (response.isSuccessful()) {
            System.out.println(response.getMessage());
            Map<String, Object> report = (Map<String, Object>) response.getData();
            System.out.println("\n───────────────────");
            System.out.println("REPORT FOR " + day);
            System.out.println("─────────────────────");
            viewReport(report); // Display the report content
        } else {
            System.out.println("Error: " + response.getMessage());
        }
    }
    
    private static void displayAllReports(RequestSender sender, String branch) {
        // Request to view all reports
        Request request = new Request("VIEW_REPORTS", branch);
        Response response = sender.sendRequest(request);
        if (response.isSuccessful()) {
            System.out.println(response.getMessage());
            Map<String, Map<String, Object>> reports = (Map<String, Map<String, Object>>) response.getData();
            System.out.println("\n─────────────────");
            System.out.println("ALL REPORTS");
            System.out.println("───────────────────");
    
            // Display each report
            reports.forEach((date, report) -> {
                System.out.println("\n--- Report for " + date + " ---");
                viewReport(report); // Display individual report
            });
        } else {
            System.out.println("Error: " + response.getMessage());
        }
    }
    
    private static void deleteReportByDate(Scanner scanner, RequestSender sender, String branch) {
        // Get the date for the report to be deleted
        String day = getDate(scanner);
    
        // Request to delete the report for the specified date
        Request request = new Request("DELETE_REPORT", new Object[]{branch, day});
        Response response = sender.sendRequest(request);
    
        if (response.isSuccessful()) {
            System.out.println(response.getMessage());
        } else {
            System.out.println("Error: " + response.getMessage());
        }
    }

    private static boolean isValidDate(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
    private static String getDate(Scanner scanner) {
        String day = ScreensUtils.getNonEmptyInput(scanner, "Enter a specific date in the format (dd/MM/yyyy): ");
        while (!isValidDate(day)) {     
            System.out.println("Invalid date format. Please use the format (dd/MM/yyyy).");
            day = ScreensUtils.getNonEmptyInput(scanner, "Enter a specific date in the format (dd/MM/yyyy): ");
        }
        return day;
    }
    
    private static void viewReport(Map<String, Object> report) {
        StringBuilder st = new StringBuilder();
        st.append("──────────────────────────────────────\n")
          .append("Branch: ").append(report.get("branch")).append("\n")
          .append("Date: ").append(report.get("date")).append("\n")
          .append("───────────────────────────────────────\n")
          .append("Devices                  Total Sales\n")
          .append("───────────────────────────────────────\n");
    
        // Use Double instead of Integer
        Map<String, Double> salesData = (Map<String, Double>) report.get("salesData");
        for (Map.Entry<String, Double> entry : salesData.entrySet()) {
            st.append(String.format("%-20s %.0f\n", entry.getKey(), entry.getValue()));
        }
    
        st.append("────────────────────────────────────────\n")
          .append("Total Sales for Branch: ").append(String.format("%.0f", report.get("totalSales"))).append(" Devices\n");
    
        System.out.println(st);
    }    
}    
