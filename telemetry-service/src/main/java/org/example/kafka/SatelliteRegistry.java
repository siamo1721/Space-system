package org.example.kafka;

import lombok.extern.slf4j.Slf4j;
import org.example.kafka.dto.SatelliteEvent;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SatelliteRegistry {

    private final Set<String> satelliteNames = ConcurrentHashMap.newKeySet();

    public void register(SatelliteEvent event) {
        satelliteNames.add(event.getSatelliteName());
        log.info("Telemetry-service: спутник добавлен в реестр — {} (тип: {})",
                event.getSatelliteName(), event.getSatelliteType());
    }

    public void unregister(String satelliteName) {
        satelliteNames.remove(satelliteName);
        log.info("Telemetry-service: спутник удалён из реестра — {}", satelliteName);
    }

    public Set<String> getSatelliteNames() {
        return Collections.unmodifiableSet(satelliteNames);
    }

    public boolean isEmpty() {
        return satelliteNames.isEmpty();
    }
}
