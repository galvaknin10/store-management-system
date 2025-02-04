package client.cli;

import shared.Request;
import shared.Response;
import java.util.*;
import client.utils.RequestSender;

public class TransactionScreen {

    public static void transactionScreen(Scanner scanner, RequestSender sender, String branch) {
        Map<String, Integer> cart = new HashMap<>();
    
        while (true) {
            try {
                System.out.println("\n───────────────────");
                System.out.println("TRANSACTION MENU");
                System.out.println("──────────────────────");
                System.out.println("1. Select Product");
                System.out.println("2. View Cart");
                System.out.println("3. Checkout");
                System.out.println("4. Go Back To Main Menu");
                System.out.print("Enter your choice: ");
    
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear the buffer
                switch (choice) {
                    case 1 -> selectProduct(scanner, sender, branch, cart);
                    case 2 -> viewCart(cart, sender, branch);
                    case 3 -> checkout(scanner, sender, branch, cart);
                    case 4 -> {
                        System.out.println("Returning to the main menu...");
                        return; // Exit the transaction screen and go back to the main menu
                    }
                    default -> System.out.println("Invalid option. Please choose a number between 1 and 4.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number between 1 and 4.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
    
    private static void selectProduct(Scanner scanner, RequestSender sender, String branch, Map<String, Integer> cart) {
        Map<String, Map<String, Object>> inventory = InventoryScreen.viewInventory(sender, branch);
        System.out.println((inventory.size() + 1) + ". " + "Go Back To The Transaction Menu.\n");
        System.out.println("\n─────────────────");
        System.out.println("SELECT PRODUCT");
        System.out.println("───────────────────");
        int choice = 0;
        while (choice <= 0 || choice > inventory.size()) {
            try {
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice == inventory.size() + 1) {
                    return;
                } else if (choice < 0 || choice > inventory.size()) {
                    System.out.println("Invalid product selection. Please enter a number between 1 and " + inventory.size());
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number between 1 and " + inventory.size()); 
                scanner.nextLine(); // Clear invalid input
            }
        }
    
        List<String> keys = new ArrayList<>(inventory.keySet());
        String selectedKey = keys.get(choice - 1);
        Map<String, Object> product = inventory.get(selectedKey);
    
        while (true) {
            try {
                System.out.print("Enter quantity: ");
                int quantity = scanner.nextInt();
                scanner.nextLine();
                if (quantity > 0 && quantity <= ((Number) product.get("quantity")).intValue()) {
                    String name = (String) product.get("name");
                    double itemTotal = (double) product.get("price") * quantity;
                    
                    cart.put(selectedKey, cart.getOrDefault(selectedKey, 0) + quantity);
                    System.out.println(quantity + " piece(s) of " + name + " has been added to the cart.");
                    System.out.println("\n--- TOTAL -> " + itemTotal + "$");
                    break;
                } else {
                    System.out.println("Insufficient stock or invalid quantity. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number."); 
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
    
    private static void viewCart(Map<String, Integer> cart, RequestSender sender, String branch) {
        if (cart.isEmpty()) {
            System.out.println("\n--- Cart is empty ---");
            System.out.println("Loading transaction menu...");
            return;
        }
    
        double productTotalSum = 0;
        System.out.println("\n──────────");
        System.out.println("CART");
        System.out.println("─────────────");
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            String serialNum = entry.getKey();
            int quantity = entry.getValue();
            System.out.println("\nSerial Number: " + serialNum);
    
            // Request to get product details
            Request request = new Request("GET_PRODUCT_DETAILS", new Object[]{branch, serialNum});
            Response response = sender.sendRequest(request);
    
            if (response.isSuccessful()) {
                System.out.println(response.getMessage());
                Map<String, Object> product = (Map<String, Object>) response.getData();
                double price = (double) product.get("price");
                productTotalSum += price * quantity;
    
                System.out.println("Product: " + product.get("name"));
                System.out.println("Price: " + price + "$");
                System.out.println("Quantity: " + quantity);
                System.out.println("─────────────────────────────────");
                System.out.println("\n--- TOTAL FOR THIS PRODUCT -> " + String.format("%.2f", productTotalSum) + "$");
            } else {
                System.out.println(response.getMessage());
                return;
            }
        }
    
        double cartTotal = calculateTotal(cart, sender, branch);
        System.out.println("\n─────────────────────────────────────");
        System.out.println("--- TOTAL FOR THIS CART -> " + String.format("%.2f", cartTotal) + "$");
        System.out.println("────────────────────────────────────────");
    }
    

    private static void checkout(Scanner scanner, RequestSender sender, String branch, Map<String, Integer> cart) {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty. Add products before checking out.");
            System.out.println("Loading transaction menu...");
            return;
        }
    
        System.out.println("\nProceeding to checkout...\n");
        String customerId = ScreensUtils.getNonEmptyInput(scanner, "Enter customer ID: ");
        
        // Request to find customer by ID
        Request customerRequest = new Request("FIND_CUSTOMER", new Object[]{branch, customerId});
        Response customerResponse = sender.sendRequest(customerRequest);
        
        if (!customerResponse.isSuccessful()) {
            System.out.println("Error: " + customerResponse.getMessage());
            System.out.println("Registering new customer.");
            customerId = CustomerScreen.createNewCustomer(scanner, sender, branch);
            if (customerId == null) {
                return;
            }
        }
    
        // Calculate the total for the cart
        double cartTotal = calculateTotal(cart, sender, branch);
    
        // Request to calculate discount based on customer and total
        Request discountRequest = new Request("CALCULATE_DISCOUNT", new Object[]{branch, customerId, cartTotal});
        Response discountResponse = sender.sendRequest(discountRequest);
    
        if (discountResponse.isSuccessful()) {
            Object[] discountInfo = (Object[]) discountResponse.getData();
    
            System.out.println(discountResponse.getMessage());
            System.out.println("Customer Type: " + discountInfo[0]);
            System.out.println("Discount Applied: " + (double) discountInfo[1] * 100 + "%");
            System.out.println("Total After Discount: " + String.format("%.2f", discountInfo[2]) + "$");
    
            // Confirm with the user if they want to proceed with the transaction
            while (true) {
                try {
                    System.out.println("\nDo you want to proceed with the transaction?");
                    System.out.println("1. Yes");
                    System.out.println("2. No");
                    System.out.print("Choose an option: ");
            
                    int choice = scanner.nextInt();
                    scanner.nextLine();
                    switch (choice) {
                        case 1 -> {
                            // Request to process the checkout
                            Request checkOutRequest = new Request("CHECKOUT", new Object[]{branch, cart});
                            Response checkOutResponse = sender.sendRequest(checkOutRequest);
    
                            if (checkOutResponse.isSuccessful()) {
                                System.out.println(checkOutResponse.getMessage());
                                return; // Return after successful checkout
                            } else {
                                System.out.println("Error: " + checkOutResponse.getMessage());
                            }
                        }
                        case 2 -> {
                            System.out.println("Transaction canceled.");
                            System.out.println("Returning to the transaction screen...");
                            return; // Return to transaction screen
                        }
                        default -> System.out.println("Invalid choice. Please enter 1 for checkout or 2 for exit.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid number. 1 for checkout or 2 for exit.");
                    scanner.nextLine(); // Clear invalid input
                }
            }
        } else {
            System.out.println("Error: " + discountResponse.getMessage());
        }
    }
    

    private static double calculateTotal(Map<String, Integer> cart, RequestSender sender, String branch) {
        double totalSum = 0;

        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            String serialNum = entry.getKey();
            int quantity = entry.getValue();

            Request request = new Request("GET_PRODUCT_DETAILS", new Object[]{branch, serialNum});
            Response response = sender.sendRequest(request);

            if (response.isSuccessful()) {
                Map<String, Object> product = (Map<String, Object>) response.getData();
                double price = (double) product.get("price");
                totalSum += price * quantity;
            } else {
                System.out.println("Error: " + response.getMessage());
            }
        }
        return totalSum;
    }
}

