package org.example.domain.dto.param.impl;

import lombok.Getter;
import org.example.domain.entity.enums.SatelliteType;
import org.example.domain.dto.param.SatelliteParam;

@Getter
public class CommunicationSatelliteParam extends SatelliteParam {
    private final double bandwidth;

    public CommunicationSatelliteParam(String name, double batteryLevel, double bandwidth) {
        super(SatelliteType.COMMUNICATION, name, batteryLevel);
        this.bandwidth = bandwidth;
    }

}
