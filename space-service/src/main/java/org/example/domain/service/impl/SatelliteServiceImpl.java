package org.example.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.domain.entity.Satellite;
import org.example.domain.entity.SatelliteConstellation;
import org.example.domain.factory.SatelliteFactory;
import org.example.domain.dto.param.SatelliteParam;
import org.example.domain.repository.SatelliteRepository;
import org.example.domain.service.SatelliteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SatelliteServiceImpl implements SatelliteService {
    private final List<SatelliteFactory> factories;
    private final SatelliteRepository satelliteRepository;

    @Override
    public Satellite createSatellite(SatelliteParam param) {
        SatelliteFactory factory = factories.stream()
                .filter((f) -> f.isSatelliteTypeSupported(param.getType()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Фабрика не найдена c типом " + param.getType()));

        return factory.createSatelliteWithParameter(param);
    }

    @Override
    public Satellite saveSatellite(Satellite satellite) {
        return satelliteRepository.saveAndFlush(satellite);
    }

    @Override
    public Satellite findByName(String name) {
        return satelliteRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Спутник не найден: " + name));
    }

    @Override
    public void deleteSatellite(String name) {
        Satellite satellite = findByName(name);

        SatelliteConstellation constellation = satellite.getConstellation();
        if (constellation != null) {
            constellation.getSatellite().remove(satellite);
        }

        satelliteRepository.delete(satellite);
    }
}
