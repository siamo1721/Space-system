package org.example.param;

import lombok.Getter;
import org.example.entity.SatelliteType;

@Getter
public class CommunicationSatelliteParam extends SatelliteParam {
    private final double bandwidth;

    public CommunicationSatelliteParam(String name, double batteryLevel, double bandwidth) {
        super(SatelliteType.COMMUNICATION, name, batteryLevel);
        this.bandwidth = bandwidth;
    }

}
