package org.example;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ImagingSatellite extends Satellite {
    private double resolution;
    private int photosTaken;


    public ImagingSatellite(String name,double batteryLevel, double resolution) {
        super(name, batteryLevel);
        this.resolution = resolution;
        this.photosTaken = 0;
        this.name = name;
    }

    @Override
    protected void performMission() {
        if (isActive()) {
            energy.consume(0.08);
        }
        if (isActive()) {
            takePhoto();
            System.out.println(this.name + ":" + " Съемка территории с разрешением " + this.resolution + " м/пиксель");
            System.out.println(this.name + ": " + "Снимок #" + this.getPhotosTaken() + " сделан!");
        } else {
            System.out.println("\uD83D\uDED1 " + this.name + ":" + " Не может выполнить съемку - не активен");
        }
    }

    private void takePhoto() {
        if (isActive()) {
            this.photosTaken++;
        }
    }
}
