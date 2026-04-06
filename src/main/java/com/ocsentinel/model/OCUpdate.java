package com.ocsentinel.model;

import java.util.List;

public class OCUpdate {
    private String instrument;
    private String expiry;
    private double spot;
    private double pcr;
    private double maxPain;
    private double maxCEStrike;    // Highest CE OI = resistance
    private double maxPEStrike;    // Highest PE OI = support
    private double atmIV;
    private long   totalCEOI;
    private long   totalPEOI;
    private List<StrikeRow> strikes;
    private long   timestamp;
    private String dataSource;     // "WEBSOCKET_V2" or "REST"
    private String trend;          // "BULLISH", "BEARISH", "NEUTRAL"
    private String trendReasoning;

    // Getters and Setters
    public String getInstrument() { return instrument; }
    public void setInstrument(String instrument) { this.instrument = instrument; }
    
    public String getExpiry() { return expiry; }
    public void setExpiry(String expiry) { this.expiry = expiry; }
    
    public double getSpot() { return spot; }
    public void setSpot(double spot) { this.spot = spot; }
    
    public double getPcr() { return pcr; }
    public void setPcr(double pcr) { this.pcr = pcr; }
    
    public double getMaxPain() { return maxPain; }
    public void setMaxPain(double maxPain) { this.maxPain = maxPain; }
    
    public double getMaxCEStrike() { return maxCEStrike; }
    public void setMaxCEStrike(double maxCEStrike) { this.maxCEStrike = maxCEStrike; }
    
    public double getMaxPEStrike() { return maxPEStrike; }
    public void setMaxPEStrike(double maxPEStrike) { this.maxPEStrike = maxPEStrike; }
    
    public double getAtmIV() { return atmIV; }
    public void setAtmIV(double atmIV) { this.atmIV = atmIV; }
    
    public long getTotalCEOI() { return totalCEOI; }
    public void setTotalCEOI(long totalCEOI) { this.totalCEOI = totalCEOI; }
    
    public long getTotalPEOI() { return totalPEOI; }
    public void setTotalPEOI(long totalPEOI) { this.totalPEOI = totalPEOI; }
    
    public List<StrikeRow> getStrikes() { return strikes; }
    public void setStrikes(List<StrikeRow> strikes) { this.strikes = strikes; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    
    public String getDataSource() { return dataSource; }
    public void setDataSource(String dataSource) { this.dataSource = dataSource; }
    
    public String getTrend() { return trend; }
    public void setTrend(String trend) { this.trend = trend; }
    
    public String getTrendReasoning() { return trendReasoning; }
    public void setTrendReasoning(String trendReasoning) { this.trendReasoning = trendReasoning; }

    public static class StrikeRow {
        private double strike;
        private OIData ce = new OIData();
        private OIData pe = new OIData();

        // Getters and Setters
        public double getStrike() { return strike; }
        public void setStrike(double strike) { this.strike = strike; }
        
        public OIData getCe() { return ce; }
        public void setCe(OIData ce) { this.ce = ce; }
        
        public OIData getPe() { return pe; }
        public void setPe(OIData pe) { this.pe = pe; }
    }

    public static class OIData {
        private String token;      // Angel One symbol token
        private long   oi;
        private long   changeOI;
        private long   volume;
        private double iv;
        private double ltp;
        private double bid;
        private double ask;

        // Getters and Setters
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        
        public long getOi() { return oi; }
        public void setOi(long oi) { this.oi = oi; }
        
        public long getChangeOI() { return changeOI; }
        public void setChangeOI(long changeOI) { this.changeOI = changeOI; }
        
        public long getVolume() { return volume; }
        public void setVolume(long volume) { this.volume = volume; }
        
        public double getIv() { return iv; }
        public void setIv(double iv) { this.iv = iv; }
        
        public double getLtp() { return ltp; }
        public void setLtp(double ltp) { this.ltp = ltp; }
        
        public double getBid() { return bid; }
        public void setBid(double bid) { this.bid = bid; }
        
        public double getAsk() { return ask; }
        public void setAsk(double ask) { this.ask = ask; }
    }
}
