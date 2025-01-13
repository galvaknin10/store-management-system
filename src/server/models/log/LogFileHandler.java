package server.models.log;

import com.google.gson.reflect.TypeToken;
import server.utils.FileHandler;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LogFileHandler {

    // Save logs to the JSON file for a specific branch
    protected static void saveLogsToFile(List<Log> logs, String branch) {
        String filePath = "data/" + branch + "_logs.json";
        FileHandler.createJsonFile(logs, filePath);
    }

    // Load logs from the JSON file for a specific branch
    protected static List<Log> loadLogsFromFile(String branch) {
        String filePath = "data/" + branch + "_logs.json";
        Type logListType = new TypeToken<List<Log>>() {}.getType();

        List<Log> logs = FileHandler.loadJsonFile(filePath, logListType);
        if (logs == null) {
            System.out.println(branch + " log file not found. Creating a new one with initial entries.");
            logs = createDefaultLogs(branch);
            saveLogsToFile(logs, branch);
        }

        return logs;
    }

    // Ensure that the default logs directory exists and initialize log files for all branches
    public static void createDefaultLogsRepo() {
        FileHandler.ensureDataDirectoryExists();

        // Create Eilat logs file if it doesn't exist
        if (!FileHandler.fileExists("data/EILAT_logs.json")) {
            saveLogsToFile(createDefaultLogs("EILAT"), "EILAT");
        }

        // Create Jerusalem logs file if it doesn't exist
        if (!FileHandler.fileExists("data/JERUSALEM_logs.json")) {
            saveLogsToFile(createDefaultLogs("JERUSALEM"), "JERUSALEM");
        }
    }

    // Create default logs for a specific branch
    private static List<Log> createDefaultLogs(String branch) {
        List<Log> logs = new ArrayList<>();
        String timestamp = java.time.LocalDateTime.now().toString();
        String action = "Initiate Action";
        String details =  "log file initiated";
        Log log = new Log(timestamp, action, details, branch);
        logs.add(log);
        return logs;
    }
}


