package client.cli;

import shared.Request;
import shared.Response;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import client.utils.RequestSender;


public class InventoryScreen {

    public static void manageInventory(RequestSender sender, Scanner scanner, String branch) {
        while (true) {
            try {
                // Display Inventory management options
                System.out.println("\n────────────────────────");
                System.out.println("INVENTORY MANAGEMENT");
                System.out.println("──────────────────────────");
                System.out.println("1. Add Product");
                System.out.println("2. Remove Product");
                System.out.println("3. View Inventory");
                System.out.println("4. Go Back To Main Menu");
                System.out.print("Enter your choice: ");
    
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear the buffer
                switch (choice) {
                    case 1 -> {
                        addProduct(scanner, sender, branch);
                        System.out.println("Product added successfully!");
                        return; // Exit after adding product
                    }
                    case 2 -> {
                        removeProduct(scanner, sender, branch);
                        System.out.println("Product removed successfully!");
                        return; // Exit after removing product
                    }
                    case 3 -> {
                        InventoryScreen.viewInventory(sender, branch);
                        return; // Exit after viewing inventory
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
    
    private static void addProduct(Scanner scanner, RequestSender sender, String branch) {
        System.out.println("\n───────────────");
        System.out.println("ADD PRODUCT");
        System.out.println("─────────────────");
    
        // Get product serial number and name (ensure inputs are non-empty)
        String serialNum = ScreensUtils.getNonEmptyInput(scanner, "Enter Product Serial Number: ");
        String name = ScreensUtils.getNonEmptyInput(scanner, "Enter Product Name: ");
    
        double price = 0;
        // Ensure valid price input (greater than 0)
        while (price == 0) {
            System.out.print("Enter Price: ");
            try {
                price = scanner.nextDouble();
                scanner.nextLine(); // Clear the buffer
                if (price <= 0) {
                    System.out.println("Price must be greater than 0.");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid price.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    
        int quantity = 0;
        // Ensure valid quantity input (greater than 0)
        while (quantity == 0) {
            System.out.print("Enter Quantity: ");
            try {
                quantity = scanner.nextInt();
                scanner.nextLine(); // Clear the buffer
                if (quantity <= 0) {
                    System.out.println("Quantity must be greater than 0.");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid quantity.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    
        // Send request to add the product
        Request request = new Request("ADD_PRODUCT", new Object[]{branch, serialNum, name, price, quantity});
        Response response = sender.sendRequest(request);
    
        if (response.isSuccessful()) {
            System.out.println("Product added successfully!");
        } else {
            System.out.println("Error: " + response.getMessage());
        }
    }
    
    private static void removeProduct(Scanner scanner, RequestSender sender, String branch) {
        System.out.println("\n──────────────");
        System.out.println("REMOVE PRODUCT");
        System.out.println("─────────────────");
    
        // Get the serial number of the product to remove (ensure input is non-empty)
        String serialNum = ScreensUtils.getNonEmptyInput(scanner, "Enter Product Serial Number to remove: ");
    
        int quantity;
        // Ensure valid quantity input (greater than 0)
        while (true) {
            try {
                System.out.print("Enter How Many Units: ");
                quantity = scanner.nextInt();
                scanner.nextLine(); // Clear the buffer
    
                if (quantity <= 0) {
                    System.out.println("Invalid quantity. Please enter a number greater than 0.");
                    continue;
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    
        // Send request to remove the product
        Request request = new Request("REMOVE_PRODUCT", new Object[]{branch, serialNum, quantity});
        Response response = sender.sendRequest(request);
    
        if (response.isSuccessful()) {
            System.out.println("Product removed successfully!");
        } else {
            System.out.println("Error: " + response.getMessage());
        }
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
                System.out.println("\n──────────────────────────────");
                System.out.println(i + ". " + product.get("name"));
                System.out.println("   Quantity: " + ((Number) product.get("quantity")).intValue());
                System.out.println("   Branch: " + product.get("branch"));
                System.out.println("   Serial Number: " + serialNum);
                System.out.println("─────────────────────────────────");
                i++;
            }       
            return inventory;     
        } else {
            System.out.println("Error: " + response.getMessage());
            return null;
        }
    }
}

