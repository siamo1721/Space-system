package org.example.domain;

public record MissionRequest(
        String targetType,
        String constellationName,
        String satelliteName
) {
}
