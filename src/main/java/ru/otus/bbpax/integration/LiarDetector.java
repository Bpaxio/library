package ru.otus.bbpax.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface LiarDetector {

    @Gateway(requestChannel = "verifyLiarChannel", replyChannel = "liarDetectedChannel")
    boolean punishIfLiar(String username);
}
