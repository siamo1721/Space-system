package org.example.domain.dto.request;

import lombok.Builder;
import lombok.Data;
import org.example.domain.entity.MissionType;

@Data
@Builder
public class MissionRequest {
    private String constellationName;
    private MissionType missionType;
}
