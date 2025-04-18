package server.models.credentials;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class CredentialsManager {

    private static final Map<String, CredentialsManager> instances = new HashMap<>();

    static {
        // Automatically create instances for predefined branches
        initializeBranch("EILAT");
        initializeBranch("JERUSALEM");
    }

    // Use a HashMap to store credentials, with the username as the key
    private Map<String, Credentials> credentials;

    // Private constructor to prevent external instantiation
    private CredentialsManager(String branch) {
        this.credentials = new ConcurrentHashMap<>(CredentialsFileHandler.loadCredentialsFromFile(branch));
    }

    // Method to initialize instances for a specific branch
    private static void initializeBranch(String branch) {
        CredentialsManager instance = new CredentialsManager(branch);
        instances.put(branch, instance);
    }

    // Public method to get an instance for a specific branch
    public static CredentialsManager getInstance(String branch) {
        if (instances.containsKey(branch)) {
            return instances.get(branch);
        } else {
            System.out.println("Branch not recognized. Please verify the branch name and try again.");
            return null;  // Return null to indicate that the branch is not valid
        }
    }
    
    // Add new credentials
    protected synchronized boolean addCredentials(String username, String userPassword, String branch) {
        if (username == null || username.isEmpty() ||userPassword == null || userPassword.isEmpty()) {
            return false;
        }
        if ((credentials.containsKey(username)) || !isValidPassword(userPassword)) {
            return false;
        }
        String passwordHash = CredentialsFileHandler.hashPassword(userPassword);
        credentials.put(username, new Credentials(username, passwordHash, branch));
        CredentialsFileHandler.saveCredentialsToFile(credentials, branch);
        return true;
    }

    // Remove credentials by username
    protected synchronized boolean removeCredentials(String username, String branch) {
        if (credentials.remove(username) != null) {
            CredentialsFileHandler.saveCredentialsToFile(credentials, branch);
            return true;
        }
        return false;
    }

    // Get all credentials
    public Map<String, Credentials> getAllCredentials() {
        return new HashMap<>(credentials);
    }
    
    public boolean authenticate(String userName, String password) {
        if (credentials.containsKey(userName)) {
            // Get the UserCredentials object and compare the stored password hash
            String storedPasswordHash = credentials.get(userName).getPasswordHash();
            String providedPasswordHash = CredentialsFileHandler.hashPassword(password);
            return storedPasswordHash.equals(providedPasswordHash);
        }
        return false;
    }
    
    public boolean isValidPassword(String password) {
        // Define password requirements
        if (password.length() < 8) {
            System.out.println("Password must be at least 8 characters long.");
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            System.out.println("Password must contain at least one uppercase letter.");
            return false;
        }
        if (!password.matches(".*[a-z].*")) {
            System.out.println("Password must contain at least one lowercase letter.");
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            System.out.println("Password must contain at least one digit.");
            return false;
        }
        if (!password.matches(".*[@#$%^&+=!].*")) {
            System.out.println("Password must contain at least one special character (e.g., @, #, $, etc.).");
            return false;
        }
        if (password.contains(" ")) {
            System.out.println("Password must not contain spaces.");
            return false;
        }
        return true;
    }
}

