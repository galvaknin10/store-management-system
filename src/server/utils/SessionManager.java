package server.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

import shared.ChatMessage;


public class SessionManager {
    private static final ConcurrentHashMap<String, ChatSession> activeChatSessions = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, List<ChatMessage>> chatLogs = new ConcurrentHashMap<>();


    // Get a session
    public static ChatSession getSession(String sessionId) {
        return activeChatSessions.get(sessionId);
    }

    // Add a session
    public static boolean addSession(String sessionId, ChatSession session) {
        if (activeChatSessions.containsKey(sessionId)) {
            return false;
        }
        activeChatSessions.put(sessionId, session);
        return true;
    }

    // Remove a session
    public static boolean removeSession(String sessionId) {
        return activeChatSessions.remove(sessionId) != null;
    }

    public static void trackMessages(String sessionId, ChatMessage message) {
        chatLogs.compute(sessionId, (key, messages) -> {
            if (messages == null) {
                // If no list exists for this sessionId, create a new one
                List<ChatMessage> newMessages = new ArrayList<>();
                newMessages.add(message);
                return newMessages;
            } else {
                // Add the new message to the existing list
                messages.add(message);
                return messages;
            }
        });
    }
    

    public static List<ChatMessage> getSortedChatLog(String sessionId) {
        List<ChatMessage> chatLog = chatLogs.get(sessionId);
        if (chatLog == null) {
            return new ArrayList<>();
        }
        // Create a sorted copy to avoid modifying the original list
        List<ChatMessage> sortedLog = new ArrayList<>(chatLog);
        sortedLog.sort(Comparator.comparing(ChatMessage::getTimestamp));
        return sortedLog;
    }
    
    public static void removeStagedChat(String sessionId) {
        chatLogs.remove(sessionId);
    }

    public static ConcurrentHashMap<String, List<ChatMessage>> getChatLogs() {
        return chatLogs;
    }
}

