package com.ocsentinel.model;

public class AnalysisResult {
    private String verdict;      // BUY CE, BUY PE, or NO TRADE
    private double entry;
    private double stopLoss;
    private double target;
    private String reasoning;
    private long   dataTimestamp; // Timestamp of the OC data used
    private long   timestamp;     // Signal generation timestamp

    public AnalysisResult() {
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getVerdict() { return verdict; }
    public void setVerdict(String verdict) { this.verdict = verdict; }
    
    public double getEntry() { return entry; }
    public void setEntry(double entry) { this.entry = entry; }
    
    public double getStopLoss() { return stopLoss; }
    public void setStopLoss(double stopLoss) { this.stopLoss = stopLoss; }
    
    public double getTarget() { return target; }
    public void setTarget(double target) { this.target = target; }
    
    public String getReasoning() { return reasoning; }
    public void setReasoning(String reasoning) { this.reasoning = reasoning; }
    
    public long getDataTimestamp() { return dataTimestamp; }
    public void setDataTimestamp(long dataTimestamp) { this.dataTimestamp = dataTimestamp; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
