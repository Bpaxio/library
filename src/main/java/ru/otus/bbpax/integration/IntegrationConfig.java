package ru.otus.bbpax.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;

@Slf4j
@Configuration
@IntegrationComponentScan
public class IntegrationConfig {

    @Bean
    public PublishSubscribeChannel liarDetectedChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    public IntegrationFlow settingLiars() {
        return IntegrationFlows.from("verifyLiarChannel")
                .handle("liarService", "isInveterateLiar")
                .channel("liarDetectedChannel")
                .get();
    }
}
