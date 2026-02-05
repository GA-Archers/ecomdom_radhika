package com.omnistore.controller;

import com.omnistore.dto.OrderResponseDto;
import com.omnistore.dto.PlaceOrderRequestDto;
import com.omnistore.entity.Order;
import com.omnistore.entity.User;
import com.omnistore.services.OrderService;
import com.omnistore.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(Principal principal, @RequestBody PlaceOrderRequestDto dto) {
        User user = userService.findUserByEmail(principal.getName());
        return ResponseEntity.ok(orderService.placeOrder(user.getId(), dto));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getOrders(Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        return ResponseEntity.ok(orderService.getOrdersByUserId(user.getId()));
    }

    @PutMapping("/{orderId}/ship")
    public ResponseEntity<Order> shipOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.shipOrder(orderId));
    }

    @PutMapping("/{orderId}/deliver")
    public ResponseEntity<Order> deliverOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.deliverOrder(orderId));
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }
}
