package com.tiu.turk.authentication.web.security;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

/**
 * In-memory brute-force protection for the student login (the app runs as a single
 * instance, so no shared store is needed). After MAX_FAILURES consecutive failed
 * sign-ins for the same email+IP pair within the counting window, the pair is
 * locked out for LOCK_DURATION. A successful sign-in clears the counter.
 */
@Service
public class LoginAttemptService {
    private static final int MAX_FAILURES = 5;
    private static final Duration LOCK_DURATION = Duration.ofHours(1);
    private static final Duration COUNT_WINDOW = Duration.ofHours(1);
    private static final int MAX_TRACKED_KEYS = 10_000;

    private final Map<String, Entry> attempts = new ConcurrentHashMap<>();

    public boolean isLocked(String username, String ip) {
        Entry entry = this.attempts.get(key(username, ip));
        if (entry == null) {
            return false;
        }
        if (entry.lockedUntil != null && Instant.now().isBefore(entry.lockedUntil)) {
            return true;
        }
        if (entry.isStale()) {
            this.attempts.remove(key(username, ip));
        }
        return false;
    }

    public void recordFailure(String username, String ip) {
        pruneIfOversized();
        this.attempts.compute(key(username, ip), (k, entry) -> {
            Instant now = Instant.now();
            if (entry == null || entry.isStale()) {
                entry = new Entry();
                entry.firstFailure = now;
            }
            entry.failures++;
            if (entry.failures >= MAX_FAILURES) {
                entry.lockedUntil = now.plus(LOCK_DURATION);
            }
            return entry;
        });
    }

    public void recordSuccess(String username, String ip) {
        this.attempts.remove(key(username, ip));
    }

    /** Client IP, honouring the X-Forwarded-For header set by the nginx proxy. */
    public static String clientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    public static String normalizeUsername(String username) {
        return username == null ? "" : username.trim().toLowerCase();
    }

    private String key(String username, String ip) {
        return normalizeUsername(username) + "|" + (ip == null ? "" : ip);
    }

    private void pruneIfOversized() {
        if (this.attempts.size() > MAX_TRACKED_KEYS) {
            this.attempts.entrySet().removeIf(e -> e.getValue().isStale());
        }
    }

    private static final class Entry {
        int failures;
        Instant firstFailure;
        Instant lockedUntil;

        boolean isStale() {
            Instant now = Instant.now();
            boolean lockOver = this.lockedUntil == null || now.isAfter(this.lockedUntil);
            boolean windowOver = this.firstFailure == null || now.isAfter(this.firstFailure.plus(COUNT_WINDOW));
            return lockOver && windowOver;
        }
    }
}
