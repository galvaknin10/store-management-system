package model.log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LogFileHandler {

    private static final String LOG_FILE_PATH = "data/system_log.json";

    // Save logs to the JSON file
    protected static void saveLogsToFile(List<Log> logs) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(LOG_FILE_PATH)) {
            gson.toJson(logs, writer);
        } catch (IOException e) {
            System.err.println("Error occurred while saving logs to the file: " + e.getMessage());
        }
    }

    // Load logs from the JSON file
    protected static List<Log> loadLogsFromFile() {
        File logFile = new File(LOG_FILE_PATH);

        // If the log file doesn't exist, create it with an initial log entry
        if (!logFile.exists()) {
            List<Log> initialLogs = new ArrayList<>();
            initialLogs.add(new Log(LocalDateTime.now().toString(), "Initiate Action", "System log initiated"));
            saveLogsToFile(initialLogs);
            return initialLogs;
        }

        // Load logs from the file
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(logFile)) {
            Type logListType = new TypeToken<List<Log>>() {}.getType();
            return gson.fromJson(reader, logListType);
        } catch (IOException e) {
            System.err.println("Error occurred while loading logs from the file: " + e.getMessage());
            return new ArrayList<>(); // Return an empty list in case of error
        }
    }

    private static void createJsonFile(List<Log> logRepo, String fileName) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(logRepo, writer); // Serialize the log list to JSON
        } catch (IOException e) {
            System.err.println("Error occurred while creating log JSON file: '" + fileName + "': " + e.getMessage());
        }
    }

    public static void createDefaultLogFile() {
        File logFile = new File(LOG_FILE_PATH);
        if (!logFile.exists()) {
            List<Log> initialLogs = new ArrayList<>();
            initialLogs.add(new Log(LocalDateTime.now().toString(), "Initiate Action", "System log initiated"));
            createJsonFile(initialLogs, LOG_FILE_PATH);
        }
    }
}

