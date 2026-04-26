package org.example.domain.service;

import org.example.domain.entity.Satellite;
import org.example.domain.entity.SatelliteConstellation;

public interface ConstellationService {
    void createAndSaveConstellation(String name);
    void addSatelliteToConstellation(String constellationName, Satellite satellite);
    void executeConstellationMission(String constellationName);
    void activateAllSatellites(String constellationName);
    void printAllSatelliteConstellations();
    SatelliteConstellation findByNameConstellation(String name);
}
