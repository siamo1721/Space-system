package org.example.domain.repository;

import org.example.domain.entity.SatelliteConstellation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConstellationRepository extends JpaRepository<SatelliteConstellation, Long> {
    Optional<SatelliteConstellation> findByConstellationName(String constellationName);
}
