package org.example.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.kafka.dto.SatelliteEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ObjectMapper kafkaObjectMapper() {
        return JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
    }

    @Bean
    public ConsumerFactory<String, SatelliteEvent> satelliteEventConsumerFactory(ObjectMapper kafkaObjectMapper) {
        JsonDeserializer<SatelliteEvent> jsonDeserializer =
                new JsonDeserializer<>(SatelliteEvent.class, kafkaObjectMapper, false);
        jsonDeserializer.addTrustedPackages("org.example.kafka.dto", "org.example.domain.kafka.dto");

        Map<String, Object> config = consumerConfig();
        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                new ErrorHandlingDeserializer<>(jsonDeserializer)
        );
    }

    private Map<String, Object> consumerConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "telemetry-service-inbox");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return config;
    }

    @Bean
    public ProducerFactory<String, SatelliteEvent> satelliteEventProducerFactory(ObjectMapper kafkaObjectMapper) {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        config.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return new DefaultKafkaProducerFactory<>(
                config,
                new StringSerializer(),
                new JsonSerializer<>(kafkaObjectMapper)
        );
    }

    @Bean
    public KafkaTemplate<String, SatelliteEvent> satelliteEventKafkaTemplate(
            ProducerFactory<String, SatelliteEvent> satelliteEventProducerFactory) {
        return new KafkaTemplate<>(satelliteEventProducerFactory);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SatelliteEvent> kafkaListenerContainerFactory(
            ConsumerFactory<String, SatelliteEvent> satelliteEventConsumerFactory,
            KafkaTemplate<String, SatelliteEvent> satelliteEventKafkaTemplate) {
        ConcurrentKafkaListenerContainerFactory<String, SatelliteEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(satelliteEventConsumerFactory);

        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(
                satelliteEventKafkaTemplate,
                (record, ex) -> {
                    log.error("Битое сообщение из топика {} отправлено в DLT: {}", record.topic(), ex.getMessage());
                    return new org.apache.kafka.common.TopicPartition(KafkaTopics.SATELLITE_EVENTS_DLT, record.partition());
                }
        );
        factory.setCommonErrorHandler(new DefaultErrorHandler(recoverer, new FixedBackOff(1000L, 2L)));
        return factory;
    }
}
