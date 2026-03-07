package org.example.factory;

import org.example.ImagingSatellite;
import org.example.Satellite;
import org.example.entity.SatelliteType;
import org.example.param.ImagingSatelliteParam;
import org.example.param.SatelliteParam;
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
