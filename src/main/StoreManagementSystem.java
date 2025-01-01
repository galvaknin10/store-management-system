package main;

import model.credentials.*;
import model.customer.*;
import model.employee.*;
import model.inventory.*;
import model.report.*;
import model.log.*;
import view.*;

import java.util.Scanner;


public class StoreManagementSystem {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize repositories
        CustomerFileHandler.createDefaultCustomersRepo();
        EmployeeFileHandler.createDefaultEmployeesRepo();
        InventoryFileHandler.createDefaultInventoryRepo();
        CredentialsFileHandler.createDefaultCredentialsRepo();
        ReportFileHandler.createDefaultReportsRepo();
        LogFileHandler.createDefaultLogFile();

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
            if (role.equals("Manager")) {
                boolean userDecison = HandleUserInteractions.ManagerScreen(scanner, branch); // Return true if user selects "Go Back"
                if (userDecison) {
                    continue;
                } else {
                    break;
                }
            } else if (role.equals("Employee")) {
                boolean userDecison = HandleUserInteractions.EmployeeScreen(scanner, branch);
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


    
    
    




