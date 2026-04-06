package com.ocsentinel.model;

public class StatusMessage {
    private String status;   // CONNECTED, DISCONNECTED, ERROR, RECONNECTING
    private String message;
    private long   timestamp;

    public StatusMessage(String status, String message) {
        this.status    = status;
        this.message   = message;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
