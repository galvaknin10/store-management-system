package model.credentials;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;



public class UserCredentialsManager extends UserCredentialsFileHandler {

    
    private static final Map<String, UserCredentialsManager> instances = new HashMap<>();

    static {
        // Automatically create instances for predefined branches
        initializeBranch("EILAT");
        initializeBranch("JERUSALEM");
    }

    // Use a HashMap to store credentials, with the username as the key
    private Map<String, UserCredentials> credentials;

    // Private constructor to prevent external instantiation
    private UserCredentialsManager(String branch) {
        this.credentials = UserCredentialsFileHandler.loadCredentialsFromFile(branch);
    }

    // Method to initialize instances for a specific branch
    private static void initializeBranch(String branch) {
        UserCredentialsManager instance = new UserCredentialsManager(branch);
        instances.put(branch, instance);
    }

    // Public method to get an instance for a specific branch
    public static UserCredentialsManager getInstance(String branch) {
        if (instances.containsKey(branch)) {
            return instances.get(branch);
        } else {
            System.out.println("Branch not recognized. Please verify the branch name and try again.");
            return null;  // Return null to indicate that the branch is not valid
        }
    }

    public boolean checkUserName(String username) {
        if (credentials.containsKey(username)) {    
            return false;
        }
        return true;
    }
    
    // Add new credentials
    protected void addCredentials(String username, String plainPassword, String branch) {
        String passwordHash = hashPassword(plainPassword);
        credentials.put(username, new UserCredentials(username, passwordHash, branch));
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

    // Remove credentials by username
    protected boolean removeCredentials(String username) {
        return credentials.remove(username) != null;
    }

    // Update a user's password
    public void updatePassword(String username, String newPlainPassword) {
        if (!credentials.containsKey(username)) {
            System.out.println("User not found: " + username);
            return;
        }
        String branch = credentials.get(username).getBranch();
        String newPasswordHash = hashPassword(newPlainPassword);
        credentials.put(username, new UserCredentials(username, newPasswordHash, branch));
        System.out.println("Password updated successfully for user: " + username);
    }

    // Get all credentials
    public Map<String, UserCredentials> getAllCredentials() {
        return new HashMap<>(credentials);
    }

    // Save credentials to file
    protected void saveCredentials(String branch) {
        UserCredentialsFileHandler.saveCredentialsToFile(credentials, branch);
    }

    
    public boolean authenticate(String userName, String password) {
        if (credentials.containsKey(userName)) {
            // Get the UserCredentials object and compare the stored password hash
            String storedPasswordHash = credentials.get(userName).getPasswordHash();
            String providedPasswordHash = hashPassword(password);
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

