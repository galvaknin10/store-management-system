package server.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Map;

public class FileHandler {

    // Ensure the existence of the data directory
    public static void ensureDataDirectoryExists() {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            if (dataDir.mkdirs()) {
                System.out.println("Data directory created.");
            } else {
                System.err.println("Failed to create data directory.");
            }
        }
    }

    // Create a JSON file with the given data
    public static <T> void createJsonFile(T data, String filePath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(data, writer);
            System.out.println("File created successfully: " + filePath);
        } catch (IOException e) {
            System.err.println("Error creating JSON file '" + filePath + "': " + e.getMessage());
        }
    }

    // Load data from a JSON file
    public static <T> T loadJsonFile(String filePath, Type type) {
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("File not found: " + filePath);
            return null;
        }

        Gson gson = new Gson();
        try (FileReader reader = new FileReader(file)) {
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            System.err.println("Error reading JSON file '" + filePath + "': " + e.getMessage());
            return null;
        }
    }

    // Check if a file exists
    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }
}

