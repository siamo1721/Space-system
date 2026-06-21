package org.example.domain.service.impl;

import lombok.AllArgsConstructor;
import org.example.domain.entity.Satellite;
import org.example.domain.entity.SatelliteConstellation;
import org.example.domain.repository.ConstellationRepository;
import org.example.domain.service.ConstellationService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ConstellationServiceImpl implements ConstellationService {

    private final ConstellationRepository repository;

    @Override
    public void createAndSaveConstellation(String name) {
        if (repository.findByConstellationName(name).isPresent()) {
            System.out.println("Группировка уже существует: " + name);
            return;
        }
        SatelliteConstellation constellation = new SatelliteConstellation(name);
        System.out.println("Создана спутниковая группировка: " + name);
        repository.save(constellation);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "constellation", key = "#constellationName"),
            @CacheEvict(value = "satellites", key = "'all'")
    })
    public void addSatelliteToConstellation(String constellationName, Satellite satellite) {
        SatelliteConstellation constellation = repository.findByConstellationName(constellationName).orElseThrow();
        constellation.addSatellite(satellite);
        System.out.println("Добавлен спутник " + satellite.getName() +
                " в группировку " + constellationName);
    }

    @Override
    public void executeConstellationMission(String constellationName) {
        SatelliteConstellation constellation = repository.findByConstellationName(constellationName).orElseThrow();
        System.out.println("\n=== ВЫПОЛНЕНИЕ МИССИЙ ДЛЯ ГРУППИРОВКИ: " + constellationName + " ===");
        constellation.executeAllMission();
    }

    @Override
    public void activateAllSatellites(String constellationName) {
        SatelliteConstellation constellation = repository.findByConstellationName(constellationName).orElseThrow();
        for (Satellite s : constellation.getSatellite()) {
            s.activate();
            System.out.println("Спутник " + s.getName() + " активирован");
        }
    }

    @Override
    @Cacheable(value = "constellation", key = "#name")
    @Transactional(readOnly = true)
    public SatelliteConstellation getConstellationByName(String name) {
        return repository.findByConstellationName(name)
                .orElseThrow(() -> new RuntimeException("Группировка не найдена: " + name));
    }

    @Override
    public SatelliteConstellation findByNameConstellation(String name) {
        return getConstellationByName(name);
    }

    @Override
    public void printAllSatelliteConstellations() {
        System.out.println(repository.findAll());
    }

}
