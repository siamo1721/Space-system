package org.example.client;

import org.example.domain.MissionRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class SpaceOperationClient {
    private static final Logger log = LoggerFactory.getLogger(SpaceOperationClient.class);
    private final RestClient restClient;

    public SpaceOperationClient(RestClient spaceOperationRestClient) {
        this.restClient = spaceOperationRestClient;
    }

    public void executeMission(MissionRequest request) {
        try {
            restClient.post()
                    .uri("/missions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();
            log.info("Successfully executed mission for request: {}", request);
        } catch (Exception e) {
            log.error("Failed to execute mission for request: {}. Error: {}", request, e.getMessage());
        }
    }
}
