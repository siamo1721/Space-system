package org.example.factory;

import org.example.CommunicationSatellite;
import org.example.Satellite;

public class CommunicationSatelliteFactory extends SatelliteFactory {
    @Override
    public Satellite createSatelliteWithParam(String name, double batteryLevel, double extraParameter) {
        return new CommunicationSatellite(name, batteryLevel, extraParameter);
    }

    @Override
    public Satellite createSatellite(String name, double batteryLevel) {
        return null;
    }
}
