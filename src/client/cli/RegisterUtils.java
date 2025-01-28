package client.cli;

import java.util.InputMismatchException;
import java.util.Scanner;

import client.utils.RequestSender;
import shared.Request;
import shared.Response;

public class RegisterUtils {

    public static void addNewUser(Scanner scanner, RequestSender sender) {
        System.out.println("\n────────────────────────────");
        System.out.println("Register the new user:");
        System.out.println("──────────────────────────────");
    
        // Validate branch
        String branch = ScreensUtils.validateBranch(scanner, sender);
        if (branch == null) {
            return;
        }
    
        // Register the new account
        String userName = registerNewAccount(scanner, sender, branch);
        if (userName == null) {
            return;
        }
    
        while (true) {
            try {
                // User type selection
                System.out.println("\nPlease select the type of this user: ");
                System.out.println("1. Manager user");
                System.out.println("2. Employee user");
                System.out.println("3. Go Back");
                System.out.print("Choose an option: ");
                
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear the buffer
    
                System.out.println("Please provide employee details: ");
                
                switch (choice) {
                    // Manager user creation
                    case 1 -> {
                        EmployeeScreen.createNewEmployee(sender, scanner, true, branch, userName);
                        System.out.println("Manager user created successfully!");
                        return;
                    }
                    // Employee user creation
                    case 2 -> {
                        EmployeeScreen.createNewEmployee(sender, scanner, false, branch, userName);
                        System.out.println("Employee user created successfully!");
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
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    private static String registerNewAccount(Scanner scanner, RequestSender sender, String branch) {
        String userName = ScreensUtils.getNonEmptyInput(scanner, "Choose a username: ");
        displayPasswordRequirements();
        String password = ScreensUtils.getNonEmptyInput(scanner, "Choose a password: ");
    
        // Request to register the new user
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

    private static void displayPasswordRequirements() {
        System.out.println("Your password must meet the following requirements:");
        System.out.println("- At least 8 characters long.");
        System.out.println("- Contains at least one uppercase letter (A-Z).");
        System.out.println("- Contains at least one lowercase letter (a-z).");
        System.out.println("- Contains at least one digit (0-9).");
        System.out.println("- Contains at least one special character (e.g., @, #, $, %, &, etc.).");
        System.out.println("- Must not contain spaces.");
    }

    public static String[] authenticateUser(Scanner scanner, RequestSender sender, boolean isManagerLogin, String branch) {
        int tries = 3;
    
        while (tries > 0) {
            String userName = ScreensUtils.getNonEmptyInput(scanner, "Enter username: ");
            String password = ScreensUtils.getNonEmptyInput(scanner, "Enter password: ");
    
            // Request to authenticate the user
            Request authRequest = new Request("AUTHENTICATE_USER", new Object[]{userName, password, branch});
            Response authResponse = sender.sendRequest(authRequest);
    
            if (authResponse.isSuccessful()) {
                System.out.println(authResponse.getMessage());
    
                // Request to get the user role
                Request roleRequest = new Request("GET_EMPLOYEE_ROLE", new Object[]{userName, branch});
                Response roleResponse = sender.sendRequest(roleRequest);
    
                if (roleResponse.isSuccessful()) {
                    System.out.println(roleResponse.getMessage());
                    String role = (String) roleResponse.getData();
    
                    // Role validation based on manager login status
                    if (isManagerLogin) {
                        if (!role.equals("Manager")) {
                            System.out.println("Access denied: This employee is not a manager.");
                            return new String[]{"EXIT", userName};
                        }
                    } else {
                        if (role.equals("Manager")) {
                            System.out.println("Access denied: This employee is a manager. Please log in as a regular employee.");
                            return new String[]{"EXIT", userName};
                        }
                    }
    
                    System.out.println("Welcome to the system, " + role + " " + userName + "!");
                    return new String[]{"OK", userName};
                } else {
                    System.out.println("Error: " + roleResponse.getMessage());
                    return new String[]{"EXIT", userName};
                }
            } else {
                System.out.println("Error: " + authResponse.getMessage());
                tries--;
                System.out.printf("%s %d attempt%s remaining.%n", authResponse.getMessage(), tries, tries > 1 ? "s" : "");
    
                if (tries > 0) {
                    while (true) {
                        System.out.print("Do you want to retry (Y/N)? ");
                        String response = scanner.nextLine().trim().toUpperCase();
                        if (response.equals("N")) {
                            System.out.println("Exiting authentication process.");
                            return new String[]{"EXIT", userName};
                        } else if (response.equals("Y")) {
                            break;
                        } else {
                            System.out.println("Invalid input. Please try again...");
                        }
                    }
                } else {
                    return handleTooManyAttempts(scanner, userName);
                }
            }
        }
        return new String[]{"EXIT", null};
    }
        
    private static String[] handleTooManyAttempts(Scanner scanner, String userName) {
        System.out.println("Too many failed attempts. Please select an option:");
    
        while (true) {
            try {
                System.out.println("1. Restart");
                System.out.println("2. Exit");
                System.out.print("Enter your choice (1 or 2): ");
    
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear the buffer
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
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }
    
    public static int handleAuthFeedBack(String verdict) {
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
}
