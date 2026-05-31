package org.example.domain.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.domain.kafka.dto.SatelliteEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, SatelliteEvent> satelliteEventProducerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, SatelliteEvent> satelliteEventKafkaTemplate() {
        return new KafkaTemplate<>(satelliteEventProducerFactory());
    }

    @Bean
    public NewTopic satelliteCreatedTopic() {
        return new NewTopic(KafkaTopics.SATELLITE_CREATED, 1, (short) 1);
    }

    @Bean
    public NewTopic satelliteDeletedTopic() {
        return new NewTopic(KafkaTopics.SATELLITE_DELETED, 1, (short) 1);
    }

    @Bean
    public NewTopic satelliteEventsDltTopic() {
        return new NewTopic(KafkaTopics.SATELLITE_EVENTS_DLT, 1, (short) 1);
    }
}
