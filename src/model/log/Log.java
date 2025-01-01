package model.log;

public class Log {
    private String timestamp;
    private String action;
    private String details;

    public Log(String timestamp, String action, String details) {
        this.timestamp = timestamp;
        this.action = action;
        this.details = details;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getAction() {
        return action;
    }

    public String getDetails() {
        return details;
    }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + action + " - " + details;
    }
}

