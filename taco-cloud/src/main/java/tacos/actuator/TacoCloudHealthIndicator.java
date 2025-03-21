package tacos.actuator;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class TacoCloudHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        return ThreadLocalRandom.current().nextBoolean() ? Health.up().build() : Health.down().build();
    }
}
