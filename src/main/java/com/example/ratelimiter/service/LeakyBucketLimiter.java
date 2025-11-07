package com.example.ratelimiter.service;

import com.example.ratelimiter.model.Decision;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LeakyBucketLimiter {

    private static class State {
        int queueSize;
        long lastLeakMs;
        State(int queueSize, long lastLeakMs) {
            this.queueSize = queueSize;
            this.lastLeakMs = lastLeakMs;
        }
    }

    // Keyed by (algorithm + capacity + rate + clientId) to isolate test configs
    private final Map<String, State> states = new ConcurrentHashMap<>();

    public Decision allow(String clientKey, int capacity, double leakPerSec) {
        long now = Instant.now().toEpochMilli();

        State st = states.compute(clientKey, (k, s) -> (s == null) ? new State(0, now) : s);

        synchronized (st) {
            double elapsedSec = (now - st.lastLeakMs) / 1000.0;
            int leaks = (int) Math.floor(elapsedSec * leakPerSec);
            if (leaks > 0) {
                st.queueSize = Math.max(0, st.queueSize - leaks);
                st.lastLeakMs = now;
            }

            boolean allowed;
            String reason;

            if (st.queueSize < capacity) {
                st.queueSize += 1; // enqueue current request
                allowed = true;
                reason = "enqueued";
            } else {
                allowed = false;
                reason = "bucket-full";
            }

            return new Decision(
                    "leaky",
                    clientKey,
                    allowed,
                    reason,
                    -1,
                    st.queueSize,
                    capacity,
                    leakPerSec,
                    now
            );
        }
    }
}
