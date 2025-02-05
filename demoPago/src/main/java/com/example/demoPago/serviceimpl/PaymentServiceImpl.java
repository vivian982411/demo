package com.example.demoPago.serviceimpl;

import com.example.demoPago.kafka.PaymentStatusProducer;
import com.example.demoPago.model.Payment;
import com.example.demoPago.model.User;
import com.example.demoPago.repository.PaymentRepository;
import com.example.demoPago.repository.UserRepository;
import com.example.demoPago.service.PaymentService;
import com.example.demoPago.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentStatusProducer paymentStatusProducer;

    public Payment createPayment(Payment payment, Long payerId, Long payeeId) {
        User payer = userRepository.findById(payerId).orElseThrow(() -> new NotFoundException(String.format("Payer %d not found",payeeId)));
        User payee = userRepository.findById(payeeId).orElseThrow(() -> new NotFoundException(String.format("Payee %d not found",payeeId)));
        payment.setPayer(payer);
        payment.setPayee(payee);
        return paymentRepository.save(payment);
    }

    public Payment getPaymentStatus(Long id) {
        return paymentRepository.findById(id).orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    public Payment updatePaymentStatus(Long id, String status) {
        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setStatus(status);
        Payment updatedPayment = paymentRepository.save(payment);
        paymentStatusProducer.sendPaymentStatusUpdate(id, status);
        return updatedPayment;
    }
}