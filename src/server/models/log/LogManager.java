package server.models.log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class LogManager {

    private static final Map<String, LogManager> instances = new HashMap<>();

    static {
        // Automatically create instances for predefined branches
        initializeBranch("EILAT");
        initializeBranch("JERUSALEM");
    }

    private List<Log> logs;

    private LogManager(String branch) {
        this.logs = new CopyOnWriteArrayList<>(LogFileHandler.loadLogsFromFile(branch));
    }

    private static void initializeBranch(String branch) {
        LogManager instance = new LogManager(branch);
        instances.put(branch, instance);
    }

    // Public method to get an instance for a specific branch
    public static LogManager getInstance(String branch) {
        if (instances.containsKey(branch)) {
            return instances.get(branch);
        } else {
            System.out.println("Branch not recognized. Please verify the branch name and try again.");
            return null; // Return null to indicate that the branch is not valid
        }
    }

    // Add a log
    protected void addLog(String action, String details, String branch) {
        String timestamp = java.time.LocalDateTime.now().toString();
        Log log = new Log(timestamp, action, details, branch);
        logs.add(log);
        LogFileHandler.saveLogsToFile(logs, branch);
    }

    // Retrieve all logs
    public List<Log> getLogs() {
        return logs;
    }
}


