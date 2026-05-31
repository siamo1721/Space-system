package org.example.domain.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.entity.CommunicationSatellite;
import org.example.domain.entity.ImagingSatellite;
import org.example.domain.entity.Satellite;
import org.example.domain.entity.enums.SatelliteType;
import org.example.domain.kafka.dto.SatelliteEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class SatelliteEventPublisher {

    private static final String CREATED = "SATELLITE_CREATED";
    private static final String DELETED = "SATELLITE_DELETED";

    private final KafkaTemplate<String, SatelliteEvent> kafkaTemplate;

    public void publishCreated(Satellite satellite) {
        SatelliteEvent event = buildEvent(CREATED, satellite);
        kafkaTemplate.send(KafkaTopics.SATELLITE_CREATED, satellite.getName(), event);
        log.info("Kafka: опубликовано событие создания спутника {}", satellite.getName());
    }

    public void publishDeleted(Satellite satellite, String constellationName) {
        SatelliteEvent event = SatelliteEvent.builder()
                .eventType(DELETED)
                .satelliteId(satellite.getId())
                .satelliteName(satellite.getName())
                .satelliteType(resolveType(satellite))
                .constellationName(constellationName)
                .timestamp(Instant.now())
                .build();

        kafkaTemplate.send(KafkaTopics.SATELLITE_DELETED, satellite.getName(), event);
        log.info("Kafka: опубликовано событие удаления спутника {}", satellite.getName());
    }

    private SatelliteEvent buildEvent(String eventType, Satellite satellite) {
        return SatelliteEvent.builder()
                .eventType(eventType)
                .satelliteId(satellite.getId())
                .satelliteName(satellite.getName())
                .satelliteType(resolveType(satellite))
                .constellationName(satellite.getConstellation().getConstellationName())
                .timestamp(Instant.now())
                .build();
    }

    private SatelliteType resolveType(Satellite satellite) {
        if (satellite instanceof CommunicationSatellite) {
            return SatelliteType.COMMUNICATION;
        }
        if (satellite instanceof ImagingSatellite) {
            return SatelliteType.IMAGE;
        }
        throw new IllegalStateException("Неизвестный тип спутника: " + satellite.getClass().getSimpleName());
    }
}
