package com.example.demoPagoConsumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "payment-status", groupId = "payment-consumer-group")
    public void listen(String message) {
        System.out.println("Received message: " + message);
    }
}
