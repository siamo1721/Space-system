package org.example.domain.dto.param;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.domain.entity.SatelliteType;

@AllArgsConstructor
@Getter
public abstract class SatelliteParam {
    private SatelliteType type;
    private String name;
    private double batteryLevel;
}
