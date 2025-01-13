package server.models.credentials;


public class Credentials {
    private String username;   
    private String passwordHash; 
    private String branch;

    public Credentials(String username, String passwordHash, String branch) {
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
