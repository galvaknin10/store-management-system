package client.cli;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import client.utils.RequestSender;


public class UserScreen {
    
    public static boolean userScreen(Scanner scanner, RequestSender sender, String branch, String userName, BlockingQueue<String> inputQueue) {
        while (true) {
            try {
                System.out.println("\n───────────────────────────────────────");
                System.out.println("MAIN MENU [Branch: " + branch + " | User: " + userName + "]");
                System.out.println("─────────────────────────────────────────");
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
                scanner.nextLine(); // Clear the buffer
                switch (choice) {
                    case 1 -> TransactionScreen.transactionScreen(scanner, sender, branch);
                    case 2 -> EmployeeScreen.viewEmployees(sender, branch);
                    case 3 -> InventoryScreen.manageInventory(sender, scanner, branch);
                    case 4 -> CustomerScreen.manageCustomers(sender, scanner, branch);
                    case 5 -> ReportScreen.manageReports(sender, scanner, branch);
                    case 6 -> ChatUtils.startChat(scanner, sender, userName); // New chat option
                    case 7 -> {
                        System.out.println("loading...");
                        if (inputQueue.isEmpty()) {
                            System.out.println("There are no new messages for you...");
                        } else {
                            return true;
                        }
                    }
                    case 8 -> {
                        ScreensUtils.logOut(sender, userName);
                        System.out.println("Exiting the system, goodbye " + userName + "!");
                        return false;
                    }
                    default -> System.out.println("Invalid choice. Please enter a number between 1 and 8.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number between 1 and 8.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }  
}


