package org.example;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EnergySystem {
    protected double batteryLevel;

    public void consume(double amount) {
        this.batteryLevel -= amount;
    }

    public boolean hasEnoughEnergy(double threshold) {
        return batteryLevel > threshold;
    }
}
