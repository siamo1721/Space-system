package org.example;

import org.example.param.CommunicationSatelliteParam;
import org.example.param.ImagingSatelliteParam;
import org.example.param.SatelliteParam;
import org.example.service.SatelliteService;
import org.example.service.SpaceOperationCenterService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {

        ConfigurableApplicationContext context =
                SpringApplication.run(Main.class, args);

        SpaceOperationCenterService service =
                context.getBean(SpaceOperationCenterService.class);

        SatelliteService satelliteService =
                context.getBean(SatelliteService.class);



        System.out.println("ЗАПУСК СИСТЕМЫ УПРАВЛЕНИЯ СПУТНИКОВОЙ ГРУППИРОВКОЙ");
        System.out.println("============================================================");

        System.out.println("\nСОЗДАНИЕ СПЕЦИАЛИЗИРОВАННЫХ СПУТНИКОВ:");
        System.out.println("---------------------------------------------");

        SatelliteParam s1Param = new CommunicationSatelliteParam("Связь-1", 500, 0.85);
        SatelliteParam s2Param = new CommunicationSatelliteParam("Связь-2", 1000, 0.75);
        SatelliteParam d1Param = new ImagingSatelliteParam("ДЗЗ-1", 2.5, 0.92);
        SatelliteParam d2Param = new CommunicationSatelliteParam("ДЗЗ-2", 1.0, 0.45);
        SatelliteParam d3Param = new CommunicationSatelliteParam("ДЗЗ-3", 0.5, 0.15);

        Satellite s1 = satelliteService.createSatellite(s1Param);
        Satellite s2 = satelliteService.createSatellite(s2Param);
        Satellite d1 = satelliteService.createSatellite(d1Param);
        Satellite d2 = satelliteService.createSatellite(d2Param);
        Satellite d3 = satelliteService.createSatellite(d3Param);

        Satellite[] satellites = {s1, s2, d1, d2, d3};

        for (Satellite s : satellites) {
            System.out.println("Создан спутник: " + s.getName() +
                    " (" + s.getBatteryLevel() + ")");
        }

        System.out.println("---------------------------------------------");

        service.createAndSaveConstellation("Орбита-1");
        service.createAndSaveConstellation("Орбита-2");

        System.out.println("\n📡 ДОБАВЛЕНИЕ СПУТНИКОВ:");
        service.addSatelliteToConstellation("Орбита-1", s1);
        service.addSatelliteToConstellation("Орбита-1", d1);
        service.addSatelliteToConstellation("Орбита-1", d2);

        service.addSatelliteToConstellation("Орбита-2", s2);
        service.addSatelliteToConstellation("Орбита-2", d3);

        service.activateAllSatellites("Орбита-1");
        service.executeConstellationMission("Орбита-1");
        service.showConstellationStatus("Орбита-1");

        System.out.println("\nВСЕ ГРУППИРОВКИ В РЕПОЗИТОРИИ:");
        service.printAllSatelliteConstellations();
    }
}