package client.cli;

import shared.Request;
import shared.Response;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

import client.utils.RequestSender;

import java.util.List;


public class InventoryScreen {

    public static void manageInventory(RequestSender sender, Scanner scanner, String branch) {
        while (true) {
            try {
                System.out.println("\n--- INVENTORY MANAGEMENT ---");
                System.out.println("1. Add Product");
                System.out.println("2. Remove Product");
                System.out.println("3. View Inventory");
                System.out.println("4. Go Back To Main Menu");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1 -> {
                        addProduct(scanner, sender, branch);
                        return;
                    }
                    case 2 -> {
                        removeProduct(scanner, sender, branch);
                        return;
                    }
                    case 3 -> {
                        ScreensUtils.viewInventory(sender, branch);
                        return;
                    }
                    case 4 -> {
                        System.out.println("Returning to the main menu...");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please enter a number Between 1 and 4");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number between 1 and 4.");
                scanner.nextLine();
            }    
        }
    }

    private static void addProduct(Scanner scanner, RequestSender sender, String branch) {
        System.out.println("\n--- Add Product ---");
        String serialNum = ScreensUtils.getNonEmptyInput(scanner, "Enter Product Serial Number: "); 
        String name = ScreensUtils.getNonEmptyInput(scanner, "Enter Product Name: ");

        double price = 0;
        while (price == 0) {
            System.out.print("Enter Price: ");
            try {
                price = scanner.nextDouble();
                scanner.nextLine();
                if (price <= 0) {
                    System.out.println("Price must be greater than 0.");
                } else {
                    break;
                }  
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid price.");
                scanner.nextLine();
            }
        }
        
        int quantity = 0;
        while (quantity == 0) {
            System.out.print("Enter Quantity: ");
            try {
                quantity = scanner.nextInt();
                scanner.nextLine();
                if (quantity <= 0) {
                    System.out.println("Quantity must be greater than 0.");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid quantity.");
                scanner.nextLine();
            }
        }

        Request request = new Request("ADD_PRODUCT", new Object[]{branch, serialNum, name, price, quantity});
        Response response = sender.sendRequest(request);
        if (response.isSuccessful()) {
            System.out.println("Product added successfully!");
        } else {
            System.out.println("Error "+ response.getMessage());
        }
    }

    private static void removeProduct(Scanner scanner, RequestSender sender, String branch) {
        System.out.println("\n--- Remove Product ---");
        String serialNum = ScreensUtils.getNonEmptyInput(scanner, "Enter Product Serial Number to remove: ");

        int quantity;
        while (true) {
            try {
                System.out.print("Enter How Many Units: ");
                quantity = scanner.nextInt();

                if (quantity <= 0) {
                    System.out.println("Invalid quantity. try again...");
                    continue;
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine();
            }
        }


        Request request = new Request("REMOVE_PRODUCT", new Object[]{branch, serialNum, quantity});
        Response response = sender.sendRequest(request);
        if (response.isSuccessful()) {
            System.out.println("Product removed successfully!");
        } else {
            System.out.println("Error " + response.getMessage());
        }
    }
}

