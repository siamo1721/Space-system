package org.example;


public class Main {
    static void main() {
        System.out.println("ЗАПУСК СИСТЕМЫ УПРАВЛЕНИЯ СПУТНИКОВОЙ ГРУППИРОВКОЙ");
        System.out.println("============================================================");

        System.out.println("СОЗДАНИЕ СПЕЦИАЛИЗИРОВАННЫХ СПУТНИКОВ:");
        System.out.println("---------------------------------------------");

        CommunicationSatellite s1 = new CommunicationSatellite("Cвязь-1", 0.85, true, 500.0);
        CommunicationSatellite s2 = new CommunicationSatellite("Cвязь-2", 0.75, false, 1000.0);

        ImagingSatellite d1 = new ImagingSatellite(2.5, 0, "ДЗЗ-1", 0.92, false);
        ImagingSatellite d2 = new ImagingSatellite(1.0, 0, "ДЗЗ-2", 0.45, false);
        ImagingSatellite d3 = new ImagingSatellite(0.5, 0, "ДЗЗ-3", 0.15, false);

        Satellite[] satellites = new Satellite[]{s1, s2, d1, d2, d3};
        for (Satellite s : satellites) {
            System.out.println("Создан спутник: " + s.name + " (заряд: " + (int) (s.batteryLevel * 100) + "%)");
        }
        SatelliteConstellation sc = new SatelliteConstellation("RU BASIC");
        System.out.println("---------------------------------------------");
        System.out.println("Создана спутниковая группировка:" + sc.getConstellationName());
        System.out.println("""
                ---------------------------------------------
                ФОРМИРОВАНИЕ ГРУППИРОВКИ:
                -----------------------------------""");
        for (Satellite s : satellites) {
            sc.addSatellite(s);
        }
        System.out.println("---------------------------------------------");
        System.out.println(sc.getSatellite());
        System.out.println("""
                -----------------------------------
                 АКТИВАЦИЯ СПУТНИКОВ:
                -------------------------""");

        for (Satellite s : satellites) {
            if(s.activate()){
                System.out.println("✅ " + s.name +" Активация успешна");
            }else{
                System.out.println("\uD83D\uDED1 " + s.name +" Ошибка активации " + "(заряд: " + (int)(s.batteryLevel * 100) + "%)");
            }
        }
        System.out.println("ВЫПОЛНЕНИЕ МИССИЙ ГРУППИРОВКИ " + sc.getConstellationName());
        System.out.println("==================================================");
        sc.executeAllMission();
        System.out.println(sc.getSatellite());
    }
}
