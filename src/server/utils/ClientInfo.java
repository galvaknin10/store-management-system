package server.utils;

import java.net.Socket;

public class ClientInfo {
    private String username;       // The username of the client
    private String branch;         // The branch the client belongs to
    private Socket socket;         // The client's socket for communication
    private boolean isAvailable;   // Whether the client is available for chat

    public ClientInfo(String username, String branch, Socket socket) {
        this.username = username;
        this.branch = branch;
        this.socket = socket;
        this.isAvailable = true; // Default to available
    }

    // Getters and Setters
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

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
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
