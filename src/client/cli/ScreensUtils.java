package client.cli;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import client.utils.RequestSender;
import shared.ChatMessage;
import shared.Request;
import shared.Response;


public class ScreensUtils {
    
    public static String validateBranch(Scanner scanner, RequestSender sender) {
        while (true) {
            String userBranch = getNonEmptyInput(scanner, "Enter the specific Branch (Eilat or Jerusalem): ");

            Request request = new Request("VALIDATE_BRANCH", userBranch);
            Response response = sender.sendRequest(request);
            if (response.isSuccessful()) {
                return (String) response.getData();
            } else {
                System.out.println("Error: " + response.getMessage());
                while (true) {
                    String userChoice = getNonEmptyInput(scanner, "Type 'GO BACK' to return to the previous screen or 'RETRY' to try again: ");
                
                    if (userChoice.equalsIgnoreCase("GO BACK")) {
                        return null; 
                    }
                    else if (userChoice.equalsIgnoreCase("RETRY")) {
                        break;
                    } else {
                        System.out.println("Invalid input.");
                    }
                }
            }
        }
    }

    public static String registerNewAccount(Scanner scanner, RequestSender sender, String branch) {
        String userName = getNonEmptyInput(scanner, "Choose a username: ");
        displayPasswordRequirements();
        String password = getNonEmptyInput(scanner, "Choose a password: ");
    
        Request registerRequest = new Request("REGISTER_USER", new Object[]{userName, password, branch});
        Response registerResponse = sender.sendRequest(registerRequest);
        if (registerResponse.isSuccessful()) {
            System.out.println(registerResponse.getMessage() + " " + userName);
            return userName;
        } else {
            System.out.println("Error: " + registerResponse.getMessage());
            return null; 
        }
    }
    
    public static String[] authenticateUser(Scanner scanner, RequestSender sender, boolean isManagerLogin, String branch) {
        int tries = 3;
    
        while (tries > 0) {
            String[] userInfo = getUserInfo(scanner);
    
            Request authRequest = new Request("AUTHENTICATE_USER", new Object[]{userInfo[0], userInfo[1], branch});
            Response authResponse = sender.sendRequest(authRequest);

            if (authResponse.isSuccessful()) {
                System.out.println(authResponse.getMessage());

                Request roleRequest = new Request("GET_EMPLOYEE_ROLE", new Object[]{userInfo[0], branch});
                Response roleResponse = sender.sendRequest(roleRequest);

                if (roleResponse.isSuccessful()) {
                    System.out.println(roleResponse.getMessage());
                    String role = (String) roleResponse.getData();
    
                    if (isManagerLogin) {
                        if (!role.equals("Manager")) {
                            System.out.println("Access denied: This employee is not a manager.");
                            return new String[]{"EXIT", userInfo[0]};
                        }
                    } else {
                        if (role.equals("Manager")) {
                            System.out.println("Access denied: This employee is a manager. Please log in as a regular employee.");
                            return new String[]{"EXIT", userInfo[0]};
                        }
                    }
    
                    System.out.println("Welcome to the system, " + role + " " + userInfo[0] + "!");
                    return new String[]{"OK", userInfo[0]};
                } else {
                    System.out.println("Error: " + roleResponse.getMessage());
                    return new String[]{"EXIT", userInfo[0]};
                }
            } else {
                System.out.println("Error: " + authResponse.getMessage());
                tries--;
                System.out.printf(authResponse.getMessage() + " %d attempt%s remaining.%n", tries, tries > 1 ? "s" : "");
    
                if (tries > 0) {
                    while (true) {
                        System.out.print("Do you want to retry (Y/N)? ");
                        String response = scanner.nextLine().trim().toUpperCase();
                        if (response.equals("N")) {
                            System.out.println("Exiting authentication process.");
                            return new String[]{"EXIT", userInfo[0]};
                        } else if (response.equals("Y")) {
                            break;
                        } else {
                            System.out.println("Invalid input. try again...");
                        }
                    }
                } else {
                    return handleTooManyAttempts(scanner, userInfo[0]);
                }
            }
        }
        return new String[]{"EXIT", null};
    }
    
