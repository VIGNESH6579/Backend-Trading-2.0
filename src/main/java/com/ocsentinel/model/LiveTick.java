package com.ocsentinel.model;

public class LiveTick {
    private String token;
    private int    exchangeType;   // 1=NSE, 2=NFO
    private double ltp;            // Last Traded Price
    private long   volume;
    private double open;
    private double high;
    private double low;
    private double close;
    private long   oi;             // Open Interest (KEY for options)
    private long   oiDayHigh;
    private long   oiDayLow;
    private double impliedVolatility;
    private long   timestamp;

    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public int getExchangeType() { return exchangeType; }
    public void setExchangeType(int exchangeType) { this.exchangeType = exchangeType; }
    
    public double getLtp() { return ltp; }
    public void setLtp(double ltp) { this.ltp = ltp; }
    
    public long getVolume() { return volume; }
    public void setVolume(long volume) { this.volume = volume; }
    
    public double getOpen() { return open; }
    public void setOpen(double open) { this.open = open; }
    
    public double getHigh() { return high; }
    public void setHigh(double high) { this.high = high; }
    
    public double getLow() { return low; }
    public void setLow(double low) { this.low = low; }
    
    public double getClose() { return close; }
    public void setClose(double close) { this.close = close; }
    
    public long getOi() { return oi; }
    public void setOi(long oi) { this.oi = oi; }
    
    public long getOiDayHigh() { return oiDayHigh; }
    public void setOiDayHigh(long oiDayHigh) { this.oiDayHigh = oiDayHigh; }
    
    public long getOiDayLow() { return oiDayLow; }
    public void setOiDayLow(long oiDayLow) { this.oiDayLow = oiDayLow; }
    
    public double getImpliedVolatility() { return impliedVolatility; }
    public void setImpliedVolatility(double impliedVolatility) { this.impliedVolatility = impliedVolatility; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
