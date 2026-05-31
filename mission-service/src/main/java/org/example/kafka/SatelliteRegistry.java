package org.example.kafka;

import lombok.extern.slf4j.Slf4j;
import org.example.kafka.dto.SatelliteEvent;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SatelliteRegistry {

    private final Map<String, SatelliteEvent> satellites = new ConcurrentHashMap<>();

    public void register(SatelliteEvent event) {
        satellites.put(event.getSatelliteName(), event);
        log.info("Mission-service: спутник зарегистрирован — {} (группировка: {}, тип: {})",
                event.getSatelliteName(), event.getConstellationName(), event.getSatelliteType());
    }

    public void unregister(String satelliteName) {
        satellites.remove(satelliteName);
        log.info("Mission-service: спутник снят с учёта — {}", satelliteName);
    }

    public boolean isKnown(String satelliteName) {
        return satellites.containsKey(satelliteName);
    }

    public Map<String, SatelliteEvent> getAll() {
        return Collections.unmodifiableMap(satellites);
    }
}
