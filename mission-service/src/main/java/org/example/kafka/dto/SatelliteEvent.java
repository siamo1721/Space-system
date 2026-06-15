package org.example.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SatelliteEvent {

    private UUID eventId;
    private String eventType;
    private Long satelliteId;
    private String satelliteName;
    private String satelliteType;
    private String constellationName;
    private Instant timestamp;
}
