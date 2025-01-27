package client.cli;


import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import client.utils.RequestSender;
import shared.ChatMessage;
import shared.Request;
import shared.Response;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ChatUtils {

    public static void startChat(Scanner scanner, RequestSender sender, String userName) {
        Request request = new Request("START_CHAT", userName);
        Response response = sender.sendRequest(request);

        if (response.isSuccessful()) {
            System.out.println(response.getMessage());
            String partnerUserName = (String) response.getData();

            while (true) {
                System.out.print(partnerUserName + " is available, Do you want to start a chat with him? (yes/no): ");
                String choice = scanner.nextLine();
    
                if ("yes".equalsIgnoreCase(choice)) {
                    runChat(scanner, sender, userName, partnerUserName);
                    break;
                } else if ("no".equalsIgnoreCase(choice)) {
                    System.out.println("Chat declined.");
                    break;
                } else {
                    System.out.println("Invalid input, try again...");
                }
            }
        } else {
            System.out.println(response.getMessage());
        }
    }


    public static void runChat(Scanner scanner, RequestSender sender, String userName, String partnerUserName) {
        String sessionId = ChatMessage.generateSessionId(userName, partnerUserName);

        Request request = new Request("ADD_CHAT_SESSION", new Object[]{sessionId, userName, partnerUserName});
        Response response = sender.sendRequest(request);
    
        if (response.isSuccessful()) {
            //System.out.println(response.getMessage());
        } else {
            System.out.println(response.getMessage());
            return;
        }
    
        System.out.println("\n--- You are now chatting with " + partnerUserName + " ---");
        System.out.println("Type your message below or type 'exit' to end the chat.");
    
        long lastCheckTime = System.currentTimeMillis();
        long lastResponseTime = System.currentTimeMillis();
        long checkInterval = 2000; // Poll every 5 seconds
        long timeout = 60000; // 30 seconds //30000
        System.out.print(userName + " > ");

        boolean partnerTurn = false;
        String message = null;
        boolean check = true;

        while (true) {            

            if (!partnerTurn && check) {
                message = scanner.nextLine();

                if (isPartnerExit(sender, sessionId, partnerUserName)) {
                    break;
                }

                if ("exit".equalsIgnoreCase(message)) { 
                    System.out.println("Exiting chat session...");
                    logChatContent(sender, scanner, sessionId);
                    exitChat(sender, sessionId, userName);
                    

                    System.out.println("You have left the chat with " + partnerUserName + ". Returning to the main menu.");
                    break;
                } else {
                    ChatMessage chatMessage = new ChatMessage(userName, partnerUserName, message);
                    request = new Request("SEND_MESSAGE", new Object[]{sessionId, chatMessage});
                    response = sender.sendRequest(request);

    
                    if (response.isSuccessful()) {
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

            if (System.currentTimeMillis() - lastCheckTime >= checkInterval) {    
                if (isPartnerExit(sender, sessionId, partnerUserName)) {
                    break;
                }
            
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


    public static void logChatContent(RequestSender sender, Scanner scanner, String sessionId) {
        String choise;

        while (true) {
            System.out.println("Do you want to Log the chat content?");
            System.out.print("Type 'yes' to accept or 'no' to decline: ");
            choise = scanner.nextLine();
            if (choise.equalsIgnoreCase("yes")) {
                Request request = new Request("LOG_CHAT", sessionId);
                Response response = sender.sendRequest(request);

                System.out.println(response.getMessage());
                return;
            } else if (choise.equalsIgnoreCase("no")) {
                return;
            } else {
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

    public static boolean isPartnerExit(RequestSender sender, String sessionId, String partnerUserName) {
        Request request = new Request("IS_PARTNER_EXIT", new Object[]{sessionId, partnerUserName});
        Response response = sender.sendRequest(request);

        if (response.isSuccessful()) {
            System.out.println("Your partner left the chat, returning main menu...");
            
            request = new Request("END_CHAT", sessionId);
            response = sender.sendRequest(request);
            return true;
        }
        return false;
    }

    public static void exitChat(RequestSender sender, String sessionId, String userName) {
        Request request = new Request("UPDATE_EXIT", new Object[]{sessionId, userName});
        Response response = sender.sendRequest(request);
    }
}
