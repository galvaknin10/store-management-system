package model.log;

import java.util.List;


public class LogManager {
    private static LogManager instance; // Singleton instance
    private List<Log> logs; // Logs list

    // Private constructor for Singleton
    private LogManager() {
        logs = LogFileHandler.loadLogsFromFile(); // Load logs from file
    }

    // Public method to get the Singleton instance
    public static LogManager getInstance() {
        if (instance == null) {
            instance = new LogManager();
        }
        return instance;
    }

    // Add a new log
    protected void addLog(String action, String details) {
        String timestamp = java.time.LocalDateTime.now().toString();
        Log log = new Log(timestamp, action, details);
        logs.add(log);
    }

    // Retrieve all logs
    public List<Log> getLogs() {
        return logs;
    }

    protected void savelog() {
        LogFileHandler.saveLogsToFile(logs); // Persist all logs
    }
}

