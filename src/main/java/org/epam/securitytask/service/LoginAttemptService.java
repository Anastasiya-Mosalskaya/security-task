package org.epam.securitytask.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Service
public class LoginAttemptService {

    public static final int MAX_ATTEMPT = 3;
    private final LoadingCache<String, Integer> attemptsCache;

    public LoginAttemptService() {
        attemptsCache = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build(new CacheLoader<>() {
            @Override
            public Integer load(String email) {
                return 0;
            }
        });
    }

    public void loginFailed(String email) {
        int attempts;
        try {
            attempts = attemptsCache.get(email);
        } catch (ExecutionException e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(email, attempts);
    }

    public boolean isBlocked(String email) {
        try {
            return attemptsCache.get(email) >= MAX_ATTEMPT;
        } catch (final ExecutionException e) {
            return false;
        }
    }

    public List<String> getBlockedUsers() {
        return attemptsCache.asMap().keySet().stream().filter(this::isBlocked).toList();
    }
}
