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
@Table(name = "imaging_satellite")
@DiscriminatorValue("IMAGING")
public class ImagingSatellite extends Satellite {

    @Column(name = "resolution", nullable = false)
    private double resolution;

    @Column(name = "photos_taken", nullable = false)
    private int photosTaken;

    public ImagingSatellite(String name, double batteryLevel, double resolution) {
        super(name, batteryLevel);
        this.resolution = resolution;
        this.photosTaken = 0;
    }

    @Override
    protected void performMission() {
        if (isActive()) {
            consumeBattery(0.08);
            if (isActive()) {
                photosTaken++;
            }
        }
    }
}
