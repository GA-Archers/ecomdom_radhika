package com.omnistore.services;

import com.omnistore.dto.OrderResponseDto;
import com.omnistore.dto.PlaceOrderRequestDto;
import com.omnistore.entity.*;
import com.omnistore.exception.BadRequestException;
import com.omnistore.exception.ResourceNotFoundException;
import com.omnistore.repository.CartRepository;
import com.omnistore.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    public OrderService(OrderRepository orderRepository, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
    }

    public Order placeOrder(Long userId, PlaceOrderRequestDto dto) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user ID: " + userId));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());
        order.setShippingAddressId(dto.getShippingAddressId());
        order.setBillingAddressId(dto.getBillingAddressId());

        double total = 0;
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            total += orderItem.getPrice() * orderItem.getQuantity();
            orderItems.add(orderItem);
        }

        order.setTotalAmount(total);
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);

        return savedOrder;
    }

    public List<OrderResponseDto> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(order -> {
            OrderResponseDto dto = new OrderResponseDto();
            dto.setOrderId(order.getId());
            dto.setUserId(order.getUserId());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setStatus(order.getStatus().name());
            dto.setCreatedAt(order.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());
    }

    public Order updateStatusAfterPayment(Long orderId, boolean success) {
        Order order = findOrderById(orderId);
        order.setStatus(success ? OrderStatus.PAID : OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    public Order shipOrder(Long orderId) {
        Order order = findOrderById(orderId);
        if (order.getStatus() != OrderStatus.PAID) {
            throw new BadRequestException("Order cannot be shipped. Current status: " + order.getStatus());
        }
        order.setStatus(OrderStatus.SHIPPED);
        return orderRepository.save(order);
    }

    public Order deliverOrder(Long orderId) {
        Order order = findOrderById(orderId);
        if (order.getStatus() != OrderStatus.SHIPPED) {
            throw new BadRequestException("Order cannot be delivered. Current status: " + order.getStatus());
        }
        order.setStatus(OrderStatus.DELIVERED);
        return orderRepository.save(order);
    }

    public Order cancelOrder(Long orderId) {
        Order order = findOrderById(orderId);
        if (order.getStatus() != OrderStatus.CREATED && order.getStatus() != OrderStatus.PAID) {
            throw new BadRequestException("Order cannot be cancelled. Current status: " + order.getStatus());
        }
        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
    }
}
