package model.credentials;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class UserCredentialsFileHandler {

    // Save credentials to a JSON file specific to a branch
    protected static void saveCredentialsToFile(Map<String, UserCredentials> credentials, String branch) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String fileName = "data/" + branch + "_credentials.json"; // Dynamically create the file name based on the branch

        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(credentials, writer);
        } catch (IOException e) {
            System.err.println("Error occurred while trying to append the credentials to the repository.");
        }
    }

    // Load credentials from a JSON file specific to a branch
    protected static Map<String, UserCredentials> loadCredentialsFromFile(String branch) {
        String fileName = "data/" + branch + "_credentials.json"; // Dynamically create the file name based on the branch
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println(branch + " is a brand new branch with no credentials information available yet.");
            Map<String, UserCredentials> credentialsRepo = new HashMap<>();
            createJsonFile(credentialsRepo, fileName);
            return credentialsRepo;
        }

        Gson gson = new Gson();
        try (FileReader reader = new FileReader(file)) {
            Type credentialsMapType = new TypeToken<Map<String, UserCredentials>>() {}.getType();
            return gson.fromJson(reader, credentialsMapType);
        } catch (IOException e) {
            System.err.println("Error loading credentials for branch '" + branch + "': " + e.getMessage());
            return new HashMap<>(); // Return an empty map in case of error
        }
    }


    public static void createDefaultCredentialsRepo() {
        File eilatFile = new File("data/EILAT_credentials.json");
        File jerusalemFile = new File("data/JERUSALEM_credentials.json");

        if (!eilatFile.exists()) {
            Map<String, UserCredentials> eilatCredentials = new HashMap<>();
            eilatCredentials.put("galUser1", new UserCredentials("galUser1", UserCredentialsManager.hashPassword("Strong#99"), "Eilat"));
            eilatCredentials.put("danUser3", new UserCredentials("danUser3", UserCredentialsManager.hashPassword("Qw3rty!@"), "Eilat"));
            createJsonFile(eilatCredentials, "data/EILAT_credentials.json");
        }
    
        if (!jerusalemFile.exists()) {
            Map<String, UserCredentials> jerusalemCredentials = new HashMap<>();
            jerusalemCredentials.put("rioUser2", new UserCredentials("rioUser2", UserCredentialsManager.hashPassword("N0tEasy#"), "JERUSALEM"));
            jerusalemCredentials.put("davidUser4", new UserCredentials("davidUser4", UserCredentialsManager.hashPassword("Go0d#Luck"), "JERUSALEM"));
            createJsonFile(jerusalemCredentials, "data/JERUSALEM_credentials.json");
        }
    }


    // Create a default JSON file with sample credentials for a specific branch
    private static void createJsonFile(Map<String, UserCredentials> credentialsRepo, String fileName) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(credentialsRepo, writer);
        } catch (IOException e) {
            System.err.println("Error occurred while creating specific JSON file: '" + fileName + "': " + e.getMessage());
        }
    }
}


