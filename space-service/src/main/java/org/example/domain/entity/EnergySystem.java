package org.example.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "energy_system")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EnergySystem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "battery_level", nullable = false)
    private double batteryLevel;

    public EnergySystem(double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public void consume(double amount) {
        this.batteryLevel -= amount;
    }

    public boolean hasEnoughEnergy(double threshold) {
        return batteryLevel > threshold;
    }
}
