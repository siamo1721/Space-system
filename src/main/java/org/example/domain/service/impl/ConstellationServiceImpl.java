package org.example.domain.service.impl;

import lombok.AllArgsConstructor;
import org.example.Satellite;
import org.example.SatelliteConstellation;
import org.example.domain.repository.ConstellationRepository;
import org.example.domain.service.ConstellationService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConstellationServiceImpl implements ConstellationService {

    private final ConstellationRepository repository;

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
        SatelliteConstellation constellation = findByNameConstellation(constellationName);
        System.out.println("\n=== ВЫПОЛНЕНИЕ МИССИЙ ДЛЯ ГРУППИРОВКИ: " + constellationName + " ===");
        constellation.executeAllMission();
    }

    public void activateAllSatellites(String constellationName) {
        SatelliteConstellation constellation = findByNameConstellation(constellationName);
        for (Satellite s : constellation.getSatellite()) {
            s.activate();
            System.out.println("Спутник " + s.getName() + " активирован");
        }
    }

    public SatelliteConstellation findByNameConstellation(String name){
        SatelliteConstellation constellation = repository.findByName(name);
        if (constellation == null){
            throw new RuntimeException("Группировка не найдена: " + name);
        }
        return constellation;
    }

    public void printAllSatelliteConstellations() {
        System.out.println(repository.findAll());
    }

}
