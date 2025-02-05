package com.example.demoPago.controller;

import com.example.demoPago.model.Payment;
import com.example.demoPago.service.PaymentService;
import com.example.demoPago.serviceimpl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PaymentControllerTest {

    @Mock
    private PaymentServiceImpl paymentServiceImpl;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePayment() {
        // Arrange
        Payment payment = new Payment();
        payment.setConcept("Test Payment");
        payment.setQuantity(1);
        payment.setAmount(100.0);
        payment.setStatus("PENDING");

        when(paymentServiceImpl.createPayment(any(Payment.class), any(Long.class), any(Long.class))).thenReturn(payment);

        // Act
        ResponseEntity<Payment> response = paymentController.createPayment(payment, 1L, 2L);

        // Assert
        assertNotNull(response.getBody());
        assertEquals("Test Payment", response.getBody().getConcept());
        verify(paymentServiceImpl, times(1)).createPayment(any(Payment.class), any(Long.class), any(Long.class));
    }

    @Test
    void testGetPaymentStatus() {
        // Arrange
        Payment payment = new Payment();
        payment.setId(1L);
        payment.setStatus("PENDING");

        when(paymentServiceImpl.getPaymentStatus(1L)).thenReturn(payment);

        // Act
        ResponseEntity<Payment> response = paymentController.getPaymentStatus(1L);

        // Assert
        assertNotNull(response.getBody());
        assertEquals("PENDING", response.getBody().getStatus());
        verify(paymentServiceImpl, times(1)).getPaymentStatus(1L);
    }

    @Test
    void testUpdatePaymentStatus() {
        // Arrange
        Payment payment = new Payment();
        payment.setId(1L);
        payment.setStatus("COMPLETED");

        when(paymentServiceImpl.updatePaymentStatus(1L, "COMPLETED")).thenReturn(payment);

        // Act
        ResponseEntity<Payment> response = paymentController.updatePaymentStatus(1L, "COMPLETED");

        // Assert
        assertNotNull(response.getBody());
        assertEquals("COMPLETED", response.getBody().getStatus());
        verify(paymentServiceImpl, times(1)).updatePaymentStatus(1L, "COMPLETED");
    }
}