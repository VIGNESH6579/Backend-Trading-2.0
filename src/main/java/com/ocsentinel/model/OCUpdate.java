package com.ocsentinel.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Data
public class OCUpdate {
    @Getter @Setter
    private String instrument;
    @Getter @Setter
    private String expiry;
    @Getter @Setter
    private double spot;
    @Getter @Setter
    private double pcr;
    @Getter @Setter
    private double maxPain;
    @Getter @Setter
    private double maxCEStrike;    // Highest CE OI = resistance
    @Getter @Setter
    private double maxPEStrike;    // Highest PE OI = support
    @Getter @Setter
    private double atmIV;
    @Getter @Setter
    private long   totalCEOI;
    @Getter @Setter
    private long   totalPEOI;
    @Getter @Setter
    private List<StrikeRow> strikes;
    @Getter @Setter
    private long   timestamp;
    @Getter @Setter
    private String dataSource;     // "WEBSOCKET_V2" or "REST"
    @Getter @Setter
    private String trend;          // "BULLISH", "BEARISH", "NEUTRAL"
    @Getter @Setter
    private String trendReasoning;

    @Data
    @Getter
    @Setter
    public static class StrikeRow {
        @Getter @Setter
        private double strike;
        @Getter @Setter
        private OIData ce = new OIData();
        @Getter @Setter
        private OIData pe = new OIData();
    }

    @Data
    @Getter
    @Setter
    public static class OIData {
        @Getter @Setter
        private String token;      // Angel One symbol token
        @Getter @Setter
        private long   oi;
        @Getter @Setter
        private long   changeOI;
        @Getter @Setter
        private long   volume;
        @Getter @Setter
        private double iv;
        @Getter @Setter
        private double ltp;
        @Getter @Setter
        private double bid;
        @Getter @Setter
        private double ask;
    }
}
