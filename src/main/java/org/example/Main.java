package org.example;

import org.example.domain.dto.request.AddSatelliteRequest;
import org.example.domain.dto.request.MissionRequest;
import org.example.domain.dto.param.impl.CommunicationSatelliteParam;
import org.example.domain.dto.param.impl.ImagingSatelliteParam;
import org.example.domain.dto.param.SatelliteParam;
import org.example.domain.entity.MissionType;
import org.example.domain.facade.SpaceOperationCenterService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {

        ConfigurableApplicationContext context =
                SpringApplication.run(Main.class, args);

        SpaceOperationCenterService facade =
                context.getBean(SpaceOperationCenterService.class);

        System.out.println("ЗАПУСК СИСТЕМЫ УПРАВЛЕНИЯ СПУТНИКОВОЙ ГРУППИРОВКОЙ");
        System.out.println("============================================================");

        facade.createAndSaveConstellation("Орбита-1");
        facade.createAndSaveConstellation("Орбита-2");

        System.out.println("\nСОЗДАНИЕ И ДОБАВЛЕНИЕ СПУТНИКОВ:");
        System.out.println("---------------------------------------------");

        SatelliteParam s1Param = new CommunicationSatelliteParam("Связь-1", 500, 0.85);
        SatelliteParam s2Param = new CommunicationSatelliteParam("Связь-2", 1000, 0.75);
        SatelliteParam d1Param = new ImagingSatelliteParam("ДЗЗ-1", 2.5, 0.92);
        SatelliteParam d2Param = new ImagingSatelliteParam("ДЗЗ-2", 1.0, 0.45);
        SatelliteParam d3Param = new ImagingSatelliteParam("ДЗЗ-3", 0.5, 0.15);

        facade.addSatellite(AddSatelliteRequest.builder()
                .param(s1Param)
                .communicationName("Орбита-1")
                .build());

        facade.addSatellite(AddSatelliteRequest.builder()
                .param(d1Param)
                .communicationName("Орбита-1")
                .build());

        facade.addSatellite(AddSatelliteRequest.builder()
                .param(d2Param)
                .communicationName("Орбита-1")
                .build());

        facade.addSatellite(AddSatelliteRequest.builder()
                .param(s2Param)
                .communicationName("Орбита-2")
                .build());

        facade.addSatellite(AddSatelliteRequest.builder()
                .param(d3Param)
                .communicationName("Орбита-2")
                .build());

        System.out.println("---------------------------------------------");

        facade.executeMission( MissionRequest.builder().constellationName("Орбита-1").missionType(MissionType.IMAGING).build());
        facade.executeMission(MissionRequest.builder().constellationName("Орбита-2").missionType(MissionType.COMMUNICATION).build());

        System.out.println("\nСТАТУС ГРУППИРОВОК:");
        facade.showConstellationStatus("Орбита-1");
        facade.showConstellationStatus("Орбита-2");

        System.out.println("\nВСЕ ГРУППИРОВКИ В РЕПОЗИТОРИИ:");
        facade.printAllSatelliteConstellations();
    }
}