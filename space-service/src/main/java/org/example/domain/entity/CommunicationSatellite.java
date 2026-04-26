package org.example.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "communication_satellite")
@DiscriminatorValue("COMMUNICATION")
public class CommunicationSatellite extends Satellite {

    @Column(name = "band_width", nullable = false)
    private double bandWidth;

    public CommunicationSatellite(String name, double batteryLevel, double bandWidth) {
        super(name, batteryLevel);
        this.bandWidth = bandWidth;
    }

    @Override
    protected void performMission() {
        if (isActive()) {
            consumeBattery(0.05);
        }
    }
}
