package model.log;

import utils.FileHandlerUtils;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LogFileHandler {

    private static final String LOG_FILE_PATH = "data/system_log.json";

    // Save logs to the JSON file
    protected static void saveLogsToFile(List<Log> logs) {
        FileHandlerUtils.createJsonFile(logs, LOG_FILE_PATH);
    }

    // Load logs from the JSON file
    protected static List<Log> loadLogsFromFile() {
        Type logListType = new TypeToken<List<Log>>() {}.getType();

        List<Log> logs = FileHandlerUtils.loadJsonFile(LOG_FILE_PATH, logListType);
        if (logs == null) {
            System.out.println("Log file not found. Creating a new one with initial entries.");
            logs = createDefaultLogs();
            saveLogsToFile(logs);
        }

        return logs;
    }

    // Create a default log file
    public static void createDefaultLogFile() {
        FileHandlerUtils.ensureDataDirectoryExists();
        if (!FileHandlerUtils.fileExists(LOG_FILE_PATH)) {
            saveLogsToFile(createDefaultLogs());
        }
    }

    // Create default logs
    private static List<Log> createDefaultLogs() {
        List<Log> initialLogs = new ArrayList<>();
        initialLogs.add(new Log(LocalDateTime.now().toString(), "Initiate Action", "System log initiated"));
        return initialLogs;
    }
}

