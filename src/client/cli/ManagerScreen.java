package client.cli;

import client.RequestSender;
import shared.Request;
import shared.Response;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class ManagerScreen {

    public static boolean managerScreen(Scanner scanner, RequestSender sender, String branch) {
        while (true) {
            try {
                System.out.println("\n--- MANAGER MENU (" + branch + ") ---");
                System.out.println("1. Add New User (New Employee to the system)");
                System.out.println("2. Manage Employees");
                System.out.println("3. Manage Inventory");
                System.out.println("4. Manage Customers");
                System.out.println("5. Manage Reports");
                System.out.println("6. Manage Chats"); // New option
                System.out.println("7. Display Logs");
                System.out.println("8. Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1 -> addNewUser(scanner, sender);
                    case 2 -> manageEmployees(scanner, sender);
                    case 3 -> manageInventory(scanner, sender);
                    case 4 -> manageCustomers(scanner, sender);
                    case 5 -> manageReports(scanner, sender);
                    case 6 -> manageChats(scanner, sender); // New method for managing chats
                    case 7 -> displayLogs(scanner, sender);
                    case 8 -> {
                        System.out.println("Exiting Manager Menu. Goodbye!");
                        return false; 
                    }
                    default -> System.out.println("Invalid choice. Please enter a number between 1 and 8.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number between 1 and 8.");
                scanner.nextLine(); 
            }
        }
    }

    private static void addNewUser(Scanner scanner, RequestSender sender) {
        System.out.println("Register the new user");
        String branch = ScreensUtils.validateBranch(scanner, sender);
        if (branch == null) {
            return;
        }

        String userName = ScreensUtils.registerNewAccount(scanner, sender, branch);
        if (userName == null) {
            return;
        }
        while (true) {
            try {
                System.out.println("\nInsert the type of this user: ");
                System.out.println("1. Manager user");
                System.out.println("2. Employee user");
                System.out.println("3. Go Back");
                System.out.print("Choose an option: ");
                
                int choice = scanner.nextInt();
                scanner.nextLine();
        
                System.out.println("Please provide employee details: ");

                switch (choice) {
                    case 1 -> {
                        ScreensUtils.createNewEmployee(sender, scanner, true, branch, userName);
                        return;
                    }
                    case 2 -> {
                        ScreensUtils.createNewEmployee(sender, scanner, false, branch, userName);
                        return;
                    }
                    case 3 -> {
                        System.out.println("Returning to the main menu...");
                        return; 
                    }
                    default -> System.out.println("Invalid choice. Please enter a number between 1 and 3.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number between 1 and 3.");
                scanner.nextLine();
            }
        }
    }

    private static void manageEmployees(Scanner scanner, RequestSender sender) {
        String branch = ScreensUtils.validateBranch(scanner, sender);
        if (branch == null) {
            return;
        }
        EmployeeScreen.manageEmployees(sender, scanner, branch);   
    }

    private static void manageInventory(Scanner scanner, RequestSender sender) {
        String branch = ScreensUtils.validateBranch(scanner, sender);
        if (branch == null) {
            return;
        }
        InventoryScreen.manageInventory(sender, scanner, branch);
    }

    private static void manageCustomers(Scanner scanner, RequestSender sender) {
        String branch = ScreensUtils.validateBranch(scanner, sender);
        if (branch == null) {
            System.out.println("Invalid branch.");
        }
        CustomerScreen.manageCustomers(sender, scanner, branch);
    }

    private static void manageReports(Scanner scanner, RequestSender sender) {
        String branch = ScreensUtils.validateBranch(scanner, sender);
        if (branch == null) {
            return;
        }
        ReportScreen.manageReports(sender, scanner, branch);
    }

    private static void displayLogs(Scanner scanner, RequestSender sender) {
        String branch = ScreensUtils.validateBranch(scanner, sender);
        if (branch == null) {
            return;
        }

        Request request = new Request("GET_LOGS", branch);
        Response response = sender.sendRequest(request);
        if (response.isSuccessful()) {
            List<Map<String, Object>> logs = (List<Map<String, Object>>) response.getData();
            System.out.println(response.getMessage());
            System.out.println("--- System Logs ---");
            for (Map<String, Object> logEntry : logs) {
                System.out.println("[" + logEntry.get("timestamp") + "] " + logEntry.get("action") + " - " + logEntry.get("details") + " |" + logEntry.get("branch") + "|");
            }
        } else {
            System.out.println("Error: " + response.getMessage());
        }
    } 
    
    private static void manageChats(Scanner scanner, RequestSender sender) {
        try {
            System.out.println("\n--- Manage Chats ---");
    
            Request listChatsRequest = new Request("LIST_ACTIVE_CHATS", null);
            Response response = sender.sendRequest(listChatsRequest);
    
            if (response.isSuccessful()) {
                List<Map<String, String>> activeChats = (List<Map<String, String>>) response.getData();
                System.out.println("Active Chats:");
                for (int i = 0; i < activeChats.size(); i++) {
                    Map<String, String> chat = activeChats.get(i);
                    System.out.printf("%d. Chat between %s and %s%n", i + 1, chat.get("sender"), chat.get("recipient"));
                }
    
                System.out.print("Enter the number of the chat to join, or 0 to go back: ");
                int choice = scanner.nextInt();
                scanner.nextLine();
    
                if (choice > 0 && choice <= activeChats.size()) {
                    Map<String, String> selectedChat = activeChats.get(choice - 1);
                    Request joinChatRequest = new Request("JOIN_CHAT", selectedChat);
                    Response joinResponse = sender.sendRequest(joinChatRequest);
    
                    if (joinResponse.isSuccessful()) {
                        System.out.println("Joined chat successfully.");
                    } else {
                        System.out.println("Error: " + joinResponse.getMessage());
                    }
                }
            } else {
                System.out.println("Error: " + response.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Error managing chats: " + e.getMessage());
        }
    }
    
}

