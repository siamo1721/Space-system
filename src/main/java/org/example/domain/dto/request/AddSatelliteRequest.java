package org.example.domain.dto.request;
import lombok.Builder;
import org.example.domain.dto.param.SatelliteParam;
import lombok.Data;

@Data
@Builder
public class AddSatelliteRequest {
    private SatelliteParam param;
    private String communicationName;
}
