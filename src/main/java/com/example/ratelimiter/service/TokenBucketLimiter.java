package com.example.ratelimiter.service;

import com.example.ratelimiter.model.Decision;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TokenBucketLimiter {

    private static class State {
        double tokens;
        long lastRefillMs;
        State(double tokens, long lastRefillMs) {
            this.tokens = tokens;
            this.lastRefillMs = lastRefillMs;
        }
    }

    // Keyed by (algorithm + capacity + rate + clientId) to isolate test configs
    private final Map<String, State> states = new ConcurrentHashMap<>();

    public Decision allow(String clientKey, int capacity, double refillPerSec) {
        long now = Instant.now().toEpochMilli();

        State st = states.compute(clientKey, (k, s) -> {
            if (s == null) return new State(capacity, now);  // start full
            double elapsedSec = (now - s.lastRefillMs) / 1000.0;
            if (elapsedSec > 0) {
                s.tokens = Math.min(capacity, s.tokens + elapsedSec * refillPerSec);
                s.lastRefillMs = now;
            }
            return s;
        });

        boolean allowed;
        String reason;

        synchronized (st) {
            if (st.tokens >= 1.0) {
                st.tokens -= 1.0;
                allowed = true;
                reason = "token-assigned";
            } else {
                allowed = false;
                reason = "bucket-empty";
            }
        }

        return new Decision(
                "token",
                clientKey,
                allowed,
                reason,
                st.tokens,
                -1,
                capacity,
                refillPerSec,
                now
        );
    }
}
