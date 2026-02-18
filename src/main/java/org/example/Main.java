package org.example;

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

        System.out.println("ЗАПУСК СИСТЕМЫ УПРАВЛЕНИЯ СПУТНИКОВОЙ ГРУППИРОВКОЙ");
        System.out.println("============================================================");

        System.out.println("\nСОЗДАНИЕ СПЕЦИАЛИЗИРОВАННЫХ СПУТНИКОВ:");
        System.out.println("---------------------------------------------");

        Satellite s1 = new CommunicationSatellite("Связь-1", 500, 0.85);
        Satellite s2 = new CommunicationSatellite("Связь-2", 1000, 0.75);
        Satellite d1 = new ImagingSatellite("ДЗЗ-1", 2.5, 0.92);
        Satellite d2 = new ImagingSatellite("ДЗЗ-2", 1.0, 0.45);
        Satellite d3 = new ImagingSatellite("ДЗЗ-3", 0.5, 0.15);

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