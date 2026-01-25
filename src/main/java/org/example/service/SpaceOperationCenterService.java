package org.example.service;

import org.example.Satellite;
import org.example.SatelliteConstellation;
import org.example.repository.ConstellationRepository;
import org.springframework.stereotype.Service;

@Service
public class SpaceOperationCenterService {

    private final ConstellationRepository repository;

    public SpaceOperationCenterService(ConstellationRepository repository) {
        this.repository = repository;
    }

    public void createAndSaveConstellation(String name) {
        SatelliteConstellation constellation = new SatelliteConstellation(name);
        System.out.println("Создана спутниковая группировка: " + name);
        repository.save(constellation);
    }

    public void addSatelliteToConstellation(String constellationName, Satellite satellite) {
        SatelliteConstellation constellation = repository.findByName(constellationName);
        constellation.addSatellite(satellite);
        System.out.println("Добавлен спутник " + satellite.getName() +
                " в группировку " + constellationName);
    }

    public void executeConstellationMission(String constellationName) {
        SatelliteConstellation constellation = repository.findByName(constellationName);
        System.out.println("\n=== ВЫПОЛНЕНИЕ МИССИЙ ДЛЯ ГРУППИРОВКИ: " + constellationName + " ===");
        constellation.executeAllMission();
    }

    public void activateAllSatellites(String constellationName) {
        SatelliteConstellation constellation = repository.findByName(constellationName);
        for (Satellite s : constellation.getSatellite()) {
            s.activate();
            System.out.println("Спутник " + s.getName() + " активирован");
        }
    }

    public void showConstellationStatus(String constellationName) {
        SatelliteConstellation constellation = repository.findByName(constellationName);
        for (Satellite s : constellation.getSatellite()) {
            System.out.println("У " + s.getName() + " статус " + s.getState());
        }
    }

    public void printAllSatelliteConstellations() {
        System.out.println(repository.findAll());
    }

}
