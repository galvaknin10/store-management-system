package shared;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ChatMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String userName;
    private final String partnerUserName;
    private final String message;
    private final LocalDateTime timestamp;

    public ChatMessage(String userName, String partnerUserName, String message) {
        this.userName = userName;
        this.partnerUserName = partnerUserName;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public String getUserName() {
        return userName;
    }

    public String getPartnerUserName() {
        return partnerUserName;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStartChatRequest() {
        return message != null && message.contains("create a chat session");
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public static String generateSessionId(String userName1, String userName2) {
        // Sort the usernames lexicographically
        if (userName1.compareTo(userName2) < 0) {
            return userName1 + "-" + userName2; // A-B
        } else {
            return userName2 + "-" + userName1; // B-A
        }
    }

    @Override
    public String toString() {
        return "From: " + userName + ", To: " + partnerUserName + ", Message: " + message;
    }
}
