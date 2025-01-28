package client.cli;

import java.util.Scanner;
import client.utils.RequestSender;
import shared.ChatMessage;
import shared.Request;
import shared.Response;


public class ChatUtils {

    public static void startChat(Scanner scanner, RequestSender sender, String userName) {
        // Create a request to start the chat
        Request request = new Request("START_CHAT", userName);
        Response response = sender.sendRequest(request);
    
        // Check if the response is successful
        if (response.isSuccessful()) {
            System.out.println(response.getMessage());
            String partnerUserName = (String) response.getData();
    
            // Loop to ask the user if they want to start the chat
            while (true) {
                System.out.print(partnerUserName + " is available. Do you want to start a chat with them? (yes/no): ");
                String choice = scanner.nextLine();
    
                if ("yes".equalsIgnoreCase(choice)) {
                    runChat(scanner, sender, userName, partnerUserName);
                    break;
                } 
                else if ("no".equalsIgnoreCase(choice)) {
                    System.out.println("Chat declined. See you next time!");
                    break;
                } 
                else {
                    System.out.println("Invalid input, please type 'yes' or 'no'...");
                }
            }
        } else {
            // Display error message if the request fails
            System.out.println(response.getMessage());
        }
    }
    
    public static void runChat(Scanner scanner, RequestSender sender, String userName, String partnerUserName) {
        // Generate a unique session ID for this chat
        String sessionId = ChatMessage.generateSessionId(userName, partnerUserName);
    
        Request request = new Request("ADD_CHAT_SESSION", new Object[]{sessionId, userName, partnerUserName});
        Response response = sender.sendRequest(request);
    
        if (response.isSuccessful()) {
            System.out.println(response.getMessage());
        } else {
            System.out.println(response.getMessage());
            return;
        }
    
        System.out.println("\n───────────────────────────── ");
        System.out.println("You are now chatting with " + partnerUserName);
        System.out.println("────────────────────────────────");
        System.out.println("Type your message below or type 'exit' to end the chat.");
    
        long lastCheckTime = System.currentTimeMillis();
        long lastResponseTime = System.currentTimeMillis();
        long checkInterval = 2000; // Poll every 2 seconds
        long timeout = 60000; // Timeout if no response after 60 seconds
        System.out.print(userName + " > ");
    
        boolean partnerTurn = false;
        String message = null;
        boolean check = true;
    
        while (true) {
    
            // If it's the user's turn, they can send a message
            if (!partnerTurn && check) {
                message = scanner.nextLine();
    
                // If the partner has exited, break the loop
                if (isPartnerExit(sender, sessionId, partnerUserName)) {
                    break;
                }
    
                // Exit the chat if the user types 'exit'
                if ("exit".equalsIgnoreCase(message)) {
                    System.out.println("Exiting chat session...");
                    logChatContent(sender, scanner, sessionId);
                    exitChat(sender, sessionId, userName);
    
                    System.out.println("You have left the chat with " + partnerUserName + ". Returning to the main menu.");
                    break;
                } else {
                    // Send the message to the partner
                    ChatMessage chatMessage = new ChatMessage(userName, partnerUserName, message);
                    request = new Request("SEND_MESSAGE", new Object[]{sessionId, chatMessage});
                    response = sender.sendRequest(request);
    
                    if (response.isSuccessful()) {
                        // Update the turn and make the partner appear online
                        request = new Request("UPDATE_TURN", new Object[]{sessionId, userName, partnerUserName});
                        response = sender.sendRequest(request);
    
                        request = new Request("MAKE_PARTNER_ONLINE", new Object[]{sessionId, userName});
                        response = sender.sendRequest(request);
    
                        lastResponseTime = System.currentTimeMillis();
                    } else {
                        System.err.println("Error: " + response.getMessage());
                    }
                }
                check = false;
            }
    
            // Periodic check to see if it's the partner's turn
            if (System.currentTimeMillis() - lastCheckTime >= checkInterval) {
                // If the partner has exited, break the loop
                if (isPartnerExit(sender, sessionId, partnerUserName)) {
                    break;
                }
    
                // Check if it's the partner's turn
                request = new Request("IS_PARTNER_TURN", new Object[]{sessionId, partnerUserName});
                response = sender.sendRequest(request);
    
                if (response.isSuccessful()) {
                    partnerTurn = true;
                } else {
                    partnerTurn = false;
                }
    
                lastCheckTime = System.currentTimeMillis();
                check = true;
            }
    
            // Timeout check if the partner hasn't responded in the allotted time
            if (partnerTurn && (System.currentTimeMillis() - lastResponseTime > timeout)) {
                System.out.println("No response from " + partnerUserName + ". Returning to the main menu...");
                exitChat(sender, sessionId, userName);
                break;
            }
        }
    }
    
    
    public static boolean isPartnerOnline(RequestSender sender, String sessionId, String partnerUserName) {
        Request request = new Request("IS_PARTNER_ONLINE", new Object[]{sessionId, partnerUserName});
        Response response = sender.sendRequest(request);
        if (response.isSuccessful()) {
            return true;
        }
        return false;
    }

    private static void logChatContent(RequestSender sender, Scanner scanner, String sessionId) {
        String choise;
    
        while (true) {
            // Ask if the user wants to log the chat content
            System.out.println("\n─────────────────────────────────");
            System.out.println("Do you want to log the chat content?");
            System.out.println("────────────────────────────────────");
            System.out.print("Type 'yes' to accept or 'no' to decline: ");
            choise = scanner.nextLine();
            
            // If yes, send a request to log the chat and display the response
            if (choise.equalsIgnoreCase("yes")) {
                Request request = new Request("LOG_CHAT", sessionId);
                Response response = sender.sendRequest(request);
    
                System.out.println(response.getMessage());
                return;
            } 
            // If no, simply return and do not log the chat
            else if (choise.equalsIgnoreCase("no")) {
                System.out.println("Chat log declined.");
                return;
            } 
            // Handle invalid input
            else {
                System.out.println("Invalid input, try again...");
            }
        }
    }
      
    public static boolean isManagerInterrupt(RequestSender sender, String userName) {
        Request request = new Request("CHECK_IF_MANAGER", userName);
        Response response = sender.sendRequest(request);
        
        if (response.isSuccessful()) {
            return true;
        }
        return false;
    }

    public  static boolean isPartnerExit(RequestSender sender, String sessionId, String partnerUserName) {
        Request request = new Request("IS_PARTNER_EXIT", new Object[]{sessionId, partnerUserName});
        Response response = sender.sendRequest(request);
    
        if (response.isSuccessful()) {
            System.out.println("Your partner has left the chat, returning to the main menu...");
            
            // End the chat session
            request = new Request("END_CHAT", sessionId);
            response = sender.sendRequest(request);
            
            if (response.isSuccessful()) {
                System.out.println(response.getMessage());
                return true;
            } else {
                System.out.println(response.getMessage());
            }
        }
        return false;
    }
    
    public static void exitChat(RequestSender sender, String sessionId, String userName) {
        // Request to update the exit status for the chat session
        Request request = new Request("UPDATE_EXIT", new Object[]{sessionId, userName});
        Response response = sender.sendRequest(request);
    
        if (response.isSuccessful()) {
            System.out.println(response.getMessage());
        } else {
            System.out.println(response.getMessage());
        }
    }
}
