package org.example.domain.service;

import org.example.domain.dto.param.SatelliteParam;
import org.example.domain.entity.Satellite;

public interface SatelliteService {
    Satellite createSatellite(SatelliteParam param);

    Satellite saveSatellite(Satellite satellite);

    Satellite findByName(String name);

    boolean existsByName(String name);

    void deleteSatellite(String name);
}
