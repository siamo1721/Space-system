package org.example.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.Satellite;
import org.example.domain.factory.SatelliteFactory;
import org.example.domain.dto.param.SatelliteParam;
import org.example.domain.service.SatelliteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SatelliteServiceImpl implements SatelliteService {
    private final List<SatelliteFactory> factories;

    @Override
    public Satellite createSatellite(SatelliteParam param) {
        SatelliteFactory factory = factories.stream()
                .filter((f) -> f.isSatelliteTypeSupported(param.getType()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Фабрика не найдена c типом " + param.getType()));

        return factory.createSatelliteWithParameter(param);
    }
}
