package org.example;

import org.example.domain.dto.request.AddSatelliteRequest;
import org.example.domain.dto.request.MissionRequest;
import org.example.domain.dto.param.impl.CommunicationSatelliteParam;
import org.example.domain.dto.param.impl.ImagingSatelliteParam;
import org.example.domain.dto.param.SatelliteParam;
import org.example.domain.entity.enums.MissionType;
import org.example.domain.facade.SpaceOperationCenterService;
import org.example.domain.service.SatelliteService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Main {

    public static void main(String[] args) {

        ConfigurableApplicationContext context =
                SpringApplication.run(Main.class, args);

        SpaceOperationCenterService facade =
                context.getBean(SpaceOperationCenterService.class);
        SatelliteService satelliteService =
                context.getBean(SatelliteService.class);

        System.out.println("ЗАПУСК СИСТЕМЫ УПРАВЛЕНИЯ СПУТНИКОВОЙ ГРУППИРОВКОЙ");
        System.out.println("============================================================");

        facade.createAndSaveConstellation("Орбита-1");
        facade.createAndSaveConstellation("Орбита-2");

        System.out.println("\nСОЗДАНИЕ И ДОБАВЛЕНИЕ СПУТНИКОВ:");
        System.out.println("---------------------------------------------");

        seedSatelliteIfAbsent(facade, satelliteService,
                new CommunicationSatelliteParam("Связь-1", 500, 0.85), "Орбита-1");
        seedSatelliteIfAbsent(facade, satelliteService,
                new CommunicationSatelliteParam("Связь-2", 1000, 0.75), "Орбита-2");
        seedSatelliteIfAbsent(facade, satelliteService,
                new ImagingSatelliteParam("ДЗЗ-1", 2.5, 0.92), "Орбита-1");
        seedSatelliteIfAbsent(facade, satelliteService,
                new ImagingSatelliteParam("ДЗЗ-2", 1.0, 0.45), "Орбита-1");
        seedSatelliteIfAbsent(facade, satelliteService,
                new ImagingSatelliteParam("ДЗЗ-3", 0.5, 0.15), "Орбита-2");

        System.out.println("---------------------------------------------");

        facade.executeMission(MissionRequest.builder().constellationName("Орбита-1").missionType(MissionType.IMAGING).build());
        facade.executeMission(MissionRequest.builder().constellationName("Орбита-2").missionType(MissionType.COMMUNICATION).build());

        System.out.println("\nСТАТУС ГРУППИРОВОК:");
        facade.showConstellationStatus("Орбита-1");
        facade.showConstellationStatus("Орбита-2");

        System.out.println("\nВСЕ ГРУППИРОВКИ В РЕПОЗИТОРИИ:");
        facade.printAllSatelliteConstellations();
    }

    private static void seedSatelliteIfAbsent(
            SpaceOperationCenterService facade,
            SatelliteService satelliteService,
            SatelliteParam param,
            String constellationName) {
        if (satelliteService.existsByName(param.getName())) {
            System.out.println("Спутник уже существует: " + param.getName());
            return;
        }
        facade.addSatellite(AddSatelliteRequest.builder()
                .param(param)
                .communicationName(constellationName)
                .build());
    }
}
