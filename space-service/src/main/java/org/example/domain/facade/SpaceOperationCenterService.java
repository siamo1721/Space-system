package org.example.domain.facade;

import org.example.domain.dto.request.AddSatelliteRequest;
import org.example.domain.dto.request.MissionRequest;
import org.example.domain.dto.response.AddSatelliteResponse;
import org.example.domain.dto.response.MissionResponse;
import org.example.domain.entity.Satellite;

import java.util.List;

public interface SpaceOperationCenterService {
    AddSatelliteResponse addSatellite(AddSatelliteRequest addSatelliteRequest);
    MissionResponse executeMission(MissionRequest missionRequest);
    void showConstellationStatus(String constellationName);
    void printAllSatelliteConstellations();
    void createAndSaveConstellation(String name);
    List<Satellite> getSatellitesInConstellation(String constellationName);
}
