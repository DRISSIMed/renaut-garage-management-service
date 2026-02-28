package com.renault.kafka;

import com.renault.dto.response.VehicleResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VehicleEventPublisher {
    private final KafkaTemplate<String, VehicleResponseDto> kafkaTemplate;
    @Value("${spring.kafka.topic}")
    private  String topic ;

    public void publishVehicleCreated(VehicleResponseDto vehicleResponse) {
        kafkaTemplate.send(topic, vehicleResponse)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Vehicle published to topic '{}': {}", topic, vehicleResponse);
                    } else {
                        log.error("Failed to publish vehicle", ex);
                    }
                });
    }
}
