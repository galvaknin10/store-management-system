package server.utils;

import java.io.ObjectOutputStream;


public class ClientInfo {
    private String username;      
    private String branch;     
    private String role;    
    private boolean isAvailable;   
    private final ObjectOutputStream output;

    public ClientInfo(String username, String branch, String role, ObjectOutputStream output) {
        this.username = username;
        this.branch = branch;
        this.role = role;
        this.output = output;
        this.isAvailable = true; // Default to available
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getRole() {
        return role;
    }

    public ObjectOutputStream getOutputStream() {
        return output;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    @Override
    public String toString() {
        return "ClientInfo{" +
                "username='" + username + '\'' +
                ", branch='" + branch + '\'' +
                ", isAvailable=" + isAvailable +
                '}';
    }
}
