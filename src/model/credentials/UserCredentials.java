package model.credentials;


public class UserCredentials {
    private String username;    // Username for login
    private String passwordHash; // Hashed password for security
    private String branch;

    public UserCredentials(String username, String passwordHash, String branch) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.branch = branch;
    }

    public String getUserName() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getBranch () {
        return branch;
    }

    @Override
    public String toString() {
        return "Name: " + username + ", Password(hash): " + passwordHash + ", Branch" + branch;
    }
}
