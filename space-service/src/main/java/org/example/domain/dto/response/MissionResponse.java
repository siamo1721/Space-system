package org.example.domain.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MissionResponse {
    private String constellationName;
    private List<String> executedSatellites;
    private String missionSummary;
}
