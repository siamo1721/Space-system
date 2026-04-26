package org.example.domain.factory;

import org.example.domain.entity.Satellite;
import org.example.domain.entity.enums.SatelliteType;
import org.example.domain.dto.param.SatelliteParam;

public interface SatelliteFactory {
    Satellite createSatelliteWithParameter(SatelliteParam param);
    boolean isSatelliteTypeSupported(SatelliteType type);

}
