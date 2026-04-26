package org.example.domain.facade;

import lombok.RequiredArgsConstructor;
import org.example.aop.annotation.LogExecutionTime;
import org.example.domain.dto.request.AddSatelliteRequest;
import org.example.domain.dto.request.MissionRequest;
import org.example.domain.dto.response.AddSatelliteResponse;
import org.example.domain.dto.response.MissionResponse;
import org.example.domain.entity.Satellite;
import org.example.domain.entity.SatelliteConstellation;
import org.example.domain.service.ConstellationService;
import org.example.domain.service.SatelliteService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpaceOperationCenterServiceImpl implements SpaceOperationCenterService {

    private final ConstellationService constellationService;
    private final SatelliteService satelliteService;

    @LogExecutionTime
    @Override
    public AddSatelliteResponse addSatellite(AddSatelliteRequest addSatelliteRequest) {
        Satellite satellite = satelliteService.createSatellite(addSatelliteRequest.getParam());

        SatelliteConstellation satelliteConstellation = constellationService.findByNameConstellation(addSatelliteRequest.getCommunicationName());

        constellationService.addSatelliteToConstellation(satelliteConstellation.getConstellationName(), satellite);

        return AddSatelliteResponse.builder()
                .satelliteName(satellite.getName())
                .communicationName(satelliteConstellation.getConstellationName())
                .build();
    }

    @LogExecutionTime
    @Override
    public MissionResponse executeMission(MissionRequest missionRequest) {

        SatelliteConstellation constellation =
                constellationService.findByNameConstellation(missionRequest.getConstellationName());

        var satellites = constellation.getSatellite();

        List<String> executedSatellites = new ArrayList<>();

        satellites.forEach(satellite -> {

            constellationService.executeConstellationMission(constellation.getConstellationName());

            executedSatellites.add(satellite.getName());
        });

        return MissionResponse.builder()
                .constellationName(constellation.getConstellationName())
                .executedSatellites(executedSatellites)
                .missionSummary("Миссия " + missionRequest.getMissionType() +
                        " выполнена для всех спутников группировки.")
                .build();
    }

    @Override
    public void showConstellationStatus(String constellationName) {
        SatelliteConstellation constellation =
                constellationService.findByNameConstellation(constellationName);

        constellation.getSatellite().forEach(s ->
                System.out.println(s.getName() + ": " + s.getState())
        );
    }

    @Override
    public void printAllSatelliteConstellations() {
        constellationService.printAllSatelliteConstellations();
    }

    @Override
    public List<Satellite> getSatellitesInConstellation(String constellationName) {
        return constellationService.findByNameConstellation(constellationName).getSatellite();
    }

    @Override
    public void createAndSaveConstellation(String name) {
        constellationService.createAndSaveConstellation(name);
    }
}
