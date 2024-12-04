package main;

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

import client.HandleUserInteractions;
import utils.CustomerFileHandler;
import utils.EmployeeFileHandler;
import utils.InventoryFileHandler;
import utils.UserCredentialsFileHandler;

import java.util.List;
import java.util.Map;
import java.util.HashMap;


public class StoreManagementSystem {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize repositories
        CustomerFileHandler.createDefaultCustomersRepo();
        EmployeeFileHandler.createDefaultEmployeesRepo();
        InventoryFileHandler.createDefaultInventoryRepo();
        UserCredentialsFileHandler.createDefaultCredentialsRepo();

        boolean running = true;

        while (running) {
            // Login Workflow
            String[] loginResult = HandleUserInteractions.userLogin(scanner);
            if (loginResult == null) {
                System.out.println("Exiting the system. Goodbye!");
                break; // Exit the program
            }

            String role = loginResult[0];
            String branch = loginResult[1];

            // Redirect to the appropriate screen
            if (role.equals("Admin")) {
                boolean userDecison = HandleUserInteractions.adminScreen(scanner); // Return true if user selects "Go Back"
                if (userDecison) {
                    continue;
                } else {
                    break;
                }

            } else if (role.equals("Branch_User")) {
                boolean userDecison = HandleUserInteractions.userScreen(scanner, branch);
                if (userDecison) {
                    continue;
                } else {
                    break;
                }
                
            } else {
                System.out.println("Unrecognized role. Exiting the system.");
                break;
            }
        }

        scanner.close();
    }
}


    
    
    




