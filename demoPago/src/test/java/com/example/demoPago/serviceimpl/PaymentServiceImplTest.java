package com.example.demoPago.serviceimpl;

import com.example.demoPago.kafka.PaymentStatusProducer;
import com.example.demoPago.model.Payment;
import com.example.demoPago.model.User;
import com.example.demoPago.repository.PaymentRepository;
import com.example.demoPago.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PaymentStatusProducer paymentStatusProducer;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreatePayment_Success() {
        // Arrange
        Payment payment = new Payment();
        payment.setAmount(100.0);

        User payer = new User();
        payer.setId(1L);
        payer.setName("John Doe");

        User payee = new User();
        payee.setId(2L);
        payee.setName("Jane Doe");

        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(payee));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        // Act
        Payment createdPayment = paymentService.createPayment(payment, 1L, 2L);

        // Assert
        assertNotNull(createdPayment);
        assertEquals(payer, createdPayment.getPayer());
        assertEquals(payee, createdPayment.getPayee());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(2L);
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    public void testCreatePayment_PayerNotFound() {
        // Arrange
        Payment payment = new Payment();
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            paymentService.createPayment(payment, 1L, 2L);
        });
        assertEquals("Payer 2 not found", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).findById(2L);
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    public void testCreatePayment_PayeeNotFound() {
        // Arrange
        Payment payment = new Payment();
        User payer = new User();
        payer.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            paymentService.createPayment(payment, 1L, 2L);
        });
        assertEquals("Payee 2 not found", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(2L);
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    public void testGetPaymentStatus_Success() {
        // Arrange
        Payment payment = new Payment();
        payment.setId(1L);
        payment.setStatus("Pending");

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        // Act
        Payment retrievedPayment = paymentService.getPaymentStatus(1L);

        // Assert
        assertNotNull(retrievedPayment);
        assertEquals(payment.getId(), retrievedPayment.getId());
        assertEquals(payment.getStatus(), retrievedPayment.getStatus());
        verify(paymentRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetPaymentStatus_NotFound() {
        // Arrange
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            paymentService.getPaymentStatus(1L);
        });
        assertEquals("Payment not found", exception.getMessage());
        verify(paymentRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdatePaymentStatus_Success() {
        // Arrange
        Payment payment = new Payment();
        payment.setId(1L);
        payment.setStatus("Pending");

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        // Act
        Payment updatedPayment = paymentService.updatePaymentStatus(1L, "Completed");

        // Assert
        assertNotNull(updatedPayment);
        assertEquals("Completed", updatedPayment.getStatus());
        verify(paymentRepository, times(1)).findById(1L);
        verify(paymentRepository, times(1)).save(payment);
        verify(paymentStatusProducer, times(1)).sendPaymentStatusUpdate(1L, "Completed");
    }

    @Test
    public void testUpdatePaymentStatus_NotFound() {
        // Arrange
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            paymentService.updatePaymentStatus(1L, "Completed");
        });
        assertEquals("Payment not found", exception.getMessage());
        verify(paymentRepository, times(1)).findById(1L);
        verify(paymentRepository, never()).save(any(Payment.class));
        verify(paymentStatusProducer, never()).sendPaymentStatusUpdate(anyLong(), anyString());
    }
}