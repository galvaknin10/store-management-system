package client.cli;

import shared.Request;
import shared.Response;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

import client.utils.RequestSender;


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
                        viewEmployees(sender, branch);
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

    public static void viewEmployees(RequestSender sender, String branch) {
        Request request = new Request("VIEW_EMPLOYEES", branch);
        Response response = sender.sendRequest(request);

        if (response.isSuccessful()) {
            System.out.println(response.getMessage());
            Map<String, Map<String, Object>> employees = (Map<String, Map<String, Object>>) response.getData();
            employees.forEach((userName, employee) -> {
                System.out.println("\nName: " + employee.get("name"));
                System.out.println("ID: " + employee.get("id"));
                System.out.println("Phone: " + employee.get("phoneNumber"));
                System.out.println("Account Number: " + employee.get("accountNumber"));
                System.out.println("Role: " + employee.get("role"));
                System.out.println("User Name: " + userName);
                System.out.println("Branch: " + employee.get("branch"));
                System.out.println("--------------------------");
            });
        } else {
            System.out.println("Error: " + response.getMessage());
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
