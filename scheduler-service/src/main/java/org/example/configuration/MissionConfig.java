package org.example.configuration;

public record MissionConfig(
        String targetType,
        String constellationName,
        String satelliteName,
        String cron
) {}
