package org.example.domain.factory.impl;

import org.example.CommunicationSatellite;
import org.example.Satellite;
import org.example.domain.entity.SatelliteType;
import org.example.domain.factory.SatelliteFactory;
import org.example.domain.dto.param.impl.CommunicationSatelliteParam;
import org.example.domain.dto.param.SatelliteParam;
import org.springframework.stereotype.Component;

@Component
public class CommunicationSatelliteFactory implements SatelliteFactory {

    @Override
    public Satellite createSatelliteWithParameter(SatelliteParam param) {

        if (!(param instanceof CommunicationSatelliteParam communicationSatelliteParam)) {
            throw new RuntimeException("Не верные параметры для communication satellite");
        }

        return new CommunicationSatellite(
                communicationSatelliteParam.getName(),
                communicationSatelliteParam.getBatteryLevel(),
                communicationSatelliteParam.getBandwidth()
        );
    }

    @Override
    public boolean isSatelliteTypeSupported(SatelliteType type) {
        return type == SatelliteType.COMMUNICATION;
    }
}
