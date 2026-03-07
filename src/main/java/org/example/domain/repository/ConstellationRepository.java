package org.example.domain.repository;

import org.example.SatelliteConstellation;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ConstellationRepository {
    private final Map<String, SatelliteConstellation> constellations = new HashMap<>();

    public void save(SatelliteConstellation constellation) {
        constellations.put(constellation.getConstellationName(), constellation);
        System.out.println("Сохранена группировка: " + constellation.getConstellationName());
    }

    public SatelliteConstellation findByName(String name) {
        return constellations.get(name);
    }

    public Map<String, SatelliteConstellation> findAll() {
        return constellations;
    }

    public void delete(String name) {
        constellations.remove(name);
    }
}
