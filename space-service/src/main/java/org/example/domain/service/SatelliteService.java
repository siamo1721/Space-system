package org.example.domain.service;

import org.example.domain.dto.param.SatelliteParam;
import org.example.domain.entity.Satellite;

import java.util.List;
import java.util.Optional;

public interface SatelliteService {
    Satellite createSatellite(SatelliteParam param);

    Satellite saveSatellite(Satellite satellite);

    Satellite getSatelliteById(Long id);

    List<Satellite> getAllSatellites();

    Optional<Satellite> findSatelliteInConstellation(String constellationName, String satelliteName);

    Satellite findByName(String name);

    boolean existsByName(String name);

    void deleteSatellite(String name);
}
