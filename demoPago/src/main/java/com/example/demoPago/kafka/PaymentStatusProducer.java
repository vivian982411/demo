package com.example.demoPago.kafka;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentStatusProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendPaymentStatusUpdate(Long paymentId, String status) {
        String message = "Payment ID: " + paymentId + ", Status: " + status;
        kafkaTemplate.send("payment-status", message);
    }
}
