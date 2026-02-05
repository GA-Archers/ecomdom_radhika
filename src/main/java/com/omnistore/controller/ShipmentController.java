package com.omnistore.controller;

import com.omnistore.entity.Shipment;
import com.omnistore.services.ShipmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Shipment> getShipmentByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(shipmentService.getShipmentByOrderId(orderId));
    }

    @PostMapping("/create")
    public ResponseEntity<Shipment> createShipment(@RequestParam Long orderId,
                                                   @RequestParam String trackingNumber,
                                                   @RequestParam String carrier) {
        return ResponseEntity.ok(shipmentService.createShipment(orderId, trackingNumber, carrier));
    }

    @PutMapping("/{shipmentId}/status")
    public ResponseEntity<Shipment> updateShipmentStatus(@PathVariable Long shipmentId,
                                                         @RequestParam String status) {
        return ResponseEntity.ok(shipmentService.updateShipmentStatus(shipmentId, status));
    }
}
