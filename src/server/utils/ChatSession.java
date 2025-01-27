package server.utils;

import java.io.Serializable;


public class ChatSession implements Serializable {
    private final String userName;
    private final String partnerUserName;
    private volatile boolean cliantATurn;
    private volatile boolean cliantBTurn;
    private volatile boolean clientAExit;
    private volatile boolean clientBExit;
    private volatile boolean clientAOnline;
    private volatile boolean clientBOnline;


    public ChatSession (String userName, String partnerUserName) {
        this.userName = userName;
        this.partnerUserName = partnerUserName;
        cliantATurn = true;
        cliantBTurn = false;
        clientAExit = false;
        clientBExit = false;
        clientAOnline = true;
        clientBOnline = false;
    }

    public synchronized boolean isClientATurn() {
        return cliantATurn;
    }

    public synchronized void setClientATurn(boolean bool) {
        this.cliantATurn = bool;
    }

    public synchronized boolean isClientBTurn() {
        return cliantBTurn;
    }

    public synchronized void setClientBTurn(boolean bool) {
        this.cliantBTurn = bool;
    }

    public synchronized boolean isClientAExit() {
        return clientAExit;
    }

    public synchronized void setClientAExit(boolean bool) {
        this.clientAExit = bool;
    }

    public synchronized boolean isClientBExit() {
        return clientBExit;
    }

    public synchronized void setClientBExit(boolean bool) {
        this.clientBExit = bool;
    }

    public synchronized boolean isClientAOnline() {
        return clientAOnline;
    }

    public synchronized void setClientAOnline(boolean bool) {
        this.clientAOnline = bool;
    }

    public synchronized boolean isClientBOnline() {
        return clientBOnline;
    }

    public synchronized void setClientBOnline(boolean bool) {
        this.clientBOnline = bool;
    }

    public String getUserName() {
        return userName;
    }

    public String getPartnerUserName() {
        return partnerUserName;
    }
}
