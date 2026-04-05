package org.example;

import lombok.Data;

@Data
public abstract class Satellite {
    protected String name;
    protected SatelliteState state;
    protected EnergySystem energy;


    protected Satellite(String name, double batteryLevel) {
        this.name = name;
        this.state = new SatelliteState();
        this.energy = EnergySystem.builder().
                batteryLevel(batteryLevel)
                .build();
    }

    public boolean activate() {
        if (energy.hasEnoughEnergy(0.2)) {
            state.activate();
            return true;
        }
        state.deactivate();
        return false;
    }

    public boolean isActive() {
        return state.isActive();
    }

    public double getBatteryLevel() {
        return energy.getBatteryLevel();
    }

    public void deactivate() {
        if (state.isActive()) {
            state.deactivate();
            System.out.println("Спутник выключен");
        } else {
            System.out.println("Спутник уже выключен");
        }
    }

    public void consumeBattery(double amount) {
        energy.consume(amount);
        if (!energy.hasEnoughEnergy(0.2)) {
            state.deactivate();
        }
    }

    protected abstract void performMission();

}
