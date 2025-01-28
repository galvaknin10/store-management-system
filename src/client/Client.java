package client;

import shared.*;
import client.utils.*;
import client.cli.ChatUtils;
import client.cli.LoginScreen;
import client.cli.ManagerScreen;
import client.cli.UserScreen;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        MessageDispatcher dispatcher = null;
        Thread dispatcherThread = null;
        ChatListener chatListener = null;
        Thread chatThread = null;
        Socket socket = null;
    
        try (Scanner scanner = new Scanner(System.in)) {
            socket = new Socket(HOST, PORT);
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
    
            // Shared queues for communication
            BlockingQueue<Response> responseQueue = new LinkedBlockingQueue<>();
            BlockingQueue<ChatMessage> chatQueue = new LinkedBlockingQueue<>();
            BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    
            // Start dispatcher thread
            dispatcher = new MessageDispatcher(input, responseQueue, chatQueue);
            dispatcherThread = new Thread(dispatcher);
            dispatcherThread.start();
    
            // Initialize RequestSender
            RequestSender sender = new RequestSender(output, responseQueue);
    
            // Start chat listener thread
            chatListener = new ChatListener(chatQueue, inputQueue, sender);
            chatThread = new Thread(chatListener);
            chatThread.start();
    
            String[] loginResult = LoginScreen.userLogin(scanner, sender);
            if (loginResult == null) {
                return;
            }
    
            String role = loginResult[0];
            String branch = loginResult[1];
            String userName = loginResult[2];
    
            boolean running = true;
            while (running) {
                if (!inputQueue.isEmpty()) {
                    String signal = inputQueue.take();
                    // Extract partner info from signal
                    String[] parts = signal.split(":");
                    String label = parts[0];
                    String partnerUserName = parts[1];
                    String message = parts[2];
    
                    System.out.println("New notification: ");
    
                    if (label.equals("start-chat")) {
                        System.out.println(message);
                    } else if (signal.startsWith("continue-chat")) {
                        System.out.println("New message from " + partnerUserName + ": " + message);
                        System.out.println("Do you want to enter a chat session?");
                    }
                    while (true) {
                        System.out.print("Type 'yes' to accept or 'no' to decline: ");
                        String choice = scanner.nextLine();
                        if ("yes".equalsIgnoreCase(choice)) {
                            ChatUtils.runChat(scanner, sender, userName, partnerUserName);
                            break;
                        } else if ("no".equalsIgnoreCase(choice)) {
                            System.out.println("You declined the chat request.");
                            if (ChatUtils.isPartnerExit(sender, ChatMessage.generateSessionId(partnerUserName, userName), partnerUserName)) {
                                System.out.println("Your partner already left...");
                            } else {
                                ChatUtils.exitChat(sender, ChatMessage.generateSessionId(partnerUserName, userName), userName);
                            }
                            break;
                        } else {
                            System.out.println("Invalid input, try again...");
                        }
                    }
                }
    
                if (role.equals("Manager")) {
                    boolean continueSession = ManagerScreen.managerScreen(scanner, sender, branch, userName);
                    if (!continueSession) {
                        return;
                    }
                } else if (role.equals("Employee")) {
                    boolean continueSession = UserScreen.userScreen(scanner, sender, branch, userName, inputQueue);
                    if (!continueSession) {
                        return;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            try {
                if (dispatcher != null) dispatcher.stop();
                if (chatListener != null) chatListener.stop();
                if (dispatcherThread != null) dispatcherThread.join();
                if (chatThread != null) chatThread.join();
                if (socket != null && !socket.isClosed()) socket.close();
            } catch (Exception e) {
                System.err.println("Error during cleanup: " + e.getMessage());
            }
        }
    }    
}







