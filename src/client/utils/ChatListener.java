package client.utils;

import shared.ChatMessage;
import java.util.concurrent.BlockingQueue;
import client.cli.ChatUtils;


public class ChatListener implements Runnable {
    private final BlockingQueue<ChatMessage> chatQueue;
    private volatile boolean running = true;
    private final BlockingQueue<String> inputQueue;
    private RequestSender sender;
  
    public ChatListener(BlockingQueue<ChatMessage> chatQueue, BlockingQueue<String> inputQueue, RequestSender sender) {
        this.chatQueue = chatQueue;
        this.inputQueue = inputQueue;
        this.sender = sender;
    }

    @Override
    public void run() {
        try {
            while (running) {
                ChatMessage chatMessage = chatQueue.take(); // Retrieve the next message from the queue
    
                String sessionid = ChatMessage.generateSessionId(chatMessage.getUserName(), chatMessage.getPartnerUserName());
                String action;
                
                if (chatMessage.isStartChatRequest()) {
                    System.out.println("\n\nYou've received a new message. You can view it anytime in the Messages section from the main menu.");
                    System.out.print("Continue from last action: ");
                    action = "start-chat";
    
                    // Notify main input queue for deferred handling
                    inputQueue.put(action + ":" + chatMessage.getUserName() + ":" + chatMessage.getMessage());
                } else if (ChatUtils.isPartnerOnline(sender, sessionid, chatMessage.getPartnerUserName())) {
                    // Handle active session when partner is online
                    System.out.println(chatMessage.getUserName() + " > " + chatMessage.getMessage());
                    System.out.print(chatMessage.getPartnerUserName() + " > ");
                } else {
                    if (ChatUtils.isManagerInterrupt(sender, chatMessage.getUserName())) {
                        System.out.println("\nNOTICE: ADMIN OPENED A CHAT WITH YOU. You can view it anytime in the Messages section from the main menu.");
                        System.out.println("If you're currently in a live session, you can continue. (For exit, type 'exit' when it's your turn to send a message.)");
                    } else {
                        System.out.println("\n\nYou've received a new message. You can view it anytime in the Messages section from the main menu.");
                        System.out.print("Continue from last action: ");
                    }
                    action = "continue-chat";
                    
                    // Notify main input queue for deferred handling
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


