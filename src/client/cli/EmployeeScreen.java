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
                // Display Employee management options
                System.out.println("\n────────────────────────");
                System.out.println("EMPLOYEE MANAGEMENT");
                System.out.println("──────────────────────────");
                
                System.out.println("1. Remove Employee");
                System.out.println("2. View Employees");
                System.out.println("3. Go Back To Main Menu");
                System.out.print("Enter your choice: ");
    
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear the buffer
                switch (choice) {
                    case 1 -> {
                        removeEmployee(scanner, sender, branch);
                        System.out.println("Employee removed successfully!");
                        return; // Exit after removing employee
                    }
                    case 2 -> {
                        viewEmployees(sender, branch);
                        return; // Exit after viewing employees
                    }
                    case 3 -> {
                        System.out.println("Returning to the main menu...");
                        return; // Exit and go back to the main menu
                    }
                    default -> System.out.println("Invalid choice. Please enter a number between 1 and 3.");
                }
            } catch (InputMismatchException e) {
                // Handle invalid input (non-integer values)
                System.out.println("Invalid input. Please enter a valid number between 1 and 3.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
    
    public static void viewEmployees(RequestSender sender, String branch) {
        // Send request to view the employees
        Request request = new Request("VIEW_EMPLOYEES", branch);
        Response response = sender.sendRequest(request);
    
        if (response.isSuccessful()) {
            System.out.println(response.getMessage());
            Map<String, Map<String, Object>> employees = (Map<String, Map<String, Object>>) response.getData();
    
            employees.forEach((userName, employee) -> {
                System.out.println("\n──────────────────────────────");
                System.out.println("Name: " + employee.get("name"));
                System.out.println("ID: " + employee.get("id"));
                System.out.println("Phone: " + employee.get("phoneNumber"));
                System.out.println("Account Number: " + employee.get("accountNumber"));
                System.out.println("Role: " + employee.get("role"));
                System.out.println("User Name: " + userName);
                System.out.println("Branch: " + employee.get("branch"));
                System.out.println("─────────────────────────────────");
            });
        } else {
            System.out.println("Error: " + response.getMessage());
        }
    }
    
    private static void removeEmployee(Scanner scanner, RequestSender sender, String branch) {
        String userName = ScreensUtils.getNonEmptyInput(scanner, "Enter the username of the employee to remove: ");
    
        // Send request to remove the employee
        Request request = new Request("REMOVE_EMPLOYEE", new Object[]{branch, userName});
        Response response = sender.sendRequest(request);
    
        if (response.isSuccessful()) {
            System.out.println(response.getMessage());
        } else {
            System.out.println("Error: " + response.getMessage());
        }
    }    

    public static void createNewEmployee(RequestSender sender, Scanner scanner, boolean isManager, String branch, String userName) {
        // Gather employee information
        String employeeId = ScreensUtils.getNonEmptyInput(scanner, "Enter Employee ID: ");
        String name = ScreensUtils.getNonEmptyInput(scanner, "Enter Employee Name: ");
        String phoneNumber = ScreensUtils.getNonEmptyInput(scanner, "Enter Phone Number: ");
        String accountNumber = ScreensUtils.getNonEmptyInput(scanner, "Enter Account Number: ");
    
        // Assign role based on manager status or prompt for role
        String role;
        if (isManager) {
            role = "Manager";
        } else {
            role = chooseEmployeeRole(scanner);
        }
    
        // Send request to add the employee
        Request request = new Request("ADD_EMPLOYEE", new Object[]{branch, employeeId, name, phoneNumber, accountNumber, role, userName});
        Response response = sender.sendRequest(request);
    
        if (response.isSuccessful()) {
            System.out.println(response.getMessage());
        } else {
            System.out.println("Error: " + response.getMessage());
        }
    }

    private static String chooseEmployeeRole(Scanner scanner) {
        while (true) {
            try {
                System.out.println("\n─────────────────────");
                System.out.println("Choose a Role");
                System.out.println("───────────────────────");
                
                System.out.println("1. Cashier");
                System.out.println("2. Salesperson");
                System.out.print("Enter your choice: ");
    
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear the buffer
                switch (choice) {
                    case 1 -> { return "Cashier"; }
                    case 2 -> { return "Salesperson"; }
                    default -> System.out.println("Invalid choice. Please enter 1 for Cashier and 2 for Salesperson.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number. 1 for Cashier and 2 for Salesperson.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
}
