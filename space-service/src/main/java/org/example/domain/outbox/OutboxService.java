package org.example.domain.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.entity.CommunicationSatellite;
import org.example.domain.entity.ImagingSatellite;
import org.example.domain.entity.Satellite;
import org.example.domain.entity.enums.SatelliteType;
import org.example.domain.kafka.dto.SatelliteEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxService {

    private static final String SATELLITE_CREATED = "SATELLITE_CREATED";
    private static final String SATELLITE_DELETED = "SATELLITE_DELETED";

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void scheduleCreated(Satellite satellite) {
        SatelliteEvent event = buildEvent(SATELLITE_CREATED, satellite);
        saveOutboxEvent(satellite.getId(), OutboxEventType.CREATED, event);
    }

    @Transactional
    public void scheduleDeleted(Satellite satellite, String constellationName) {
        SatelliteEvent event = SatelliteEvent.builder()
                .eventId(UUID.randomUUID())
                .eventType(SATELLITE_DELETED)
                .satelliteId(satellite.getId())
                .satelliteName(satellite.getName())
                .satelliteType(resolveType(satellite))
                .constellationName(constellationName)
                .timestamp(Instant.now())
                .build();
        saveOutboxEvent(satellite.getId(), OutboxEventType.DELETED, event);
    }

    private void saveOutboxEvent(Long aggregateId, OutboxEventType eventType, SatelliteEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            OutboxEvent outboxEvent = new OutboxEvent(event.getEventId(), aggregateId, eventType, payload);
            outboxEventRepository.save(outboxEvent);
            log.info("Outbox: событие {} для спутника {} сохранено (status=PENDING)", eventType, aggregateId);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Не удалось сериализовать событие в outbox", e);
        }
    }

    private SatelliteEvent buildEvent(String eventType, Satellite satellite) {
        return SatelliteEvent.builder()
                .eventId(UUID.randomUUID())
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
