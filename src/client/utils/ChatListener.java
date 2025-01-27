package client.utils;

import shared.ChatMessage;
import shared.Request;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import client.cli.ChatUtils;
import client.cli.ScreensUtils;
import server.utils.ChatSession;
import server.utils.SessionManager;

public class ChatListener implements Runnable {
    private final BlockingQueue<ChatMessage> chatQueue;
    private volatile boolean running = true;
    private final BlockingQueue<String> inputQueue;
    private RequestSender sender;
    private Scanner scanner;

    public ChatListener(BlockingQueue<ChatMessage> chatQueue, BlockingQueue<String> inputQueue, RequestSender sender, Scanner scanner) {
        this.chatQueue = chatQueue;
        this.inputQueue = inputQueue;
        this.sender = sender;
        this.scanner = scanner;
    }

    @Override
    public void run() {
        try {
            while (running) {
                ChatMessage chatMessage = chatQueue.take(); // Retrieve the next message from the queue

                String sessionid = ChatMessage.generateSessionId(chatMessage.getUserName(), chatMessage.getPartnerUserName());

                if (ChatUtils.isPartnerOnline(sender, sessionid, chatMessage.getPartnerUserName())) {
                    // Handle active session
                    System.out.println(chatMessage.getUserName() + " > " + chatMessage.getMessage());
                    System.out.print(chatMessage.getPartnerUserName() + " > ");
                } else {
 
                    if (ChatUtils.isManagerInterrupt(sender, chatMessage.getUserName())) {
                        System.out.println("\nNOTICE: ADMIN OPEN A CHAT WITH YOU.  You can view it anytime in the Messages section from the main menu");
                        System.out.println("If you currently in a live session, you can continue. (for exit, type 'exit' when its your turn to send a message.)");
                    } else {
                        System.out.println("\n\n[You've received a new message. You can view it anytime in the Messages section from the main menu]");
                        System.out.print("Continue from last action: ");
                    }

                    String action = null;

                    if (chatMessage.isStartChatRequest()) { 
                        action = "start-chat";
                    } else {
                        action = "continue-chat";
                    }

                    // Notify main input queue for deferred handling in the main menu
                    inputQueue.put(action + ":" + chatMessage.getUserName() + ":" + chatMessage.getMessage());
                }
            }
        } catch (InterruptedException e) {
            if (running) {
                System.err.println("Error in ChatListener: " + e.getMessage());
            }
        }
    }


    public void stop() {
        running = false;
    }
}


