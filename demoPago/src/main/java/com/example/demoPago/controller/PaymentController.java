package com.example.demoPago.controller;


import com.example.demoPago.model.Payment;
import com.example.demoPago.serviceimpl.PaymentServiceImpl;
import com.example.demoPago.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentServiceImpl paymentService;

    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment, @RequestParam Long payerId, @RequestParam Long payeeId) {
        Payment paymentResponse = paymentService.createPayment(payment, payerId, payeeId);
        if (Objects.isNull(paymentResponse))
            throw new NotFoundException("");
        return new ResponseEntity<>(paymentResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentStatus(@PathVariable Long id) {
        Payment payment = paymentService.getPaymentStatus(id);
        if (Objects.isNull(payment))
            throw new NotFoundException("");
        return new ResponseEntity<>(payment, HttpStatus.OK);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Payment> updatePaymentStatus(@PathVariable Long id, @RequestParam String status) {
        Payment payment = paymentService.updatePaymentStatus(id, status);
        if (Objects.isNull(payment))
            throw new NotFoundException("");
        return new ResponseEntity<>(payment, HttpStatus.OK);
    }
}