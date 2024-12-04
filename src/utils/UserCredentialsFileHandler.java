package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import models.Product;
import models.UserCredentials;

import java.io.*;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class UserCredentialsFileHandler {

    // Save credentials to a JSON file specific to a branch
    public static void saveCredentialsToFile(Map<String, UserCredentials> credentials, String branch) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String fileName = "data/" + branch + "_credentials.json"; // Dynamically create the file name based on the branch

        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(credentials, writer);
            System.out.println("Credentials successfully appended to the repository.");
        } catch (IOException e) {
            System.err.println("Error occurred while trying to append the credentials to the repository.");
        }
    }

    // Load credentials from a JSON file specific to a branch
    public static Map<String, UserCredentials> loadCredentialsFromFile(String branch) {
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
        File adminsFile = new File("data/ADMINS_credentials.json");

        if(!adminsFile.exists()) {
            Map<String, UserCredentials> adminsCredentials = new HashMap<>();
            adminsCredentials.put("admin1", new UserCredentials("admin1", hashPassword("adminpass1"), null));
            adminsCredentials.put("admin2", new UserCredentials("admin2", hashPassword("adminpass2"), null));
            createJsonFile(adminsCredentials, "data/ADMINS_credentials.json");
        }
    
        if (!eilatFile.exists()) {
            Map<String, UserCredentials> eilatCredentials = new HashMap<>();
            eilatCredentials.put("john_doe", new UserCredentials("john_doe", hashPassword("password123"), "Eilat"));
            eilatCredentials.put("alice", new UserCredentials("alice", hashPassword("mypassword123"), "Eilat"));
            createJsonFile(eilatCredentials, "data/EILAT_credentials.json");
        }
    
        if (!jerusalemFile.exists()) {
            Map<String, UserCredentials> jerusalemCredentials = new HashMap<>();
            jerusalemCredentials.put("bob", new UserCredentials("bob", hashPassword("password1234"), "JERUSALEM"));
            jerusalemCredentials.put("avi", new UserCredentials("avi", hashPassword("mypassword12355"), "JERUSALEM"));
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

    // Hash a password using SHA-256
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder hashString = new StringBuilder();
            for (byte b : hashBytes) {
                hashString.append(String.format("%02x", b));
            }
            return hashString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password: " + e.getMessage());
        }
    }
}


