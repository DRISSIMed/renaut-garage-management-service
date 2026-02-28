package com.renault.kafka;

import com.renault.dto.response.VehicleResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleEventPublisher {

    private final KafkaTemplate<String, VehicleResponseDto> kafkaTemplate;

    @Value("${spring.kafka.topic}")
    private String topic;

    public void publishVehicleCreated(VehicleResponseDto vehicleResponse) {
        try {
            var result = kafkaTemplate.send(topic, vehicleResponse).get();
            log.info("Vehicle published to topic '{}' partition={} offset={}",
                    topic,
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset());
        } catch (Exception ex) {
            log.error("Failed to publish vehicle to topic '{}'", topic, ex);
        }
    }
}