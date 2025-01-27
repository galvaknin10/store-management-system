package server.utils;

import shared.ChatMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ChatManager {
    private static final Queue<String> waitingQueue = new ConcurrentLinkedQueue<>();
    private static ChatManager instance;
    private final Object queueLock = new Object();
    private final Object activeChatLock = new Object();
    ConcurrentHashMap<String, ClientInfo> connectedClients;

    private ChatManager(ConcurrentHashMap<String, ClientInfo> connectedClients) {
        this.connectedClients = connectedClients;
        // Start background thread to monitor waiting clients
        new Thread(this::monitorQueue).start();
    }

     // Public method to get the single instance of ChatManager
     public static synchronized ChatManager getInstance(ConcurrentHashMap<String, ClientInfo> connectedClients) {
        if (instance == null) {
            instance = new ChatManager(connectedClients);
        }
        return instance;
    }

    // Start a chat
    public synchronized String startChat(String userName) {
        ClientInfo userNameInfo = connectedClients.get(userName);
        String userBranch = userNameInfo.getBranch();
        // Check if any client is available
        for (Map.Entry<String, ClientInfo> entry : connectedClients.entrySet()) {
            String partnetUserName = entry.getKey();
            ClientInfo partnerInfo = entry.getValue();
            String partnerBranch = partnerInfo.getBranch();
            String partnerRole = partnerInfo.getRole();

            if (!partnetUserName.equals(userName) && partnerInfo.isAvailable() && !userBranch.equals(partnerBranch) && !partnerRole.equals("Manager")) {
                return partnetUserName;
            }
        }

        // No clients available, add to waiting queue
        waitingQueue.add(userName);
        return null;
    }

    // Confirm chat session
    public synchronized boolean confirmChat(String sessionId, String userName, String partnerUserName) {
        if (userName == null || partnerUserName == null) {
            return false;
        }
        ChatSession session = new ChatSession(userName, partnerUserName);

        if (SessionManager.addSession(sessionId, session)) {
            connectedClients.get(userName).setAvailable(false);
            connectedClients.get(partnerUserName).setAvailable(false);
        }
        return true;
    }


    public synchronized boolean sendMessage(ChatMessage chatMessage) {
        String partnerUserName = chatMessage.getPartnerUserName();
        ClientInfo partnerInfo = connectedClients.get(partnerUserName);

        try {
            ObjectOutputStream output = partnerInfo.getOutputStream();
            output.writeObject(chatMessage);
            output.flush();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean endChat(String sessionId) {
        ChatSession session = SessionManager.getSession(sessionId);
        synchronized (activeChatLock) {
            if (SessionManager.removeSession(sessionId)) {
                synchronized (connectedClients) {
                    connectedClients.get(session.getUserName()).setAvailable(true);
                    connectedClients.get(session.getPartnerUserName()).setAvailable(true);
                }
                return true;
            }
            return false;
        }
    }
    
    private void monitorQueue() {
        while (true) {
            try {
                Thread.sleep(1000); // Poll every second
                String userName;
    
                synchronized (queueLock) {
                    userName = waitingQueue.peek();
                }
    
                if (userName != null) {
                    for (Map.Entry<String, ClientInfo> entry : connectedClients.entrySet()) {
                        String partnerUserName = entry.getKey();
                        ClientInfo partnerInfo = entry.getValue();
    
                        synchronized (activeChatLock) {
                            if (!partnerUserName.equals(userName) && partnerInfo.isAvailable()) {
                                ChatMessage chatMessage = new ChatMessage(userName, partnerUserName, userName + " is trying to connect with you. Do you want to create a chat session?");
                                sendMessage(chatMessage);
                                waitingQueue.poll();
                                break;
                            }
                        }
                    }
                }
            } catch (InterruptedException e) {
                System.err.println("Queue monitor interrupted: " + e.getMessage());
            }
        }
    }
}
