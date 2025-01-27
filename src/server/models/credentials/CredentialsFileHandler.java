package server.models.credentials;

import com.google.gson.reflect.TypeToken;
import server.utils.FileHandler;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


public class CredentialsFileHandler {

    // Save credentials to JSON file
    protected synchronized static void saveCredentialsToFile(Map<String, Credentials> credentials, String branch) {
        String fileName = "data/" + branch + "_credentials.json";
        FileHandler.createJsonFile(credentials, fileName);
    }

    // Load credentials from JSON file
    protected static Map<String, Credentials> loadCredentialsFromFile(String branch) {
        String fileName = "data/" + branch + "_credentials.json";
        Type mapType = new TypeToken<Map<String, Credentials>>() {}.getType();
        Map<String, Credentials> credentials = FileHandler.loadJsonFile(fileName, mapType);

        if (credentials == null) {
            System.out.println(branch + " is a brand new branch with no credentials information available yet.");
            credentials = new HashMap<>();
            saveCredentialsToFile(credentials, branch);
        }

        return credentials;
    }


        // Hash a password using SHA-256
    protected static String hashPassword(String password) {
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

    public static void createDefaultCredentialsRepo() {
        FileHandler.ensureDataDirectoryExists();

        // Create Eilat credentials if the file doesn't exist
        if (!FileHandler.fileExists("data/EILAT_credentials.json")) {
            createDefaultRepo("data/EILAT_credentials.json", Map.of(
                "galUser1", new Credentials("galUser1", hashPassword("Strong#99"), "Eilat"),
                "danUser3", new Credentials("danUser3", hashPassword("Qw3rty!@"), "Eilat"),
                "adiUser9", new Credentials("adiUser9", hashPassword("Ab3@12345"), "Eilat")
            ));
        }
    
        // Create Jerusalem credentials if the file doesn't exist
        if (!FileHandler.fileExists("data/JERUSALEM_credentials.json")) {
            createDefaultRepo("data/JERUSALEM_credentials.json", Map.of(
                "rioUser2", new Credentials("rioUser2", hashPassword("N0tEasy#"), "JERUSALEM"),
                "davidUser4", new Credentials("davidUser4", hashPassword("Go0d#Luck"), "JERUSALEM"),
                "alonUser7", new Credentials("alonUser7", hashPassword("123456Ab&"), "JERUSALEM")
            ));
        }
    }
    

    private static void createDefaultRepo(String fileName, Map<String, Credentials> defaultData) {
        FileHandler.createJsonFile(defaultData, fileName);
    }
}


