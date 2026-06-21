package org.example.domain.controller;

import lombok.RequiredArgsConstructor;
import org.example.domain.dto.request.AddSatelliteRequest;
import org.example.domain.dto.request.MissionRequest;
import org.example.domain.entity.Satellite;
import org.example.domain.entity.SatelliteConstellation;
import org.example.domain.facade.SpaceOperationCenterService;
import org.example.domain.service.ConstellationService;
import org.example.domain.service.SatelliteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SpaceOperationController {
    private final SpaceOperationCenterService spaceOperationCenterService;
    private final SatelliteService satelliteService;
    private final ConstellationService constellationService;

    @PostMapping("/missions")
    public ResponseEntity<Void> executeMission(@RequestBody MissionRequest request) {
        spaceOperationCenterService.executeMission(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add-satellites")
    public ResponseEntity<Void> addSatellite(@RequestBody AddSatelliteRequest request) {
        spaceOperationCenterService.addSatellite(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/satellites/{name}")
    public ResponseEntity<Void> deleteSatellite(@PathVariable String name) {
        spaceOperationCenterService.deleteSatellite(name);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/overview")
    public ResponseEntity<Void> overview() {
        spaceOperationCenterService.printAllSatelliteConstellations();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/satellites")
    public ResponseEntity<List<Satellite>> getAllSatellites() {
        return ResponseEntity.ok(satelliteService.getAllSatellites());
    }

    @GetMapping("/satellites/{id}")
    public ResponseEntity<Satellite> getSatelliteById(@PathVariable Long id) {
        return ResponseEntity.ok(satelliteService.getSatelliteById(id));
    }

    @GetMapping("/constellations/{name}")
    public ResponseEntity<SatelliteConstellation> getConstellationByName(@PathVariable String name) {
        return ResponseEntity.ok(constellationService.getConstellationByName(name));
    }

}
