package ru.otus.bbpax.integration;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static ru.otus.bbpax.controller.security.SecurityUtils.getUsername;

@Slf4j
@Service
@AllArgsConstructor
public class LiarService {
    private final LieCounter counter;

    public boolean isInveterateLiar(String username) {
        final String realUsername = getUsername();
        log.info("user: {}, realUser: {}", username, realUsername);
        if (!realUsername.equalsIgnoreCase(username)) {
            log.info("lied at {} time", counter.lieTimes(realUsername));
            counter.lied(realUsername);
        } else {
            log.info("let's trust to {}", realUsername);
            trustTo(realUsername);
            return false;
        }
        return counter.lieTimes(realUsername) > 3;
    }

    public void trustTo(String username) {
        counter.reset(username);
    }
}
