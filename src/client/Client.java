package client;

import client.cli.LoginScreen;
import client.cli.ManagerScreen;
import client.cli.UserScreen;
import java.util.Scanner;


public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 12345; 

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            RequestSender sender = new RequestSender(HOST, PORT);

            // Login Workflow
            String[] loginResult = LoginScreen.userLogin(scanner, sender);
            if (loginResult == null) {
                sender.closeConnection();
                return; // Exit the program
            }

            String role = loginResult[0];
            String branch = loginResult[1];
            String userName = loginResult[2];


            // Post-login actions
            if (role.equals("Manager")) {
                boolean continueSession = ManagerScreen.managerScreen(scanner, sender, branch);
                if (!continueSession) {
                    sender.closeConnection();
                    return;
                }
            } else if (role.equals("Employee")) {
                boolean continueSession = UserScreen.EmployeeScreen(scanner, sender, branch);
                if (!continueSession) {
                    sender.closeConnection();
                    return;
                }
            }

            sender.closeConnection();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}





