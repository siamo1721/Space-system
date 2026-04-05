package org.example.domain.dto.param.impl;

import lombok.Getter;
import org.example.domain.entity.SatelliteType;
import org.example.domain.dto.param.SatelliteParam;

@Getter
public class ImagingSatelliteParam extends SatelliteParam {
    private final double resolution;

    public ImagingSatelliteParam(String name, double batteryLevel, double resolution) {
        super(SatelliteType.IMAGE, name, batteryLevel);
        this.resolution = resolution;
    }

}
