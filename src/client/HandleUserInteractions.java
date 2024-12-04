package client;

import server.InventoryManager;
import server.CustomerManager;
import server.UserCredentialsManager;
import server.EmployeeManager;
import models.Customer;
import models.Employee;
import models.Product;
import models.UserCredentials;
import java.util.Scanner;
import com.google.gson.Gson;
import utils.CustomerFileHandler;
import utils.EmployeeFileHandler;
import utils.InventoryFileHandler;
import utils.UserCredentialsFileHandler;
import java.util.List;
import java.util.Map;
import java.util.InputMismatchException;

public class HandleUserInteractions {


    public static String[] userLogin(Scanner scanner) {
        UserCredentialsManager credentialsManager;
        System.out.println("Welcome to the Store Management System!");
    
        while (true) {
            try {
                System.out.println("Please select an option:");
                System.out.println("1. Log in as Admin");
                System.out.println("2. Log in as Branch User");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
    
                int userChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            
                switch (userChoice) {
                    case 1 -> {
                        System.out.println("Admin Login:");
                        credentialsManager = UserCredentialsManager.getInstance("ADMINS");
                        String verdict = authenticateUser(scanner, credentialsManager);

                        if (verdict.equals("RESTART")) {
                            continue;
                        }
                        if (verdict.equals("EXIT")) {
                            return null;
                        }
                        if (verdict.equals("OK")) {
                            return new String[]{"Admin", null};
                        }
                    }
                    case 2 -> {
                        String branch = validateBranch(scanner);
                        credentialsManager = UserCredentialsManager.getInstance(branch);

 
                        String verdict = authenticateUser(scanner, credentialsManager);

                        if (verdict.equals("RESTART")) {
                            continue;
                        }
                        if (verdict.equals("EXIT")) {
                            return null;
                        }
                        if (verdict.equals("OK")) {
                            return new String[]{"Branch_User", branch};
                        }
                    }
                    case 3 -> {
                        System.out.println("Exiting the system. Goodbye!");
                        return null;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number (1, 2, or 3).");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }


    public static boolean adminScreen(Scanner scanner) {
        EmployeeManager employeeManagers;
        UserCredentialsManager credentialsManager;
        InventoryManager inventoryManagers;
        CustomerManager customerManagers;
    
        while (true) { // Remove redundant `running` variable
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Add New User");
            System.out.println("2. Manage Employees");
            System.out.println("3. Manage Inventory");
            System.out.println("4. Manage Customers");
            System.out.println("5. Go Back");
            System.out.println("6. Exit");
    
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
    
            switch (choice) {
                case 1 -> {
                    while (true) {
                        System.out.println("Which user do you want to add?");
                        System.out.println("1. Admin user");
                        System.out.println("2. Branch user");
                        System.out.println("3. Go Back");
                        System.out.print("Choose an option: ");
                        int userChoice = scanner.nextInt();
                        scanner.nextLine();
    
                        if (userChoice == 1) {
                            credentialsManager = UserCredentialsManager.getInstance("ADMINS");
                            registerNewAccount(scanner, credentialsManager, "ADMINS");
                            break;
                        } else if (userChoice == 2) {
                            String userBranch = validateBranch(scanner);
                            credentialsManager = UserCredentialsManager.getInstance(userBranch);
                            registerNewAccount(scanner, credentialsManager, userBranch);
                            employeeManagers = EmployeeManager.getInstance(userBranch);
                            createNewEmployee(employeeManagers, scanner);
                            break;
                        } else if (userChoice == 3) {
                            System.out.println("Returning to the Admin Menu...");
                            break;
                        } else {
                            System.out.println("Invalid choice, please try again.");
                        }
                    }
                }
                case 2 -> {
                    String userBranch = validateBranch(scanner);
                    employeeManagers = EmployeeManager.getInstance(userBranch);
                    credentialsManager = UserCredentialsManager.getInstance(userBranch);
                    manageEmployees(credentialsManager, employeeManagers, userBranch, scanner);
                }
                case 3 -> {
                    String userBranch = validateBranch(scanner);
                    inventoryManagers = InventoryManager.getInstance(userBranch);
                    manageInventory(inventoryManagers, userBranch, scanner);
                }
                case 4 -> {
                    String userBranch = validateBranch(scanner);
                    customerManagers = CustomerManager.getInstance(userBranch);
                    manageCustomers(customerManagers, userBranch, scanner);
                }
                case 5 -> {
                    System.out.println("Returning to the login menu...");
                    return true; // Go back to the login menu
                }
                case 6 -> {
                    System.out.println("Exiting Admin Menu. Goodbye!");
                    return false; // Exit the system
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    


    public static String validateBranch(Scanner scanner) {
        System.out.print("Enter the specific Branch: ");
        String userBranch = scanner.nextLine();
        userBranch = userBranch.toUpperCase();

        while ((!userBranch.equals("EILAT")) && (!userBranch.equals("JERUSALEM"))) {
            System.out.print("Branch not recognized. Please verify the branch name and try again: ");
            userBranch = scanner.nextLine();
            userBranch = userBranch.toUpperCase();
        }
        return userBranch;
    }

    public static boolean userScreen(Scanner scanner, String branch) {
        EmployeeManager employeeManagers = EmployeeManager.getInstance(branch);
        InventoryManager inventoryManagers = InventoryManager.getInstance(branch);
        CustomerManager customerManagers = CustomerManager.getInstance(branch);
    
        while (true) {
            System.out.println("\nBranch User Menu (" + branch + "):");
            System.out.println("1. Display Employees");
            System.out.println("2. Manage Inventory");
            System.out.println("3. Manage Customers");
            System.out.println("4. Go Back");
            System.out.println("5. Exit");
    
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
    
            switch (choice) {
                case 1 -> displayEmployees(employeeManagers);
                case 2 -> manageInventory(inventoryManagers, branch, scanner);
                case 3 -> manageCustomers(customerManagers, branch, scanner);
                case 4 -> {
                    System.out.println("Returning to the login screen...");
                    return true; // Indicate going back to the login screen
                }
                case 5 -> {
                    System.out.println("Exiting Branch User Menu. Goodbye!");
                    return false; // Indicate exiting the system
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    


    private static void registerNewAccount(Scanner scanner, UserCredentialsManager credentailManager, String branch) {
        System.out.print("Choose a username: ");
        String username = scanner.nextLine();
        while (!credentailManager.checkUserName(username)) {
            System.out.println("Username already exists. Please choose a different username.");
        }

        String password;
        displayPasswordRequirements();
        while (true) {
            System.out.print("Choose a password: ");
            password = scanner.nextLine();

            if (credentailManager.isValidPassword(password)) {
                break;
            } else {
                System.out.println("Password does not meet the requirements. Please try again.");
            }
        }

        System.out.println("Account information verified successfully.");

        credentailManager.addCredentials(username, password, branch);
        credentailManager.saveCredentials(branch);
        System.out.println("Account created successfully! Please log in with your new account.");
    }


    private static String authenticateUser(Scanner scanner, UserCredentialsManager credentailManager) {
        String[] userInfo = getUserInfo(scanner);
        String userName = userInfo[0];
        String password = userInfo[1];
        int attempts = 1;
        while (!credentailManager.authenticate(userName, password)) {
            attempts++;
            System.out.println("Invalid username or password. Attempt " + attempts + ".");
            userInfo = getUserInfo(scanner);

            if (attempts == 2) {
                System.out.println("Last try, pay attention!");
            }
            if (attempts >= 3) {
                return handleTooManyAttempts(scanner);
            }
        }

        System.out.println("Hey " + userName + ", welcome to the system!");
        return "OK";
    }


    private static String handleTooManyAttempts(Scanner scanner) {
        System.out.println("Too many failed attempts, please select an option: ");

        while (true) {
            try {
                System.out.println("1. For restart");
                System.out.println("2. For exit");
                System.out.print("Select a choice: ");
                int userChoice = scanner.nextInt();
                scanner.nextLine();

                if (userChoice == 1) {
                    System.out.println("Restarting...");
                    return "RESTART";
                } else if (userChoice == 2) {
                    System.out.println("Exiting the system. Goodbye!");
                    return "EXIT";
                } else {
                    System.out.println("Invalid choice, please try again.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number (1 or 2).");
                scanner.nextLine(); // Clear invalid input
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }


    private static String[] getUserInfo(Scanner scanner) {
        System.out.print("Enter username: ");
        String userName = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
    
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



    
    public static void manageInventory(InventoryManager inventoryManager, String branch,  Scanner scanner) {
        System.out.println("\nInventory Management:");
        System.out.println("1. Add Product");
        System.out.println("2. Remove Product");
        System.out.println("3. View Inventory");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
    
        switch (choice) {
            case 1 -> {
                System.out.print("Enter Product ID: ");
                String id = scanner.nextLine();
                System.out.print("Enter Product Name: ");
                String name = scanner.nextLine();
                System.out.print("Enter Category: ");
                String category = scanner.nextLine();
                System.out.print("Enter Price: ");
                double price = scanner.nextDouble();
                System.out.print("Enter Quantity: ");
                int quantity = scanner.nextInt();
                scanner.nextLine(); // Consume newline
    
                Product product = new Product(id, name, category, price, quantity, branch);
                inventoryManager.addProduct(product);
                inventoryManager.saveInventory(branch);
                System.out.println("Product added successfully!");
            }
            case 2 -> {
                System.out.print("Enter Product ID to remove: ");
                String id = scanner.nextLine();
                if (inventoryManager.removeProduct(id)) {
                    inventoryManager.saveInventory(branch);
                    System.out.println("Product removed successfully!");
                } else {
                    System.out.println("Product not found in inventory.");
                }
                
            }
            case 3 -> {
                System.out.println("Inventory:");
                inventoryManager.getInventory().forEach((id, product) -> {
                    System.out.println("ID: " + id);
                    System.out.println(product);
                    System.out.println("--------------------------");
                });
            }
            default -> System.out.println("Invalid choice. Returning to main menu.");
        }
    }
    

    public static void createNewEmployee(EmployeeManager employeeManager, Scanner scanner) {
        System.out.print("Enter Employee Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Employee ID: ");
        String employeeId = scanner.nextLine(); // Updated variable name for clarity
        System.out.print("Enter Phone Number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Enter Account Number: "); // New field
        String accountNumber = scanner.nextLine();
        System.out.print("Enter Role: ");
        String role = scanner.nextLine();
        System.out.print("Enter Branch: ");
        String branch = scanner.nextLine();
        System.out.print("Enter Employee User Name As It Stored In The System: ");
        String userName = scanner.nextLine();

        // Pass all 7 arguments to the 
        Employee employee = new Employee(name, accountNumber, phoneNumber, accountNumber, branch, employeeId, role, userName);
        employeeManager.addEmployee(employee);
        employeeManager.saveEmployees(branch);
        System.out.println("Employee added successfully!");
    }


    public static void displayEmployees(EmployeeManager employeeManager) {
        System.out.println("Employees:");
        employeeManager.getAllEmployees().forEach((id, employee) -> {
            System.out.println("ID: " + id);
            System.out.println(employee);
            System.out.println("--------------------------");
        });
    }


    public static void manageEmployees(UserCredentialsManager credentialsManager, EmployeeManager employeeManager, String branch, Scanner scanner) {
        System.out.println("\nEmployee Management:");
        System.out.println("1. Remove Employee");
        System.out.println("2. View Employees");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
    
        switch (choice) {            
            case 1 -> {
                System.out.print("Enter Employee Number to remove: ");
                String id = scanner.nextLine(); 
                String employeeUserName = employeeManager.getEmployeeUserName(id);
                if (employeeManager.removeEmployee(id) && credentialsManager.removeCredentials(employeeUserName)) {
                    System.out.println("Employee removed successfully!");
                    employeeManager.saveEmployees(branch);
                    credentialsManager.saveCredentials(branch);
                } else {
                    System.out.println("Employee not found in the system.");
                }
            }
            case 2 -> {
                displayEmployees(employeeManager);
            }

            default -> System.out.println("Invalid choice. Returning to main menu.");
        }
    }

    
    public static void manageCustomers(CustomerManager customerManager, String branch,  Scanner scanner) {
        System.out.println("\nCustomer Management:");
        System.out.println("1. Add Customer");
        System.out.println("2. Remove Customer");
        System.out.println("3. View Customers");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
    
        switch (choice) {
            case 1 -> {
                System.out.print("Enter Customer Name: ");
                String name = scanner.nextLine();
                System.out.print("Enter Customer ID: ");
                String id = scanner.nextLine();
                System.out.print("Enter Phone Number: ");
                String phoneNumber = scanner.nextLine();
                System.out.print("Enter Customer Type (New/Returning/VIP): ");
                String type = scanner.nextLine();
    
                // Add the new customer
                customerManager.addCustomer(new models.Customer(name, id, phoneNumber, type, branch));
                customerManager.saveCustomers(branch);
                System.out.println("Customer added successfully!");
            }
            case 2 -> {
                System.out.print("Enter Customer ID to remove: ");
                String id = scanner.nextLine();
                if (customerManager.removeCustomer(id)) {
                    System.out.println("Customer removed successfully!");
                    customerManager.saveCustomers(branch);
                } else {
                    System.out.println("Customer not found in the system.");
                }
                
            }
            case 3 -> {
                System.out.println("Customers:");
                customerManager.getAllCustomers().forEach((id, customer) -> {
                    System.out.println("ID: " + id);
                    System.out.println(customer);
                    System.out.println("--------------------------");
                });
            }
            default -> System.out.println("Invalid choice. Returning to main menu.");
        }
    }
}


