package org.example.domain.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddSatelliteResponse {
    private String satelliteName;
    private String communicationName;
}
