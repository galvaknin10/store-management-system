package view;

import model.credentials.*;
import model.customer.*;
import model.employee.*;
import model.inventory.*;
import model.report.*;
import model.log.*;

import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;


public class HandleUserInteractions {

    static LogManager logManager = LogManager.getInstance();

    public static String[] userLogin(Scanner scanner) {
        CredentialsManager credentialsManager = null;
        EmployeeManager employeeManager = null;
        System.out.println("Welcome to the Store Management System!");
    
        while (true) {
            try {
                System.out.println("\nPlease select your role:");
                System.out.println("1. Log in as Manager");
                System.out.println("2. Log in as Employee");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
    
                int userChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                
                switch (userChoice) {
                    case 1 -> {
                        System.out.println("\nManager Login:");
                        String branch = validateBranch(scanner);
                        if (branch == null || branch.isEmpty()) {
                            System.out.println("Invalid branch. Please try again.");
                            continue;
                        }
    
                        try {
                            credentialsManager = CredentialsManager.getInstance(branch);
                            employeeManager = EmployeeManager.getInstance(branch);
                        } catch (NullPointerException e) {
                            System.out.println("Error: Unable to load branch information. Please contact support.");
                            continue;
                        }
    
                        String verdict = authenticateUser(scanner, credentialsManager, true, employeeManager);
                        if (handleVerdict(verdict)) {
                            System.out.println("Thank you for using the Store Management System. Goodbye!");
                            return null;
                        }
                        if (verdict.equals("OK")) {
                            return new String[]{"Manager", branch};
                        }
                    }
                    case 2 -> {
                        System.out.println("\nEmployee Login:");
                        String branch = validateBranch(scanner);
                        if (branch == null || branch.isEmpty()) {
                            System.out.println("Invalid branch. Please try again.");
                            continue;
                        }
    
                        try {
                            credentialsManager = CredentialsManager.getInstance(branch);
                            employeeManager = EmployeeManager.getInstance(branch);
                        } catch (NullPointerException e) {
                            System.out.println("Error: Unable to load branch information. Please contact support.");
                            continue;
                        }
    
                        String verdict = authenticateUser(scanner, credentialsManager, false, employeeManager);
                        if (handleVerdict(verdict)) {
                            System.out.println("Thank you for using the Store Management System. Goodbye!");
                            return null;
                        }
                        if (verdict.equals("OK")) {
                            return new String[]{"Employee", branch};
                        }
                    }
                    case 3 -> {
                        System.out.println("Exiting the system. Goodbye!");
                        return null;
                    }
                    default -> System.out.println("Invalid choice. Please enter a number between 1 and 3.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number (1, 2, or 3).");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
    

    private static boolean handleVerdict(String verdict) {
        switch (verdict) {
            case "RESTART" -> { return false; }
            case "EXIT" -> { return true; }
        }
        return false;
    }
    

    public static boolean ManagerScreen(Scanner scanner, String branch) {
        EmployeeManager employeeManagers = null;
        CredentialsManager credentialsManager = null;
        InventoryManager inventoryManagers = null;
        CustomerManager customerManagers = null;
        ReportManager reportManager = null;
    
        while (true) {
            try {
                System.out.println("\nManager Menu (" + branch + "):");
                System.out.println("1. Add New User (New Employee to the system)");
                System.out.println("2. Manage Employees");
                System.out.println("3. Manage Inventory");
                System.out.println("4. Manage Customers");
                System.out.println("5. Manage Reports");
                System.out.println("6. Display Logs");
                System.out.println("7. Go Back");
                System.out.println("8. Exit");
                System.out.print("Enter your choice: ");
    
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
    
                switch (choice) {
                    case 1 -> {
                        while (true) {
                            System.out.println("\nWhich user do you want to add?");
                            System.out.println("1. Manager user");
                            System.out.println("2. Employee user");
                            System.out.println("3. Go Back");
                            System.out.print("Choose an option: ");
    
                            try {
                                int userChoice = scanner.nextInt();
                                scanner.nextLine();
    
                                if (userChoice == 1 || userChoice == 2) {
                                    String userBranch = validateBranch(scanner);
                                    if (userBranch == null || userBranch.isEmpty()) {
                                        System.out.println("Invalid branch. Please try again.");
                                        continue;
                                    }
    
                                    credentialsManager = CredentialsManager.getInstance(userBranch);
                                    if (credentialsManager == null) {
                                        System.out.println("Error: Unable to load user credentials for the branch.");
                                        continue;
                                    }
    
                                    String userName = registerNewAccount(scanner, credentialsManager, userBranch);
                                    employeeManagers = EmployeeManager.getInstance(userBranch);
                                    if (employeeManagers == null) {
                                        System.out.println("Error: Unable to initialize employee management for the branch.");
                                        continue;
                                    }
    
                                    createNewEmployee(employeeManagers, scanner, userChoice == 1, userBranch, userName);
                                    break;
                                } else if (userChoice == 3) {
                                    System.out.println("Returning to the Manager Menu...");
                                    break;
                                } else {
                                    System.out.println("Invalid choice, please try again.");
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid input. Please enter a valid number (1, 2, or 3).");
                                scanner.nextLine(); // Clear invalid input
                            }
                        }
                    }
                    case 2 -> {
                        String userBranch = validateBranch(scanner);
                        if (userBranch == null || userBranch.isEmpty()) {
                            System.out.println("Invalid branch. Please try again.");
                            continue;
                        }
    
                        employeeManagers = EmployeeManager.getInstance(userBranch);
                        credentialsManager = CredentialsManager.getInstance(userBranch);
                        if (employeeManagers == null || credentialsManager == null) {
                            System.out.println("Error: Unable to load resources for employee management.");
                            continue;
                        }
    
                        manageEmployees(credentialsManager, employeeManagers, userBranch, scanner);
                    }
                    case 3 -> {
                        String userBranch = validateBranch(scanner);
                        if (userBranch == null || userBranch.isEmpty()) {
                            System.out.println("Invalid branch. Please try again.");
                            continue;
                        }
    
                        inventoryManagers = InventoryManager.getInstance(userBranch);
                        if (inventoryManagers == null) {
                            System.out.println("Error: Unable to load inventory resources.");
                            continue;
                        }
    
                        manageInventory(inventoryManagers, userBranch, scanner);
                    }
                    case 4 -> {
                        String userBranch = validateBranch(scanner);
                        if (userBranch == null || userBranch.isEmpty()) {
                            System.out.println("Invalid branch. Please try again.");
                            continue;
                        }
    
                        customerManagers = CustomerManager.getInstance(userBranch);
                        if (customerManagers == null) {
                            System.out.println("Error: Unable to load customer resources.");
                            continue;
                        }
    
                        manageCustomers(customerManagers, userBranch, scanner);
                    }
                    case 5 -> {
                        String userBranch = validateBranch(scanner);
                        if (userBranch == null || userBranch.isEmpty()) {
                            System.out.println("Invalid branch. Please try again.");
                            continue;
                        }
    
                        reportManager = ReportManager.getInstance(userBranch);
                        if (reportManager == null) {
                            System.out.println("Error: Unable to load report resources.");
                            continue;
                        }
    
                        manageReports(reportManager, userBranch, scanner);
                    }
                    case 6 -> {
                        System.out.println("--- System Logs ---");
                        if (logManager == null || logManager.getLogs().isEmpty()) {
                            System.out.println("No logs available.");
                        } else {
                            for (Log log : logManager.getLogs()) {
                                System.out.println(log);
                            }
                        }
                    }
                    case 7 -> {
                        System.out.println("Returning to the login menu...");
                        return true; // Go back to the login menu
                    }
                    case 8 -> {
                        System.out.println("Exiting Manager Menu. Goodbye!");
                        return false; // Exit the system
                    }
                    default -> System.out.println("Invalid choice. Please enter a number between 1 and 8.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number (1 to 8).");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
    
    
    public static boolean EmployeeScreen(Scanner scanner, String branch) {
        EmployeeManager employeeManager = EmployeeManager.getInstance(branch);
        InventoryManager inventoryManager = InventoryManager.getInstance(branch);
        CustomerManager customerManager = CustomerManager.getInstance(branch);
        ReportManager reportManager = ReportManager.getInstance(branch);
    
        if (employeeManager == null || inventoryManager == null || customerManager == null || reportManager == null) {
            System.out.println("Error: Unable to load resources for the branch. Please contact support.");
            return false; // Exit the system if resources cannot be initialized
        }
    
        while (true) {
            try {
                System.out.println("\nBranch User Menu (" + branch + "):");
                System.out.println("1. Make Transaction");
                System.out.println("2. Display Branch Employees");
                System.out.println("3. Manage Inventory");
                System.out.println("4. Manage Customers");
                System.out.println("5. Manage Reports");
                System.out.println("6. Go Back");
                System.out.println("7. Exit");
                System.out.print("Enter your choice: ");
    
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
    
                switch (choice) {
                    case 1 -> TransactionScreen(inventoryManager, customerManager, scanner, branch, reportManager);
    
                    case 2 -> {
                        System.out.println("\nEmployees:");
                        Map<String, Employee> employees = employeeManager.getAllEmployees();
    
                        if (employees == null || employees.isEmpty()) {
                            System.out.println("No employees found for this branch.");
                        } else {
                            employees.forEach((id, employee) -> {
                                System.out.println("ID: " + id);
                                System.out.println(employee);
                                System.out.println("--------------------------");
                            });
                        }
                    }
    
                    case 3 -> manageInventory(inventoryManager, branch, scanner);
    
                    case 4 -> manageCustomers(customerManager, branch, scanner);
    
                    case 5 -> manageReports(reportManager, branch, scanner);
    
                    case 6 -> {
                        System.out.println("Returning to the login screen...");
                        return true; // Indicate going back to the login screen
                    }
    
                    case 7 -> {
                        System.out.println("Exiting Branch User Menu. Goodbye!");
                        return false; // Indicate exiting the system
                    }
    
                    default -> System.out.println("Invalid choice. Please enter a number between 1 and 7.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number (1 to 7).");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
    

    public static void TransactionScreen(InventoryManager inventoryManager, CustomerManager customerManager, Scanner scanner, String branch, ReportManager reportManager) {
    Map<String, Product> inventory = inventoryManager.getInventory();
    Map<String, Integer> cart = new HashMap<>();
    double totalSum = 0;

    while (true) {
        try {
            System.out.println("\n--- Transaction Menu ---");
            System.out.println("1. View Products");
            System.out.println("2. View Cart");
            System.out.println("3. Checkout");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int menuChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (menuChoice) {
                case 1 -> {
                    System.out.println("\nAvailable Products:");
                    List<String> keys = new ArrayList<>(inventory.keySet());

                    if (keys.isEmpty()) {
                        System.out.println("No products available.");
                        break;
                    }

                    for (int i = 1; i <= keys.size(); i++) {
                        System.out.println(i + ". " + inventory.get(keys.get(i - 1)));
                    }

                    System.out.print("\nChoose a product (or -1 to go back): ");
                    int productChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    if (productChoice > 0 && productChoice <= keys.size()) {
                        String selectedKey = keys.get(productChoice - 1);
                        Product selectedProduct = inventory.get(selectedKey);

                        System.out.print("Enter quantity: ");
                        int quantity = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        if (quantity > 0 && selectedProduct.getQuantity() >= quantity) {
                            cart.put(selectedKey, cart.getOrDefault(selectedKey, 0) + quantity);
                            totalSum += selectedProduct.getPrice() * quantity;
                            System.out.println(quantity + " units of " + selectedProduct.getName() + " added to cart.");
                        } else {
                            System.out.println("Insufficient stock or invalid quantity.");
                        }
                    } else if (productChoice != -1) {
                        System.out.println("Invalid product selection.");
                    }
                }

                case 2 -> {
                    System.out.println("\n--- Cart ---");
                    if (cart.isEmpty()) {
                        System.out.println("Cart is empty.");
                    } else {
                        for (String key : cart.keySet()) {
                            Product product = inventory.get(key);
                            int quantity = cart.get(key);
                            System.out.printf("%s - Quantity: %d - Subtotal: $%.2f%n",
                                    product.getName(), quantity, product.getPrice() * quantity);
                        }
                        System.out.printf("Total: $%.2f%n", totalSum);
                    }
                }

                case 3 -> {
                    System.out.println("\nProceeding to checkout...");
                    if (cart.isEmpty()) {
                        System.out.println("Cart is empty. Add products before checking out.");
                        break;
                    }

                    System.out.println("Is the customer already registered?");
                    System.out.println("1. Yes");
                    System.out.println("2. No");
                    System.out.print("Choose an option: ");
                    int customerChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    Customer customer = null;
                    if (customerChoice == 1) {
                        System.out.print("Enter customer ID: ");
                        String customerId = scanner.nextLine();
                        customer = customerManager.findCustomerById(customerId);
                        if (customer == null) {
                            System.out.println("Customer not found. Registering new customer.");
                            customer = createNewCustomer(customerManager, scanner, branch);
                        }
                    } else if (customerChoice == 2) {
                        customer = createNewCustomer(customerManager, scanner, branch);
                    } else {
                        System.out.println("Invalid choice. Returning to menu.");
                        break;
                    }

                    if (customer != null) {
                        double discount = customerManager.getDiscountForType(customer.getType());
                        double discountedTotal = totalSum * (1 - discount);

                        System.out.printf("Customer type: %s%n", customer.getType());
                        System.out.printf("Eligible for a discount of %.0f%%%n", discount * 100);
                        System.out.printf("Total after discount: $%.2f%n", discountedTotal);

                        System.out.println("Do you want to proceed with the transaction?");
                        System.out.println("1. Yes");
                        System.out.println("2. No");
                        System.out.print("Choose an option: ");
                        int finalChoice = scanner.nextInt();

                        if (finalChoice == 1) {
                            inventoryManager.sellProducts(cart, branch);
                            soldProductToRecord(cart, inventoryManager);
                            System.out.println("Transaction completed successfully! Thank you for your purchase.");
                            ReportController.updateReport(cart, branch, inventoryManager, reportManager);
                            return;
                        } else {
                            System.out.println("Transaction canceled.");
                        }
                    } else {
                        System.out.println("Customer information is missing. Unable to complete the transaction.");
                    }
                }

                case 4 -> {
                    System.out.println("Transaction canceled.");
                    return;
                }

                default -> System.out.println("Invalid option. Please choose again.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.nextLine(); // Clear invalid input
        }
    }
}


    public static void soldProductToRecord(Map<String, Integer> cart, InventoryManager inventoryManager) {
        if (cart == null || cart.isEmpty()) {
            System.out.println("No products in the cart to record.");
            return;
        }

        cart.forEach((serialNum, amount) -> {
            Product product = inventoryManager.getProduct(serialNum);
            if (product == null) {
                System.out.println("Warning: Product with serial number " + serialNum + " not found in inventory.");
                return;
            }

            String productName = product.getName();
            if (productName == null || productName.isEmpty()) {
                System.out.println("Warning: Product with serial number " + serialNum + " has no valid name.");
                return;
            }

            LogController.addRecordToLog(logManager, "Sold Product", productName + " - " + amount + " Devices");
            System.out.println("Recorded sale: " + productName + " - " + amount + " Devices");
        });
    }




    
    public static String validateBranch(Scanner scanner) {
        System.out.print("Enter the specific Branch (Eilat or Jerusalem): ");
        String userBranch = scanner.nextLine();
        userBranch = userBranch.toUpperCase();

        while ((!userBranch.equals("EILAT")) && (!userBranch.equals("JERUSALEM"))) {
            System.out.print("Branch not recognized. Please verify the branch name and try again: ");
            userBranch = scanner.nextLine();
            userBranch = userBranch.toUpperCase();
        }
        return userBranch;
    }

    private static String registerNewAccount(Scanner scanner, CredentialsManager credentialManager, String branch) {
        String userName;
    
        // Username validation
        while (true) {
            System.out.print("Choose a username: ");
            userName = scanner.nextLine().trim();

            if (userName.isEmpty()) {
                System.out.println("Username cannot be empty. Please try again.");
                continue;
            }
            if (credentialManager.checkUserName(userName)) {
                break; // Username is valid
            } else {
                System.out.println("Username already exists. Please choose a different username.");
            }
        }
    
        // Display password requirements
        displayPasswordRequirements();
    
        // Password validation
        String password;
        while (true) {
            System.out.print("Choose a password: ");
            password = scanner.nextLine().trim();
    
            if (password.isEmpty()) {
                System.out.println("Password cannot be empty. Please try again.");
                continue;
            }
    
            if (credentialManager.isValidPassword(password)) {
                break; // Password is valid
            } else {
                System.out.println("Password does not meet the requirements. Please try again.");
            }
        }
    
        // Add user to repository
        CredentialController.addUserToRepo(credentialManager, userName, password, branch);
        System.out.println("User added successfully: " + userName);
        System.out.println("Please provide employee details: ");
    
        return userName;
    }
    
    private static String authenticateUser(Scanner scanner, CredentialsManager credentialManager, boolean isManagerLogin, EmployeeManager employeeManager) {
    String[] userInfo = getUserInfo(scanner);
    int tries = 3;

    while (tries > 0) {
        if (credentialManager.authenticate(userInfo[0], userInfo[1])) {
            break; // Successfully authenticated
        }

        tries--;
        System.out.printf("Invalid username or password. %d attempt%s remaining.%n", tries, tries > 1 ? "s" : "");
        
        if (tries > 0) {
            System.out.print("Do you want to retry (Y/N)? ");
            String response = scanner.nextLine().trim().toUpperCase();
            if (response.equals("N")) {
                System.out.println("Exiting authentication process.");
                return "EXIT";
            }
        } else {
            System.out.println("Too many failed attempts. Access denied.");
            return handleTooManyAttempts(scanner);
        }

        if (tries == 1) {
            System.out.println("Last try. Please pay attention!");
        }
        userInfo = getUserInfo(scanner);
    }

        // Ensure the employee exists
        Employee employee = employeeManager.getEmployee(userInfo[0]);
        if (employee == null) {
            System.out.println("Error: Employee not found.");
            return "EXIT";
        }

        String role = employee.getRole();

        if (isManagerLogin) {
            if (!role.equals("Manager")) {
                System.out.println("Access denied: This employee is not a manager.");
                return "EXIT";
            }
        } else {
            if (role.equals("Manager")) {
                System.out.println("Access denied: This employee is a manager. Please log in as a regular employee.");
                return "EXIT";
            }
        }

        System.out.println("Welcome to the system, " + userInfo[0] + "!");
        return "OK";
    }


    private static String handleTooManyAttempts(Scanner scanner) {
        System.out.println("Too many failed attempts. Please select an option:");
    
        while (true) {
            try {
                System.out.println("1. Restart");
                System.out.println("2. Exit");
                System.out.print("Enter your choice (1 or 2): ");
                int userChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
    
                switch (userChoice) {
                    case 1 -> {
                        System.out.println("Restarting...");
                        return "RESTART";
                    }
                    case 2 -> {
                        System.out.println("Exiting the system. Goodbye!");
                        return "EXIT";
                    }
                    default -> System.out.println("Invalid choice. Please enter 1 to restart or 2 to exit.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number (1 or 2).");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
    

    private static String[] getUserInfo(Scanner scanner) {
        String userName;
        String password;
    
        // Get and validate username
        while (true) {
            System.out.print("Enter username: ");
            userName = scanner.nextLine().trim();
            if (!userName.isEmpty()) {
                break;
            } else {
                System.out.println("Username cannot be empty. Please try again.");
            }
        }
    
        // Get and validate password
        while (true) {
            System.out.print("Enter password: ");
            password = scanner.nextLine().trim();
            if (!password.isEmpty()) {
                break;
            } else {
                System.out.println("Password cannot be empty. Please try again.");
            }
        }
    
        return new String[]{userName, password};
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


    public static void manageInventory(InventoryManager inventoryManager, String branch, Scanner scanner) {
        System.out.println("\nInventory Management:");
        System.out.println("1. Add Product");
        System.out.println("2. Remove Product");
        System.out.println("3. View Inventory");
        System.out.print("Enter your choice: ");
    
        int choice;
        try {
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number between 1 and 3.");
            scanner.nextLine(); // Clear invalid input
            return;
        }
    
        switch (choice) {
            case 1 -> addProduct(scanner, inventoryManager, branch);
            case 2 -> removeProduct(scanner, inventoryManager, branch);
            case 3 -> viewInventory(inventoryManager);
            default -> System.out.println("Invalid choice. Returning to the main menu.");
        }
    }
    
    private static void addProduct(Scanner scanner, InventoryManager inventoryManager, String branch) {
        System.out.println("\n--- Add Product ---");
        System.out.print("Enter Product Serial Number: ");
        String id = scanner.nextLine().trim();
    
        while (id.isEmpty()) {
            System.out.print("Product Serial Number cannot be empty. Please enter a valid Product Serial Number: ");
            id = scanner.nextLine().trim();
        }
    
        System.out.print("Enter Product Name: ");
        String name = scanner.nextLine().trim();
    
        while (name.isEmpty()) {
            System.out.print("Product Name cannot be empty. Please enter a valid Product Name: ");
            name = scanner.nextLine().trim();
        }
    
        double price = 0;
        while (true) {
            System.out.print("Enter Price: ");
            try {
                price = scanner.nextDouble();
                if (price <= 0) {
                    System.out.println("Price must be greater than 0. Please try again.");
                    continue;
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid price.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    
        int quantity = 0;
        while (true) {
            System.out.print("Enter Quantity: ");
            try {
                quantity = scanner.nextInt();
                if (quantity < 0) {
                    System.out.println("Quantity cannot be negative. Please try again.");
                    continue;
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid quantity.");
                scanner.nextLine(); // Clear invalid input
            }
        }
        scanner.nextLine(); // Consume newline
    
        Product product = new Product(id, name, price, quantity, branch);
        InventoryController.addProductToRepo(inventoryManager, product, branch);
        System.out.println("Product added successfully!");
    }
    
    private static void removeProduct(Scanner scanner, InventoryManager inventoryManager, String branch) {
        System.out.println("\n--- Remove Product ---");
        System.out.print("Enter Product Serial Number to remove: ");
        String id = scanner.nextLine().trim();
    
        if (id.isEmpty()) {
            System.out.println("Product Serial Number cannot be empty.");
            return;
        }
    
        boolean isProductRemoved = InventoryController.removeProductFromRepo(inventoryManager, id, branch);
        if (isProductRemoved) {
            System.out.println("Product removed successfully!");
        } else {
            System.out.println("Product not found in inventory.");
        }
    }
    

    public static void viewInventory(InventoryManager inventoryManager) {
        System.out.println("Inventory:");
        Map<String, Product> inventory = inventoryManager.getInventory();
    
        if (inventory == null || inventory.isEmpty()) {
            System.out.println("No products found.");
        } else {
            int i = 1;
            for (Map.Entry<String, Product> entry : inventory.entrySet()) {
                System.out.println(i + ". " + entry.getValue().toString());
                System.out.println("--------------------------");
                i++;
            }
        }
    }
    
    public static void manageEmployees(CredentialsManager credentialsManager, EmployeeManager employeeManager, String branch, Scanner scanner) {
        System.out.println("\nEmployee Management:");
        System.out.println("1. Remove Employee");
        System.out.println("2. View Employees");
        System.out.print("Enter your choice: ");
    
        int choice;
        try {
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number between 1 and 2.");
            scanner.nextLine(); // Clear invalid input
            return;
        }
    
        switch (choice) {
            case 1 -> removeEmployee(scanner, employeeManager, credentialsManager, branch);
            case 2 -> viewEmployees(employeeManager);
            default -> System.out.println("Invalid choice. Returning to main menu.");
        }
    }
    
    private static void removeEmployee(Scanner scanner, EmployeeManager employeeManager, CredentialsManager credentialsManager, String branch) {
        System.out.print("Enter the username of the employee to remove: ");
        String userName = scanner.nextLine().trim();
    
        if (userName.isEmpty()) {
            System.out.println("Username cannot be empty.");
            return;
        }
    
        Employee employee = employeeManager.getEmployee(userName);
        if (employee == null) {
            System.out.println("Employee not found in the system.");
            return;
        }
    
        boolean removeEmployee = EmployeeController.removeEmployeeFromRepo(employeeManager, userName, branch);
        boolean removeUser = CredentialController.removeUserFromRepo(credentialsManager, userName, branch);
    
        if (removeEmployee && removeUser) {
            LogController.addRecordToLog(logManager, "Delete employee", employee.toString());
            System.out.println("Employee removed successfully!");
        } else {
            System.out.println("Error: Unable to remove employee from the system.");
        }
    }
    
    private static void viewEmployees(EmployeeManager employeeManager) {
        System.out.println("\n--- Employees ---");
        Map<String, Employee> employees = employeeManager.getAllEmployees();
    
        if (employees == null || employees.isEmpty()) {
            System.out.println("No employees found.");
        } else {
            int i = 1;
            for (Map.Entry<String, Employee> entry : employees.entrySet()) {
                System.out.println(i + ". " + entry.getValue().toString());
                System.out.println("--------------------------");
                i++;
            }
        }
    }
    

    public static void manageCustomers(CustomerManager customerManager, String branch, Scanner scanner) {
        System.out.println("\nCustomer Management:");
        System.out.println("1. Add Customer");
        System.out.println("2. Remove Customer");
        System.out.println("3. View Customers");
        System.out.print("Enter your choice: ");
    
        int choice;
        try {
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number between 1 and 3.");
            scanner.nextLine(); // Clear invalid input
            return;
        }
    
        switch (choice) {
            case 1 -> createNewCustomer(customerManager, scanner, branch);
            case 2 -> removeCustomer(scanner, customerManager, branch);
            case 3 -> viewCustomers(customerManager);
            default -> System.out.println("Invalid choice. Returning to the main menu.");
        }
    }
    
    private static void removeCustomer(Scanner scanner, CustomerManager customerManager, String branch) {
        System.out.print("Enter Customer ID to remove: ");
        String customerId = scanner.nextLine().trim();
    
        if (customerId.isEmpty()) {
            System.out.println("Customer ID cannot be empty.");
            return;
        }
    
        Customer customer = customerManager.findCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found in the system.");
            return;
        }
    
        boolean removeCustomer = CustomerController.RemoveCustomerFromRepo(customerManager, customerId, branch);
        if (removeCustomer) {
            LogController.addRecordToLog(logManager, "Delete customer", customer.toString());
            System.out.println("Customer removed successfully!");
        } else {
            System.out.println("Error: Unable to remove customer from the system.");
        }
    }
    
    private static void viewCustomers(CustomerManager customerManager) {
        System.out.println("\n--- Customers ---");
        Map<String, Customer> customers = customerManager.getAllCustomers();
    
        if (customers == null || customers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            int i = 1;
            for (Map.Entry<String, Customer> entry : customers.entrySet()) {
                System.out.println(i + ". " + entry.getValue().toString());
                System.out.println("--------------------------");
                i++;
            }
        }
    }
    

    public static void manageReports(ReportManager reportManager, String branch, Scanner scanner) {
        System.out.println("\nReports Management:");
        System.out.println("1. Display Report By Specific Date");
        System.out.println("2. Display All Reports");
        System.out.println("3. Delete Specific Report By Date");
        System.out.print("Enter your choice: ");

        int choice;
        try {
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number between 1 and 3.");
            scanner.nextLine(); // Clear invalid input
            return;
        }

        switch (choice) {
            case 1 -> displayReportByDate(scanner, reportManager);
            case 2 -> displayAllReports(reportManager);
            case 3 -> deleteReportByDate(scanner, reportManager, branch);
            default -> System.out.println("Invalid choice. Returning to main menu.");
        }
    }

    private static void displayReportByDate(Scanner scanner, ReportManager reportManager) {
        System.out.print("Enter a specific date in the format (dd/MM/yyyy): ");
        String day = scanner.nextLine().trim();

        if (!isValidDate(day)) {
            System.out.println("Invalid date format. Please use the format (dd/MM/yyyy).");
            return;
        }

        Report report = reportManager.getReport(day);
        if (report != null) {
            System.out.println("\n--- Report ---");
            System.out.println(report.generateReportString());
        } else {
            System.out.println("No report found for the specified date.");
        }
    }

    private static void displayAllReports(ReportManager reportManager) {
        System.out.println("\n--- All Reports ---");
        Map<String, Report> reports = reportManager.getAllReports();

        if (reports == null || reports.isEmpty()) {
            System.out.println("No reports available.");
            return;
        }

        reports.forEach((year, report) -> {
            System.out.println(report);
            System.out.println("--------------------------");
        });
    }

    private static void deleteReportByDate(Scanner scanner, ReportManager reportManager, String branch) {
        System.out.print("Enter a specific date in the format (dd/MM/yyyy): ");
        String day = scanner.nextLine().trim();

        if (!isValidDate(day)) {
            System.out.println("Invalid date format. Please use the format (dd/MM/yyyy).");
            return;
        }

        boolean removed = ReportController.RemoveReportFromRepo(reportManager, day, branch);
        if (removed) {
            System.out.println("Report removed successfully!");
        } else {
            System.out.println("No report found for the specified date.");
        }
    }

    private static boolean isValidDate(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }


    public static void createNewEmployee(EmployeeManager employeeManager, Scanner scanner, boolean manager, String userBranch, String userName) {
        String id = getNonEmptyInput(scanner, "Enter Employee ID: ");
        String name = getNonEmptyInput(scanner, "Enter Employee Name: ");
        String phoneNumber = getNonEmptyInput(scanner, "Enter Phone Number: ");
        String accountNumber = getNonEmptyInput(scanner, "Enter Account Number: ");
    
        String role;
        if (manager) {
            role = "Manager";
        } else {
            role = getEmployeeRole(scanner);
        }
    
        Employee employee = new Employee(name, id, phoneNumber, accountNumber, userBranch, role, userName);
        EmployeeController.addEmployeeToRepo(employee, employeeManager, userBranch);
        LogController.addRecordToLog(logManager, "Adding new employee", employee.toString());
        System.out.println("Employee has been added successfully!");
    }
    
    private static String getNonEmptyInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("Input cannot be empty. Please try again.");
            }
        }
    }
    
    private static String getEmployeeRole(Scanner scanner) {
        while (true) {
            System.out.println("Choose a Role: ");
            System.out.println("1. Cashier");
            System.out.println("2. Salesperson");
            System.out.print("Enter your choice: ");
    
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
    
                if (choice == 1) {
                    return "Cashier";
                } else if (choice == 2) {
                    return "Salesperson";
                } else {
                    System.out.println("Invalid choice. Please enter 1 or 2.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number (1 or 2).");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
    

    public static Customer createNewCustomer(CustomerManager customerManager, Scanner scanner, String branch) {
        String name = getNonEmptyInput(scanner, "Enter Customer Name: ");
        String id = getNonEmptyInput(scanner, "Enter Customer ID: ");
        String phoneNumber = getNonEmptyInput(scanner, "Enter Phone Number: ");
    
        String type = getCustomerType(scanner);
    
        Customer customer = new Customer(name, id, phoneNumber, type, branch);
    
        // Add the new customer to the repository
        CustomerController.addCustomerToRepo(customerManager, customer, branch);
        LogController.addRecordToLog(logManager, "Adding new customer", customer.toString());
        System.out.println("Customer has been added successfully!");
        return customer;
    }
    

    private static String getCustomerType(Scanner scanner) {
        while (true) {
            System.out.println("Enter Customer Type:");
            System.out.println("1. New");
            System.out.println("2. Returning");
            System.out.println("3. VIP");
            System.out.print("Choose your choice: ");
    
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
    
                switch (choice) {
                    case 1 -> {
                        return "New";
                    }
                    case 2 -> {
                        return "Returning";
                    }
                    case 3 -> {
                        return "VIP";
                    }
                    default -> System.out.println("Invalid choice. Please select 1, 2, or 3.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number (1, 2, or 3).");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
    
}



