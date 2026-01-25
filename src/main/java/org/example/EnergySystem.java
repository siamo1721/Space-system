package org.example;

public class EnergySystem {
    protected double batteryLevel;

    public EnergySystem(double batteryLevel){
        this.batteryLevel = batteryLevel;
    }

    public double getBatteryLevel() {
        return batteryLevel;
    }

    public void consume(double amount) {
        this.batteryLevel -= amount;
    }

    public boolean hasEnoughEnergy(double threshold) {
        return batteryLevel > threshold;
    }
}
