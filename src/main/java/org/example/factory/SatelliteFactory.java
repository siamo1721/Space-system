package org.example.factory;

import org.example.Satellite;
import org.example.entity.SatelliteType;
import org.example.param.SatelliteParam;

public interface SatelliteFactory {
    Satellite createSatelliteWithParameter(SatelliteParam param);
    boolean isSatelliteTypeSupported(SatelliteType type);

}
