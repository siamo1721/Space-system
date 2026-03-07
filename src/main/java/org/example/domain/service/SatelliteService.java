package org.example.domain.service;

import org.example.Satellite;
import org.example.domain.dto.param.SatelliteParam;

public interface SatelliteService {
    Satellite createSatellite(SatelliteParam param);

}
