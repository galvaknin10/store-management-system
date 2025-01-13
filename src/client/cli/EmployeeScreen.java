package client.cli;

import client.RequestSender;
import shared.Request;
import shared.Response;
import java.util.InputMismatchException;
import java.util.Scanner;


public class EmployeeScreen {

    public static void manageEmployees(RequestSender sender, Scanner scanner, String branch) {
        while (true) {
            try {
                System.out.println("\n--- EMPLOYEE MANAGEMENT ---");
                System.out.println("1. Remove Employee");
                System.out.println("2. View Employees");
                System.out.println("3. Go Back To Main Menu");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1 -> {
                        removeEmployee(scanner, sender, branch);
                        return;
                    }
                    case 2 -> {
                        ScreensUtils.viewEmployees(sender, branch);
                        return;
                    }
                    case 3 -> {
                        System.out.println("Returning to the main menu...");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please enter a number between 1 and 2");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 2.");
                scanner.nextLine(); 
            }
        }
    }

    private static void removeEmployee(Scanner scanner, RequestSender sender, String branch) {
        String userName = ScreensUtils.getNonEmptyInput(scanner, "Enter the username of the employee to remove: ");

        Request request = new Request("REMOVE_EMPLOYEE", new Object[]{branch, userName});
        Response response = sender.sendRequest(request);
        if (response.isSuccessful()) {
            System.out.println(response.getMessage());
        } else {
            System.out.println("Error: " + response.getMessage());
        }
    }
}
