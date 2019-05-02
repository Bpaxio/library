package ru.otus.bbpax.configuration.metrics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Random;

@Slf4j
@Component("slave")
public class HealthCheck implements HealthIndicator {

    private final int maxLoad;
    private final Random random;

    public HealthCheck(@Value("${request.max}") Integer maxLoad) {
        this.maxLoad = maxLoad;
        random = new Random();
    }

    @Override
    public Health health() {
        int currentLoad = random.nextInt(maxLoad);
        double health = 100 * (1. - (double) currentLoad / maxLoad);
        if (health > 50) {
            return Health.up().withDetail("message", "OK =)")
                    .withDetail("HEALTH(%)", health).build();
        } else if (health > 30) {
            return Health.up().withDetail("message", "Not bad.")
                    .withDetail("HEALTH(%)", health).build();
        } else if (health > 7) {
            return Health.up().withDetail("message", "Master, I need HELP!")
                    .withDetail("HEALTH(%)", health).build();
        } else {
            return Health.down().withDetail("message", "I died young, you have to replace me")
                    .withDetail("HEALTH(%)", health).build();
        }
    }
}
