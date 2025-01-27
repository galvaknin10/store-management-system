package client.cli;

import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

import client.utils.RequestSender;
import shared.Request;
import shared.Response;


public class UserScreen {
    
    public static boolean userScreen(Scanner scanner,  RequestSender sender, String branch, String userName) {
        while (true) {
            try {
                System.out.println("\n--- MAIN MENU" + "     [" + branch + "-" + "USER" + "-" + userName + "]" + " ---");
                System.out.println("1. Make Transaction");
                System.out.println("2. Display Branch Employees");
                System.out.println("3. Manage Inventory");
                System.out.println("4. Manage Customers");
                System.out.println("5. Manage Reports");
                System.out.println("6. Start Chat");
                System.out.println("7. Messages Section");
                System.out.println("8. Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1 -> TransactionScreen.transactionScreen(scanner, sender, branch);

                    case 2 -> EmployeeScreen.viewEmployees(sender, branch);

                    case 3 -> InventoryScreen.manageInventory(sender, scanner, branch);

                    case 4 -> CustomerScreen.manageCustomers(sender, scanner, branch);

                    case 5 -> ReportScreen.manageReports(sender, scanner, branch);

                    case 6 -> ChatUtils.startChat(scanner, sender, userName); // New chat option

                    case 7 -> {
                        System.out.println("loading...");
                        return true;
                    }
                    case 8 -> {
                        ScreensUtils.logOut(sender, userName);
                        System.out.println("Exiting the system, goodbye :)");
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


