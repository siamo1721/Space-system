package org.example.domain.factory.impl;

import org.example.domain.entity.ImagingSatellite;
import org.example.domain.entity.Satellite;
import org.example.domain.entity.enums.SatelliteType;
import org.example.domain.factory.SatelliteFactory;
import org.example.domain.dto.param.impl.ImagingSatelliteParam;
import org.example.domain.dto.param.SatelliteParam;
import org.springframework.stereotype.Component;

@Component
public class ImagingSatelliteFactory implements SatelliteFactory {

    @Override
    public Satellite createSatelliteWithParameter(SatelliteParam param) {
        if (!(param instanceof ImagingSatelliteParam imagingSatelliteParam)) {
            throw new RuntimeException("Не верные параметры для imaging satellite");
        }

        return new ImagingSatellite(
                imagingSatelliteParam.getName(),
                imagingSatelliteParam.getBatteryLevel(),
                imagingSatelliteParam.getResolution()
        );
    }

    @Override
    public boolean isSatelliteTypeSupported(SatelliteType type) {
        return type == SatelliteType.IMAGE;
    }
}
