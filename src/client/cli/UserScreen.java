package client.cli;

import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import client.RequestSender;
import shared.Request;
import shared.Response;


public class UserScreen {
    
    public static boolean EmployeeScreen(Scanner scanner,  RequestSender sender, String branch) {
        while (true) {
            try {
                System.out.println("\nEmployee Menu (" + branch + "):");
                System.out.println("1. Make Transaction");
                System.out.println("2. Display Branch Employees");
                System.out.println("3. Manage Inventory");
                System.out.println("4. Manage Customers");
                System.out.println("5. Manage Reports");
                System.out.println("6. Go Back");
                System.out.println("7. Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1 -> TransactionScreen.transactionScreen(scanner, sender, branch);

                    case 2 -> ScreensUtils.viewEmployees(sender, branch);

                    case 3 -> InventoryScreen.manageInventory(sender, scanner, branch);

                    case 4 -> CustomerScreen.manageCustomers(sender, scanner, branch);

                    case 5 -> ReportScreen.manageReports(sender, scanner, branch);

                    case 6 -> {
                        System.out.println("Returning to the login screen...");
                        return true;
                    }
                    case 7 -> {
                        System.out.println("Exiting Branch User Menu. Goodbye!");
                        return false;
                    }
                    default -> System.out.println("Invalid choice. Please enter a number between 1 and 7.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number between 1 and 7");
                scanner.nextLine();
            }
        }
    }    
}


