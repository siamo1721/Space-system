package org.example.param;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.entity.SatelliteType;

@AllArgsConstructor
@Getter
public abstract class SatelliteParam {
    private SatelliteType type;
    private String name;
    private double batteryLevel;
}
