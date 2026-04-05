package org.example.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;

@ConfigurationProperties(prefix = "app.space-center-service")
public record SpaceCenterProperties(
        String url,
        List<MissionProperties> missions
) {
    public record MissionProperties(
            String targetType,
            String constellationName,
            String satelliteName,
            String cron
    ) {}
}

