package main;

import model.credentials.*;
import model.customer.*;
import model.employee.*;
import model.inventory.*;
import model.report.*;
import view.*;

import java.util.Scanner;


public class StoreManagementSystem {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize repositories
        CustomerFileHandler.createDefaultCustomersRepo();
        EmployeeFileHandler.createDefaultEmployeesRepo();
        InventoryFileHandler.createDefaultInventoryRepo();
        UserCredentialsFileHandler.createDefaultCredentialsRepo();
        ReportFileHandler.createDefaultReportsRepo();

        boolean running = true;

        while (running) {
            // Login Workflow
            String[] loginResult = HandleUserInteractions.userLogin(scanner);
            if (loginResult == null) {
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
            }
        }
        scanner.close();
    }
}


    
    
    




