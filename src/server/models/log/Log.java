package server.models.log;

public class Log {
    private String timestamp;
    private String action;
    private String details;
    private String branch;

    public Log(String timestamp, String action, String details, String branch) {
        this.timestamp = timestamp;
        this.action = action;
        this.details = details;
        this.branch = branch;
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
        return "[" + timestamp + "] " + action + " - " + details + "|" + branch + "|";
    }
}

