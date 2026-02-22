package org.example.factory;

import org.example.CommunicationSatellite;
import org.example.Satellite;
import org.example.entity.SatelliteType;
import org.example.param.CommunicationSatelliteParam;
import org.example.param.SatelliteParam;
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
