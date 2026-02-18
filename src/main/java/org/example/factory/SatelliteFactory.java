package org.example.factory;

import org.example.Satellite;

public abstract class SatelliteFactory {
    public abstract Satellite createSatelliteWithParam(String name, double batteryLevel, double extraParameter);

}
