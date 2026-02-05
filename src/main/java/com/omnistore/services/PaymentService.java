package com.omnistore.services;

import com.omnistore.entity.Order;
import com.omnistore.entity.Payment;
import com.omnistore.exception.ResourceNotFoundException;
import com.omnistore.repository.OrderRepository;
import com.omnistore.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    public Payment pay(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(order.getTotalAmount());
        payment.setProvider("MOCK_GATEWAY");
        payment.setStatus("INITIATED");
        payment.setCreatedAt(LocalDateTime.now());

        // mock response
        boolean success = new Random().nextInt(10) < 9; // 90% success
        payment.setStatus(success ? "SUCCESS" : "FAILED");

        return paymentRepository.save(payment);
    }
}
