package com.omnistore.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private Double amount;

    private String status;   // INITIATED, SUCCESS, FAILED
    private String provider; // MOCK_GATEWAY

    private LocalDateTime createdAt;

    // getters/setters
    public Long getId() { return id; }
    public Long getOrderId() { return orderId; }
    public Double getAmount() { return amount; }
    public String getStatus() { return status; }
    public String getProvider() { return provider; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public void setAmount(Double amount) { this.amount = amount; }
    public void setStatus(String status) { this.status = status; }
    public void setProvider(String provider) { this.provider = provider; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
