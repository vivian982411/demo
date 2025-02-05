package com.example.demoPago.service;

import com.example.demoPago.model.Payment;
import com.example.demoPago.model.User;

public interface PaymentService {
    Payment createPayment(Payment payment, Long payerId, Long payeeId);

    Payment getPaymentStatus(Long id);

    Payment updatePaymentStatus(Long id, String status);
}
