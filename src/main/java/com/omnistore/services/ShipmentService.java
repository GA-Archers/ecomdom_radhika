package com.omnistore.services;

import com.omnistore.entity.Order;
import com.omnistore.entity.OrderStatus;
import com.omnistore.entity.Shipment;
import com.omnistore.exception.BadRequestException;
import com.omnistore.exception.ResourceNotFoundException;
import com.omnistore.repository.OrderRepository;
import com.omnistore.repository.ShipmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final OrderRepository orderRepository;

    public ShipmentService(ShipmentRepository shipmentRepository, OrderRepository orderRepository) {
        this.shipmentRepository = shipmentRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Shipment createShipment(Long orderId, String trackingNumber, String carrier) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        if (shipmentRepository.findByOrderId(orderId).isPresent()) {
            throw new BadRequestException("Shipment already exists for this order");
        }

        Shipment shipment = new Shipment();
        shipment.setOrder(order);
        shipment.setTrackingNumber(trackingNumber);
        shipment.setCarrier(carrier);
        shipment.setStatus("IN_TRANSIT");
        shipment.setShippedAt(LocalDateTime.now());

        order.setStatus(OrderStatus.SHIPPED);
        orderRepository.save(order);

        return shipmentRepository.save(shipment);
    }

    public Shipment getShipmentByOrderId(Long orderId) {
        return shipmentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found for order ID: " + orderId));
    }

    @Transactional
    public Shipment updateShipmentStatus(Long shipmentId, String status) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with ID: " + shipmentId));

        shipment.setStatus(status);

        if ("DELIVERED".equalsIgnoreCase(status)) {
            shipment.setEstimatedDelivery(LocalDateTime.now());
            shipment.getOrder().setStatus(OrderStatus.DELIVERED);
            orderRepository.save(shipment.getOrder());
        }

        return shipmentRepository.save(shipment);
    }
}
