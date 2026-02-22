package org.example.factory;

import org.example.ImagingSatellite;
import org.example.Satellite;

public class ImagingSatelliteFactory extends SatelliteFactory {
    @Override
    public Satellite createSatelliteWithParam(String name, double batteryLevel, double extraParameter) {
        return new ImagingSatellite(name, batteryLevel, extraParameter);
    }

    @Override
    public Satellite createSatellite(String name, double batteryLevel) {
        return null;
    }
}
