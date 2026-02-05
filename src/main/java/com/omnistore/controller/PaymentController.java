package com.omnistore.controller;

import com.omnistore.entity.Payment;
import com.omnistore.services.OrderService;
import com.omnistore.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderService orderService;

    public PaymentController(PaymentService paymentService,
                             OrderService orderService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
    }

    @PostMapping("/pay/{orderId}")
    public ResponseEntity<Payment> pay(@PathVariable Long orderId) {
        Payment payment = paymentService.pay(orderId);
        orderService.updateStatusAfterPayment(orderId, "SUCCESS".equals(payment.getStatus()));
        return ResponseEntity.ok(payment);
    }
}
