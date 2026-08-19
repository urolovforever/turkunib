package com.tiu.turk.authentication.web.security;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

/**
 * Small in-memory sliding-window rate limiter for abuse-prone public endpoints
 * (password-reset and activation-resend requests). Single-instance app, so a
 * shared store is unnecessary.
 */
@Service
public class RateLimitService {
    private static final int MAX_TRACKED_KEYS = 10_000;

    private final Map<String, Deque<Instant>> hits = new ConcurrentHashMap<>();

    /** Returns true if the action is allowed and records the hit. */
    public boolean tryAcquire(String key, int maxRequests, Duration window) {
        if (this.hits.size() > MAX_TRACKED_KEYS) {
            Instant cutoff = Instant.now().minus(window);
            this.hits.entrySet().removeIf(e -> {
                Instant last = e.getValue().peekLast();
                return last == null || last.isBefore(cutoff);
            });
        }
        Deque<Instant> deque = this.hits.computeIfAbsent(key, k -> new ArrayDeque<>());
        synchronized (deque) {
            Instant now = Instant.now();
            Instant cutoff = now.minus(window);
            while (!deque.isEmpty() && deque.peekFirst().isBefore(cutoff)) {
                deque.pollFirst();
            }
            if (deque.size() >= maxRequests) {
                return false;
            }
            deque.addLast(now);
            return true;
        }
    }
}
