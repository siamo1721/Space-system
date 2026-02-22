package org.example.param;

import lombok.Getter;
import org.example.entity.SatelliteType;

@Getter
public class ImagingSatelliteParam extends SatelliteParam {
    private final double resolution;

    public ImagingSatelliteParam(String name, double batteryLevel, double resolution) {
        super(SatelliteType.IMAGE, name, batteryLevel);
        this.resolution = resolution;
    }

}
