package client.cli;

import java.util.InputMismatchException;
import java.util.Scanner;
import client.utils.RequestSender;

public class LoginScreen {

    public static String[] userLogin(Scanner scanner, RequestSender sender) {
        System.out.println("\n─────────────────────────────────────────────");
        System.out.println("Welcome to the Clothing Shop Management System");
        System.out.println("──────────────────────────────────────────────");
    
        while (true) {
            try {
                // Prompt for the user's role selection
                System.out.println("\nPlease select your role:");
                System.out.println("1. Log in as Manager");
                System.out.println("2. Log in as Employee");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
    
                int userChoice = scanner.nextInt();
                scanner.nextLine(); // Clear the buffer
    
                switch (userChoice) {
                    // Manager login flow
                    case 1 -> {
                        System.out.println("\n|Manager Login|");
                        String branch = ScreensUtils.validateBranch(scanner, sender);
                        if (branch == null) {
                            continue; // If branch validation fails, restart the loop
                        }
    
                        String[] authFeedback = RegisterUtils.authenticateUser(scanner, sender, true, branch);
                        String status = authFeedback[0];
                        String userName = authFeedback[1];
                        int result = RegisterUtils.handleAuthFeedBack(status);
                        if (result == 1) {
                            continue; // If authentication fails, restart the loop
                        } else if (result == 2) {
                            // If the user chooses to log out
                            ScreensUtils.logOut(sender, null);
                            System.out.println("Exiting the system, goodbye :)");
                            return null;
                        } else {
                            // Return the user data
                            return new String[]{"Manager", branch, userName};
                        }
                    }
                    // Employee login flow
                    case 2 -> {
                        System.out.println("\n|Employee Login|");
                        String branch = ScreensUtils.validateBranch(scanner, sender);
                        if (branch == null) {
                            continue; // If branch validation fails, restart the loop
                        }
    
                        String[] authFeedback = RegisterUtils.authenticateUser(scanner, sender, false, branch);
                        String status = authFeedback[0];
                        String userName = authFeedback[1];
                        int result = RegisterUtils.handleAuthFeedBack(status);
                        if (result == 1) {
                            continue; // If authentication fails, restart the loop
                        } else if (result == 2) {
                            // If the user chooses to log out
                            ScreensUtils.logOut(sender, null);
                            System.out.println("Exiting the system, goodbye :)");
                            return null;
                        } else {
                            // Return the user data (role, branch, and username)
                            return new String[]{"Employee", branch, userName};
                        }
                    }
                    // Exit the system
                    case 3 -> {
                        ScreensUtils.logOut(sender, null);
                        System.out.println("Exiting the system, goodbye :)");
                        return null;
                    }
                    default -> System.out.println("Invalid choice. Please enter a number between 1 and 3.");
                }
            } catch (InputMismatchException e) {
                // Handle invalid input (non-integer values)
                System.out.println("Invalid input. Please enter a valid number between 1 and 3");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }    
}

