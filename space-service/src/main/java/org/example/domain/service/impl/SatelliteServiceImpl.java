package org.example.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.domain.dto.param.SatelliteParam;
import org.example.domain.entity.Satellite;
import org.example.domain.entity.SatelliteConstellation;
import org.example.domain.factory.SatelliteFactory;
import org.example.domain.repository.ConstellationRepository;
import org.example.domain.repository.SatelliteRepository;
import org.example.domain.service.SatelliteService;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SatelliteServiceImpl implements SatelliteService {
    private final List<SatelliteFactory> factories;
    private final SatelliteRepository satelliteRepository;
    private final ConstellationRepository constellationRepository;
    private final CacheManager cacheManager;

    @Override
    public Satellite createSatellite(SatelliteParam param) {
        SatelliteFactory factory = factories.stream()
                .filter((f) -> f.isSatelliteTypeSupported(param.getType()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Фабрика не найдена c типом " + param.getType()));

        return factory.createSatelliteWithParameter(param);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "satellites", key = "'all'"),
            @CacheEvict(value = "satellite", key = "#result.id", condition = "#satellite.id != null")
    })
    public Satellite saveSatellite(Satellite satellite) {
        return satelliteRepository.saveAndFlush(satellite);
    }

    @Override
    @Cacheable(value = "satellite", key = "#id")
    @Transactional(readOnly = true)
    public Satellite getSatelliteById(Long id) {
        return satelliteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Спутник не найден: id=" + id));
    }

    @Override
    @Cacheable(value = "satellites", key = "'all'")
    @Transactional(readOnly = true)
    public List<Satellite> getAllSatellites() {
        return satelliteRepository.findAll();
    }

    @Override
    @Cacheable(value = "satellite", key = "#constellationName + '::' + #satelliteName")
    @Transactional(readOnly = true)
    public Optional<Satellite> findSatelliteInConstellation(String constellationName, String satelliteName) {
        return constellationRepository.findByConstellationName(constellationName)
                .flatMap(constellation -> constellation.getSatellite().stream()
                        .filter(s -> s.getName().equals(satelliteName))
                        .findFirst());
    }

    @Override
    @Transactional(readOnly = true)
    public Satellite findByName(String name) {
        return satelliteRepository.findFirstByNameOrderByIdAsc(name)
                .orElseThrow(() -> new RuntimeException("Спутник не найден: " + name));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return satelliteRepository.existsByName(name);
    }

    @Override
    @CacheEvict(value = "satellites", key = "'all'")
    public void deleteSatellite(String name) {
        Satellite satellite = findByName(name);
        evictSatelliteEntry(satellite);

        SatelliteConstellation constellation = satellite.getConstellation();
        if (constellation != null) {
            constellation.getSatellite().remove(satellite);
        }

        satelliteRepository.delete(satellite);
    }

    private void evictSatelliteEntry(Satellite satellite) {
        var cache = cacheManager.getCache("satellite");
        if (cache == null) {
            return;
        }
        cache.evict(satellite.getId());
        if (satellite.getConstellation() != null) {
            cache.evict(satellite.getConstellation().getConstellationName() + "::" + satellite.getName());
        }
    }
}
