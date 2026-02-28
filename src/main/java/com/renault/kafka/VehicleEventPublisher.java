package com.renault.kafka;

import com.renault.dto.response.VehicleResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class VehicleEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(VehicleEventPublisher.class);
    private final KafkaTemplate<String, VehicleResponseDto> kafkaTemplate;
    public VehicleEventPublisher(KafkaTemplate<String, VehicleResponseDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    @Value("${spring.kafka.topic}")
    private String TOPIC ;



    public void publishVehicleCreated(VehicleResponseDto vehicleResponse) {
        kafkaTemplate.send(TOPIC, vehicleResponse);
        log.info("Vehicle published to  topic '{}': {}", TOPIC, vehicleResponse);

    }
}
