package org.example.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.client.SpaceOperationClient;
import org.example.configuration.MissionConfig;
import org.example.configuration.SpaceCenterProperties;
import org.example.domain.MissionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConfiguredMissionScheduler {
    private static final Logger log = LoggerFactory.getLogger(ConfiguredMissionScheduler.class);

    private final SpaceCenterProperties properties;
    private final TaskScheduler taskScheduler;
    private final SpaceOperationClient spaceOperationClient;

    @PostConstruct
    public void scheduleMissions() {
        List<MissionConfig> missions = properties.missions();
        if (missions == null || missions.isEmpty()) {
            log.warn("No missions found in configuration.");
            return;
        }

        for (MissionConfig config : missions) {
            validateConfig(config);
            MissionRequest request = new MissionRequest(
                    config.targetType(),
                    config.constellationName(),
                    config.satelliteName()
            );

            Runnable task = () -> {
                log.info("Starting mission: {}", request);
                spaceOperationClient.executeMission(request);
            };

            log.info("Scheduling mission {} with cron: {}", request, config.cron());
            taskScheduler.schedule(task, new CronTrigger(config.cron()));
        }
    }

    private void validateConfig(MissionConfig config) {
        if ("SINGLE_SATELLITE".equals(config.targetType()) && (config.satelliteName() == null || config.satelliteName().isEmpty())) {
            log.error("Validation failed: satelliteName must be provided for SINGLE_SATELLITE missions. Constellation: {}", config.constellationName());
            throw new IllegalArgumentException("satelliteName is required for SINGLE_SATELLITE");
        }
        if ("CONSTELLATION".equals(config.targetType()) && config.satelliteName() != null && !config.satelliteName().isEmpty()) {
            log.warn("Validation warning: satelliteName should not be provided for CONSTELLATION missions. Constellation: {}", config.constellationName());
        }
    }
}
