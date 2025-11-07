package com.example.ratelimiter.model;

public class Decision {
    private String algorithm;
    private String clientId;
    private boolean allowed;
    private String reason;
    private double tokensLeft;     // for token bucket
    private int queueSize;         // for leaky bucket
    private int capacity;
    private double rate;
    private long timestampMs;

    public Decision() {}

    public Decision(String algorithm, String clientId, boolean allowed, String reason,
                    double tokensLeft, int queueSize, int capacity, double rate, long timestampMs) {
        this.algorithm = algorithm;
        this.clientId = clientId;
        this.allowed = allowed;
        this.reason = reason;
        this.tokensLeft = tokensLeft;
        this.queueSize = queueSize;
        this.capacity = capacity;
        this.rate = rate;
        this.timestampMs = timestampMs;
    }

    // getters & setters
    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }
    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    public boolean isAllowed() { return allowed; }
    public void setAllowed(boolean allowed) { this.allowed = allowed; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public double getTokensLeft() { return tokensLeft; }
    public void setTokensLeft(double tokensLeft) { this.tokensLeft = tokensLeft; }
    public int getQueueSize() { return queueSize; }
    public void setQueueSize(int queueSize) { this.queueSize = queueSize; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public double getRate() { return rate; }
    public void setRate(double rate) { this.rate = rate; }
    public long getTimestampMs() { return timestampMs; }
    public void setTimestampMs(long timestampMs) { this.timestampMs = timestampMs; }
}
