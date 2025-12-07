package org.example;

public abstract class Satellite {
    protected String name;
    protected boolean isActive;
    protected double batteryLevel;

    public boolean activate() {
        if (this.batteryLevel > 0.2) {
            this.isActive = true;
            return true;
        }
        this.isActive = false;
        return false;
    }

    public void deactivate() {
        if (this.isActive) {
            this.isActive = false;
            System.out.println("Спутник выключен");
        } else {
            System.out.println("Спутник уже выключен");
        }
    }

    public void consumeBattery(double amount) {
        this.batteryLevel -= amount;
        if (this.batteryLevel < 0.2) {
            deactivate();
        }
    }
    protected abstract void performMission();
}
