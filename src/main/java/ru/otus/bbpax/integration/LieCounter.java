package ru.otus.bbpax.integration;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LieCounter {
    private final Map<String, Integer> count;

    public LieCounter() {
        count = new ConcurrentHashMap<>();
    }

    public boolean lied(String username) {
        count.putIfAbsent(username, 0);
        count.put(username, count.get(username) + 1);
        return true;
    }

    public Integer lieTimes(String username) {
        count.putIfAbsent(username, 0);
        return count.get(username);
    }

    public void reset(String username) {
        count.remove(username);
    }
}
