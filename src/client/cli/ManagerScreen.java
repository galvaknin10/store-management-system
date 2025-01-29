package client.cli;

import shared.ChatMessage;
import shared.Request;
import shared.Response;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import client.utils.RequestSender;


public class ManagerScreen {

    public static boolean managerScreen(Scanner scanner, RequestSender sender, String branch, String userName) {
        while (true) {
            try {
                System.out.println("\n───────────────────────────────────");
                System.out.println("MAIN MENU [Branch: " + branch + " | User: " + userName + "]");
                System.out.println("─────────────────────────────────────");
                System.out.println("1. Add New User (New Employee to the system)");
                System.out.println("2. Manage Employees");
                System.out.println("3. Manage Inventory");
                System.out.println("4. Manage Customers");
                System.out.println("5. Manage Reports");
                System.out.println("6. Join live chat");
                System.out.println("7. Display Logs");
                System.out.println("8. Exit");
                System.out.println("──────────────────────────────────────");
                System.out.print("Please choose an option: ");
                
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear the buffer
                
                switch (choice) {
                    case 1 -> RegisterUtils.addNewUser(scanner, sender);
                    case 2 -> manageEmployees(scanner, sender);
                    case 3 -> manageInventory(scanner, sender);
                    case 4 -> manageCustomers(scanner, sender);
                    case 5 -> manageReports(scanner, sender);
                    case 6 -> joinLiveChat(scanner, sender, userName); 
                    case 7 -> displayLogs(scanner, sender);
                    case 8 -> {
                        ScreensUtils.logOut(sender, userName);
                        System.out.println("\nExiting the system, goodbye " + userName);
                        return false;
                    }
                    default -> System.out.println("Invalid choice. Please enter a number between 1 and 8.");
                }
            } catch (InputMismatchException e) {
                // Handle invalid input (non-integer values)
                System.out.println("Invalid input. Please enter a valid number between 1 and 8.");
                scanner.nextLine(); // Clear invalid input
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
            return;
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
        // Validate branch
        String branch = ScreensUtils.validateBranch(scanner, sender);
        if (branch == null) {
            return;
        }
    
        // Request logs from the server
        Request request = new Request("GET_LOGS", branch);
        Response response = sender.sendRequest(request);
    
        if (response.isSuccessful()) {
            List<Map<String, Object>> logs = (List<Map<String, Object>>) response.getData();
            System.out.println(response.getMessage());
            System.out.println("\n─────────────");
            System.out.println("System Logs");
            System.out.println("───────────────");
    
            // Display the logs
            for (Map<String, Object> logEntry : logs) {
                System.out.println("[" + logEntry.get("timestamp") + "] " + logEntry.get("action") + " - " + logEntry.get("details") + " |" + logEntry.get("branch") + "|");
                System.out.println("────────────────────────────");
            }
        } else {
            System.out.println("Error: " + response.getMessage());
        }
    }
    
    private static void joinLiveChat(Scanner scanner, RequestSender sender, String userName) {
        // Request to interrupt the live chat and retrieve chat details
        Request request = new Request("INTERRUPT_LIVE_CHAT", null);
        Response response = sender.sendRequest(request);
    
        if (response.isSuccessful()) {
            // Extract chat information from the response
            Object[] chatInfo = (Object[]) response.getData();
            String partnerUserName = (String) chatInfo[0];
            List<ChatMessage> previousConversation = (List<ChatMessage>) chatInfo[1];
    
            // Display the previous conversation
            if (!previousConversation.isEmpty()) {
                System.out.println("\n────────────────────────");
                System.out.println("Previous Conversation");
                System.out.println("──────────────────────────");
    
                for (ChatMessage message : previousConversation) {
                    System.out.println(message); // Display each message
                }
    
                System.out.println("───────────────────────────");
            } else {
                System.out.println("No previous conversation found.");
            }
    
            // Start a new chat
            ChatUtils.runChat(scanner, sender, userName, partnerUserName);
        } else {
            System.out.println(response.getMessage());
        }
    }
}

