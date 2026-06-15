package org.example.domain.repository;

import org.example.domain.entity.Satellite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SatelliteRepository extends JpaRepository<Satellite, Long> {
    Optional<Satellite> findFirstByNameOrderByIdAsc(String name);

    List<Satellite> findAllByName(String name);

    boolean existsByName(String name);
}
