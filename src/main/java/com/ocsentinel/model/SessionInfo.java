package com.ocsentinel.model;

public class SessionInfo {
    private String jwtToken;
    private String feedToken;
    private String clientCode;
    private String name;
    private boolean loggedIn;

    // Getters and Setters
    public String getJwtToken() { return jwtToken; }
    public void setJwtToken(String jwtToken) { this.jwtToken = jwtToken; }
    
    public String getFeedToken() { return feedToken; }
    public void setFeedToken(String feedToken) { this.feedToken = feedToken; }
    
    public String getClientCode() { return clientCode; }
    public void setClientCode(String clientCode) { this.clientCode = clientCode; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public boolean isLoggedIn() { return loggedIn; }
    public void setLoggedIn(boolean loggedIn) { this.loggedIn = loggedIn; }
}
