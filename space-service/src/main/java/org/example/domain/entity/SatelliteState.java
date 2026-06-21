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
@Table(name = "satellite_state")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SatelliteState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "status_message", nullable = false)
    private String statusMessage = "Не активирован";

    public void activate() {
        active = true;
        statusMessage = "Активен";
    }

    public void deactivate() {
        active = false;
        statusMessage = "Не активирован";
    }
}
