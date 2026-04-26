package org.example.domain.service.impl;

import lombok.AllArgsConstructor;
import org.example.domain.entity.Satellite;
import org.example.domain.entity.SatelliteConstellation;
import org.example.domain.repository.ConstellationRepository;
import org.example.domain.service.ConstellationService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConstellationServiceImpl implements ConstellationService {

    private final ConstellationRepository repository;

    @Override
    public void createAndSaveConstellation(String name) {
        SatelliteConstellation constellation = new SatelliteConstellation(name);
        System.out.println("Создана спутниковая группировка: " + name);
        repository.save(constellation);
    }

    @Override
    public void addSatelliteToConstellation(String constellationName, Satellite satellite) {
        SatelliteConstellation constellation = repository.findByConstellationName(constellationName).orElseThrow();
        constellation.addSatellite(satellite);
        System.out.println("Добавлен спутник " + satellite.getName() +
                " в группировку " + constellationName);
    }

    @Override
    public void executeConstellationMission(String constellationName) {
        SatelliteConstellation constellation = findByNameConstellation(constellationName);
        System.out.println("\n=== ВЫПОЛНЕНИЕ МИССИЙ ДЛЯ ГРУППИРОВКИ: " + constellationName + " ===");
        constellation.executeAllMission();
    }

    @Override
    public void activateAllSatellites(String constellationName) {
        SatelliteConstellation constellation = findByNameConstellation(constellationName);
        for (Satellite s : constellation.getSatellite()) {
            s.activate();
            System.out.println("Спутник " + s.getName() + " активирован");
        }
    }

    @Override
    public SatelliteConstellation findByNameConstellation(String name){
        return repository.findByConstellationName(name).orElseThrow();
    }

    @Override
    public void printAllSatelliteConstellations() {
        System.out.println(repository.findAll());
    }

}
