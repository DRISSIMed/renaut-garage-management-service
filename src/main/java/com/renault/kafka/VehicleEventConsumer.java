package com.renault.kafka;

import com.renault.dto.response.VehicleResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;

public class VehicleEventConsumer {
    private static final Logger log = LoggerFactory.getLogger(VehicleEventConsumer.class);
    @KafkaListener(topics ="${spring.kafka.topic}",  groupId ="${spring.kafka.consumer.group-id}" )
    public void consumeVehicleEvent(VehicleResponseDto event) {
        log.info("Vehicle recieved from Kafka topic '{}': {}", "vehicle-events", event);
    }
}
