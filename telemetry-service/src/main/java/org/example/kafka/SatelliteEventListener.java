package org.example.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.kafka.dto.SatelliteEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SatelliteEventListener {

    private final SatelliteRegistry satelliteRegistry;

    @KafkaListener(topics = KafkaTopics.SATELLITE_CREATED, groupId = "telemetry-service")
    public void onSatelliteCreated(SatelliteEvent event) {
        log.info("Telemetry-service: получено событие создания спутника: {}", event);
        satelliteRegistry.register(event);
    }

    @KafkaListener(topics = KafkaTopics.SATELLITE_DELETED, groupId = "telemetry-service")
    public void onSatelliteDeleted(SatelliteEvent event) {
        log.info("Telemetry-service: получено событие удаления спутника: {}", event);
        satelliteRegistry.unregister(event.getSatelliteName());
    }
}
