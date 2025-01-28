package client.cli;

import shared.Request;
import shared.Response;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import client.utils.RequestSender;


public class CustomerScreen {

    public static void manageCustomers(RequestSender sender, Scanner scanner, String branch) {
        while (true) {
            try {
                // Display customer management options
                System.out.println("\n──────────────────────");
                System.out.println("CUSTOMER MANAGEMENT");
                System.out.println("────────────────────────");                
                System.out.println("1. Add Customer");
                System.out.println("2. Remove Customer");
                System.out.println("3. View Customers");
                System.out.println("4. Go Back To Main Menu");
                System.out.print("Enter your choice: ");
    
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear the buffer
                switch (choice) {
                    case 1 -> {
                        createNewCustomer(scanner, sender, branch);
                        System.out.println("Customer added successfully!");
                        return; // Exit after adding a customer
                    }
                    case 2 -> {
                        removeCustomer(scanner, sender, branch);
                        System.out.println("Customer removed successfully!");
                        return; // Exit after removing a customer
                    }
                    case 3 -> {
                        viewCustomers(sender, branch);
                        return; // Exit after viewing customers
                    }
                    case 4 -> {
                        System.out.println("Returning to the main menu...");
                        return; // Exit and go back to the main menu
                    }
                    default -> System.out.println("Invalid choice. Please enter a number between 1 and 4.");
                }
            } catch (InputMismatchException e) {
                // Handle invalid input (non-integer values)
                System.out.println("Invalid input. Please enter a valid number between 1 and 4.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
    
    
    public static String createNewCustomer(Scanner scanner, RequestSender sender, String branch) {
        System.out.println("\n────────────────────────");
        System.out.println("ADD NEW CUSTOMER");
        System.out.println("──────────────────────────");
        
        String name = ScreensUtils.getNonEmptyInput(scanner, "Enter Customer Name: ");
        String customerId = ScreensUtils.getNonEmptyInput(scanner, "Enter Customer ID: ");
        String phoneNumber = ScreensUtils.getNonEmptyInput(scanner, "Enter Phone Number: ");
    
        String type = null;
        while (type == null) {
            try {
                // Select customer type
                System.out.println("\nSelect Customer Type:");
                System.out.println("1. New");
                System.out.println("2. Returning");
                System.out.println("3. VIP");
                System.out.print("Choose your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();
    
                switch (choice) {
                    case 1 -> type = "New";
                    case 2 -> type = "Returning";
                    case 3 -> type = "VIP";
                    default -> {
                        System.out.println("Invalid choice. Please enter a number between 1 and 3.");
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number between 1 and 3.");
                scanner.nextLine(); // Clear the invalid input
            }
        }
    
        // Send request to add the new customer
        Request request = new Request("ADD_CUSTOMER", new Object[]{branch, customerId, name, phoneNumber, type});
        Response response = sender.sendRequest(request);
    
        if (response.isSuccessful()) {
            System.out.println(response.getMessage());
            return customerId;
        } else {
            System.out.println("Error: " + response.getMessage());
            return null;
        }
    }
    
    private static void removeCustomer(Scanner scanner, RequestSender sender, String branch) {
        String customerId = ScreensUtils.getNonEmptyInput(scanner, "Enter Customer ID to remove: ");
    
        // Send request to remove the customer
        Request request = new Request("REMOVE_CUSTOMER", new Object[]{branch, customerId});
        Response response = sender.sendRequest(request);
    
        if (response.isSuccessful()) {
            System.out.println(response.getMessage());
        } else {
            System.out.println(response.getMessage());
        }
    }
    
    private static void viewCustomers(RequestSender sender, String branch) {
        // Send request to view the customers
        Request request = new Request("VIEW_CUSTOMERS", branch);
        Response response = sender.sendRequest(request);
    
        if (response.isSuccessful()) {
            System.out.println(response.getMessage());
            Map<String, Map<String, Object>> customers = (Map<String, Map<String, Object>>) response.getData();
    
            customers.forEach((id, customer) -> {
                System.out.println("\n─────────────────────────────────");
                System.out.println("Name: " + customer.get("name"));
                System.out.println("ID: " + id);
                System.out.println("Phone: " + customer.get("phoneNumber"));
                System.out.println("Type: " + customer.get("type"));
                System.out.println("Branch: " + customer.get("branch"));
                System.out.println("───────────────────────────────────");
            });
        } else {
            System.out.println(response.getMessage());
        }
    }
}
