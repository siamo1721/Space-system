package org.example;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CommunicationSatellite extends Satellite {

    private double bandWidth;

    private void sendData(double amount) {
        System.out.println(this.name + ": Передача данных со скоростью " + amount + " Мбит/с");
        System.out.println(this.name + ": Отправил " + amount + " Мбит данных!");
    }

    public CommunicationSatellite(String name, double batteryLevel, double bandWidth) {
        super(name, batteryLevel);
        this.bandWidth = bandWidth;
    }

    @Override
    protected void performMission() {
        if (isActive()) {
            energy.consume(0.05);
            if (isActive()) {
                sendData(this.bandWidth);
            }
        }
    }
}