    public static String[] handleTooManyAttempts(Scanner scanner, String userName) {
        System.out.println("Too many failed attempts. Please select an option:");
    
        while (true) {
            try {
                System.out.println("1. Restart");
                System.out.println("2. Exit");
                System.out.print("Enter your choice (1 or 2): ");

                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1 -> {
                        System.out.println("Restarting...");
                        return new String[]{"RESTART", userName};
                    }
                    case 2 -> {
                        System.out.println("Exiting the system. Goodbye!");
                        return new String[]{"EXIT", userName};
                    }
                    default -> System.out.println("Invalid choice. Please enter 1 for restart or 2 for exit.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number. 1 for restart or 2 for exit.");
                scanner.nextLine();
            }
        }
    }
    
    public static String[] getUserInfo(Scanner scanner) {    
        String userName = getNonEmptyInput(scanner, "Enter username: ");
        String password = getNonEmptyInput(scanner, "Enter password: ");
        return new String[]{userName, password};
    }
    
    public static void displayPasswordRequirements() {
        System.out.println("Your password must meet the following requirements:");
        System.out.println("- At least 8 characters long.");
        System.out.println("- Contains at least one uppercase letter (A-Z).");
        System.out.println("- Contains at least one lowercase letter (a-z).");
        System.out.println("- Contains at least one digit (0-9).");
        System.out.println("- Contains at least one special character (e.g., @, #, $, %, &, etc.).");
        System.out.println("- Must not contain spaces.");
    }

    public static String getNonEmptyInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("Input cannot be empty. Please try again.");
            }
        }
    }


    public static int handleVerdict(String verdict) {
        if (verdict.equals("RESTART")) {
            System.out.println("Restarting...");
            return 1;
        }
        else if (verdict.equals("EXIT")) {
            System.out.println("Thank you for using the Store Management System. Goodbye!");
            return 2;
        }
        else {
            System.out.println("Loading...");
            return 3;
        }
    }

    public static boolean isValidDate(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static String getDate(Scanner scanner) {
        String day = ScreensUtils.getNonEmptyInput(scanner, "Enter a specific date in the format (dd/MM/yyyy): ");
        while (!isValidDate(day)) {     
            System.out.println("Invalid date format.");
            day = ScreensUtils.getNonEmptyInput(scanner, "Enter a specific date in the format (dd/MM/yyyy): ");
        }
        return day;
    }


    public static Map<String, Map<String, Object>> viewInventory(RequestSender sender, String branch) {
        System.out.println("Fetching inventory...");

        Request request = new Request("VIEW_INVENTORY", branch);
        Response response = sender.sendRequest(request);
        if (response.isSuccessful()) {
            System.out.println(response.getMessage());
            Map<String, Map<String, Object>> inventory = (Map<String, Map<String, Object>>) response.getData();
            int i = 1;
            for (Map.Entry<String, Map<String, Object>> entry : inventory.entrySet()) {
                String serialNum = entry.getKey();
                Map<String, Object> product = entry.getValue();
                System.out.println(i + ". " + product.get("name"));
                System.out.println("   Quantity: " + ((Number) product.get("quantity")).intValue());
                System.out.println("   Branch: " + product.get("branch"));
                System.out.println("   Serial Number: " + serialNum);
                System.out.println("--------------------------");
                i++;
            }       
            return inventory;     
        } else {
            System.out.println("Error " + response.getMessage());
            return null;
        }
    }    
    
    protected static String getEmployeeRole(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Choose a Role: ");
                System.out.println("1. Cashier");
                System.out.println("2. Salesperson");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1 -> {return "Cashier";}
                    case 2 -> {return "Salesperson";}
                    default -> System.out.println("Invalid choice. Please enter 1 for Cashier and 2 for Salesperson");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number. 1 for Cashier and 2 for Salesperson");
                scanner.nextLine();
            }
        }
    }

    public static void logOut(RequestSender sender, String userName) {
        Request disconnectRequest = new Request("DISCONNECT", userName);
        Response response = sender.sendRequest(disconnectRequest);
    }

}


