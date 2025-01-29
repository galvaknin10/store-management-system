package client.cli;

import java.util.Scanner;
import client.utils.RequestSender;
import shared.Request;
import shared.Response;


public class ScreensUtils {
    
    public static String validateBranch(Scanner scanner, RequestSender sender) {
        while (true) {
            String userBranch = getNonEmptyInput(scanner, "Enter the specific Branch (Eilat or Jerusalem): ");
    
            // Request to validate the branch
            Request request = new Request("VALIDATE_BRANCH", userBranch);
            Response response = sender.sendRequest(request);
            if (response.isSuccessful()) {
                return (String) response.getData();
            } else {
                System.out.println("Error: " + response.getMessage());
                while (true) {
                    String userChoice = getNonEmptyInput(scanner, "Type 'GO BACK' to return to the previous screen or 'RETRY' to try again: ");
                    
                    if (userChoice.equalsIgnoreCase("GO BACK")) {
                        return null; 
                    } else if (userChoice.equalsIgnoreCase("RETRY")) {
                        break; // Retry branch validation
                    } else {
                        System.out.println("Invalid input. Please type 'GO BACK' or 'RETRY'.");
                    }
                }
            }
        }
    }
    
    public static String getNonEmptyInput(Scanner scanner, String prompt) {
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
        
    public static void logOut(RequestSender sender, String userName) {
        // Request to disconnect the user
        Request disconnectRequest = new Request("DISCONNECT", userName);
        Response response = sender.sendRequest(disconnectRequest);
    
        if (response.isSuccessful()) {
            System.out.println("You have successfully logged out.");
        } else {
            System.out.println("Error logging out: " + response.getMessage());
        }
    }
}


