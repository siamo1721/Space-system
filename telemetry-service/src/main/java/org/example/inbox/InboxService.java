package org.example.inbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.kafka.SatelliteRegistry;
import org.example.kafka.dto.SatelliteEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InboxService {

    private static final String SATELLITE_CREATED = "SATELLITE_CREATED";
    private static final String SATELLITE_DELETED = "SATELLITE_DELETED";

    private final InboxEventRepository inboxEventRepository;
    private final SatelliteRegistry satelliteRegistry;

    @Transactional
    public void process(SatelliteEvent event) {
        if (event.getEventId() == null) {
            log.warn("Inbox: событие без eventId, пропуск идемпотентной обработки");
            applyBusinessLogic(event);
            return;
        }

        if (inboxEventRepository.existsById(event.getEventId())) {
            log.info("Inbox: дубликат события {} — пропуск (идемпотентность)", event.getEventId());
            return;
        }

        inboxEventRepository.save(new InboxEvent(
                event.getEventId(),
                event.getSatelliteId(),
                event.getEventType()
        ));

        applyBusinessLogic(event);
        log.info("Inbox: событие {} обработано (aggregateId={})", event.getEventId(), event.getSatelliteId());
    }

    private void applyBusinessLogic(SatelliteEvent event) {
        if (SATELLITE_CREATED.equals(event.getEventType())) {
            satelliteRegistry.register(event);
        } else if (SATELLITE_DELETED.equals(event.getEventType())) {
            satelliteRegistry.unregister(event.getSatelliteName());
        } else {
            log.warn("Inbox: неизвестный тип события {}", event.getEventType());
        }
    }
}
