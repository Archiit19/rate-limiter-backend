package com.example.ratelimiter.controller;

import com.example.ratelimiter.model.Decision;
import com.example.ratelimiter.service.LeakyBucketLimiter;
import com.example.ratelimiter.service.TokenBucketLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rate")
public class RateLimiterController {

    // Singletons with in-memory state
    private final TokenBucketLimiter tokenLimiter = new TokenBucketLimiter();
    private final LeakyBucketLimiter leakyLimiter = new LeakyBucketLimiter();

    /**
     * Test endpoint:
     * GET /rate/test?algorithm=token|leaky&clientId=u1&capacity=5&rate=1
     *
     * - algorithm: token (default) or leaky
     * - clientId:  identify caller; state kept per client+config
     * - capacity:  bucket capacity (default 5)
     * - rate:      tokens/sec (token) or requests/sec leak (leaky) (default 1)
     */
    @GetMapping("/test")
    public ResponseEntity<Decision> test(
            @RequestParam(name = "algorithm", defaultValue = "token") String algorithm,
            @RequestParam(name = "clientId", defaultValue = "client1") String clientId,
            @RequestParam(name = "capacity", defaultValue = "5") int capacity,
            @RequestParam(name = "rate", defaultValue = "1") double rate)
     {
        // Distinct key per algorithm+config+client so tests don't mix
        String key = algorithm + ":" + capacity + ":" + rate + ":" + clientId;

        Decision d;
        if ("leaky".equalsIgnoreCase(algorithm)) {
            d = leakyLimiter.allow(key, capacity, rate);
        } else {
            d = tokenLimiter.allow(key, capacity, rate);
        }

        return new ResponseEntity<>(d, d.isAllowed() ? HttpStatus.OK : HttpStatus.TOO_MANY_REQUESTS);
    }

    /**
     * Optional: reset per-client state (helpful for demos)
     */
    @GetMapping("/help")
    public String help() {
        return "Use /rate/test?algorithm=token|leaky&clientId=u1&capacity=5&rate=1";
    }
}

