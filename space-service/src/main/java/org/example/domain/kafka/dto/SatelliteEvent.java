package org.example.domain.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.entity.enums.SatelliteType;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SatelliteEvent {

    private String eventType;
    private Long satelliteId;
    private String satelliteName;
    private SatelliteType satelliteType;
    private String constellationName;
    private Instant timestamp;
}
