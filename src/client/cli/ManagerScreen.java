package client.cli;

import shared.ChatMessage;
import shared.Request;
import shared.Response;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

import client.utils.RequestSender;


public class ManagerScreen {

    public static boolean managerScreen(Scanner scanner, RequestSender sender, String branch, String userName) {
        while (true) {
            try {
                System.out.println("\n--- MAIN MENU" + "     [" + branch + "-" + "Manager" + "-" + userName + "]" + " ---");
                System.out.println("1. Add New User (New Employee to the system)");
                System.out.println("2. Manage Employees");
                System.out.println("3. Manage Inventory");
                System.out.println("4. Manage Customers");
                System.out.println("5. Manage Reports");
                System.out.println("6. Join live chat"); // New option
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
                    case 6 -> joinLiveChat(scanner, sender, userName); 
                    case 7 -> displayLogs(scanner, sender);
                    case 8 -> {
                        ScreensUtils.logOut(sender, userName);
                        System.out.println("Exiting the system, goodbye :)");
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
        System.out.println("Register the new user: ");
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
                        createNewEmployee(sender, scanner, true, branch, userName);
                        return;
                    }
                    case 2 -> {
                        createNewEmployee(sender, scanner, false, branch, userName);
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

    private static void createNewEmployee(RequestSender sender, Scanner scanner, boolean isManager, String branch, String userName) {
        String employeeId = ScreensUtils.getNonEmptyInput(scanner, "Enter Employee ID: ");
        String name = ScreensUtils.getNonEmptyInput(scanner, "Enter Employee Name: ");
        String phoneNumber = ScreensUtils.getNonEmptyInput(scanner, "Enter Phone Number: ");
        String accountNumber = ScreensUtils.getNonEmptyInput(scanner, "Enter Account Number: ");

        String role;
        if (isManager) {
            role = "Manager";
        } else {
            role = ScreensUtils.getEmployeeRole(scanner);
        }

        Request request = new Request("ADD_EMPLOYEE", new Object[]{branch, employeeId, name, phoneNumber, accountNumber, role, userName});
        Response response = sender.sendRequest(request);
        if (response.isSuccessful()) {
            System.out.println(response.getMessage());
            return;
        } else {
            System.out.println("Error: " + response.getMessage());
        }
    }


    private static void joinLiveChat(Scanner scanner, RequestSender sender, String userName) {
        Request request = new Request("INTERRUPT_LIVE_CHAT", null);
        Response response = sender.sendRequest(request);

        if (response.isSuccessful()) {
            Object[] chatInfo = (Object[]) response.getData();
            String partnerUserName = (String) chatInfo[0];
            List<ChatMessage> previousConversation = (List<ChatMessage>) chatInfo[1];

            for (ChatMessage message : previousConversation) {
                System.out.println(message); 
            }

            ChatUtils.runChat(scanner, sender, userName, partnerUserName);
        }
    }
}

