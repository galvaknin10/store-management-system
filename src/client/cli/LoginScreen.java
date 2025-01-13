package client.cli;

import client.RequestSender;
import java.util.InputMismatchException;
import java.util.Scanner;


public class LoginScreen {

    public static String[] userLogin(Scanner scanner, RequestSender sender) {
        System.out.println("\n--- Welcome to the Store Management System ---");
        while (true) {
            try {
                System.out.println("Please select your role:");
                System.out.println("1. Log in as Manager");
                System.out.println("2. Log in as Employee");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");

                int userChoice = scanner.nextInt();
                scanner.nextLine(); 
                switch (userChoice) {
                    case 1 -> {
                        System.out.println("\n|Manager Login|");
                        String branch = ScreensUtils.validateBranch(scanner, sender);
                        if (branch == null) {
                            continue;
                        }

                        String[] verdict = ScreensUtils.authenticateUser(scanner, sender, true, branch);
                        int result = ScreensUtils.handleVerdict(verdict[0]);
                        if (result == 1) {
                            continue;
                        } else if (result == 2) {
                            return null;
                        } else {
                            return new String[]{"Manager", branch, verdict[1]};
                        }
                    }
                    case 2 -> {
                        System.out.println("\n|Employee Login|");
                        String branch = ScreensUtils.validateBranch(scanner, sender);
                        if (branch == null) {
                            continue;
                        }

                        String[] verdict = ScreensUtils.authenticateUser(scanner, sender, false, branch);
                        int result = ScreensUtils.handleVerdict(verdict[0]);
                        if (result == 1) {
                            continue;
                        } else if (result == 2) {
                            return null;
                        } else {
                            return new String[]{"Employee", branch, verdict[1]};
                        }
                    }
                    case 3 -> {
                        System.out.println("Exiting the system. Goodbye!");
                        return null;
                    }
                    default -> System.out.println("Invalid choice. Please enter a number between 1 and 3.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number between 1 and 3");
                scanner.nextLine();
            }
        }
    }
}

