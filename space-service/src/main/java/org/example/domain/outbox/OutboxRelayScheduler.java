package org.example.domain.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.kafka.KafkaTopics;
import org.example.domain.kafka.dto.SatelliteEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxRelayScheduler {

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, SatelliteEvent> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelayString = "${app.outbox.relay-interval-ms:5000}")
    @Transactional
    public void relayPendingEvents() {
        List<OutboxEvent> pendingEvents =
                outboxEventRepository.findByStatusOrderByCreatedAtAsc(OutboxStatus.PENDING);

        for (OutboxEvent outboxEvent : pendingEvents) {
            try {
                SatelliteEvent event = objectMapper.readValue(outboxEvent.getPayload(), SatelliteEvent.class);
                String topic = resolveTopic(outboxEvent.getEventType());
                String key = String.valueOf(outboxEvent.getAggregateId());

                kafkaTemplate.send(topic, key, event).get();

                outboxEvent.setStatus(OutboxStatus.SENT);
                outboxEventRepository.save(outboxEvent);
                log.info("Outbox: событие {} отправлено в Kafka (topic={}, key={})",
                        outboxEvent.getId(), topic, key);
            } catch (Exception e) {
                log.error("Outbox: не удалось отправить событие {}", outboxEvent.getId(), e);
            }
        }
    }

    private String resolveTopic(OutboxEventType eventType) {
        return switch (eventType) {
            case CREATED -> KafkaTopics.SATELLITE_CREATED;
            case DELETED -> KafkaTopics.SATELLITE_DELETED;
        };
    }
}
