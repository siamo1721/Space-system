package org.example.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "satellite")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "sat_type")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CommunicationSatellite.class, name = "COMMUNICATION"),
        @JsonSubTypes.Type(value = ImagingSatellite.class, name = "IMAGING")
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public abstract class Satellite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    protected String name;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
    @JoinColumn(name = "state_id", nullable = false, unique = true)
    protected SatelliteState state;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
    @JoinColumn(name = "energy_id", nullable = false, unique = true)
    protected EnergySystem energy;

    @ManyToOne()
    @JoinColumn(name = "constellation_id")
    @JsonBackReference
    private SatelliteConstellation constellation;

    @Column(name = "internal_temperature")
    protected Double internalTemperature = 20.0;

    @Column(name = "external_temperature")
    protected Double externalTemperature = -50.0;

    protected Satellite(String name, double batteryLevel) {
        this.name = name;
        this.state = new SatelliteState();
        this.energy = new EnergySystem(batteryLevel);
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
        state.deactivate();
    }

    public void consumeBattery(double amount) {
        energy.consume(amount);
        if (!energy.hasEnoughEnergy(0.2)) {
            state.deactivate();
        }
    }

    protected abstract void performMission();
}
