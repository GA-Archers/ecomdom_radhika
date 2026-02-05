package com.omnistore.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "addresses")
public class Address {

    // getters/setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // keep simple for college

    private String label;   // Home / Office
    private String line1;
    private String line2;
    private String city;
    private String state;
    private String pincode;
    private String country;

    private String type; // SHIPPING or BILLING

    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setLabel(String label) { this.label = label; }
    public void setLine1(String line1) { this.line1 = line1; }
    public void setLine2(String line2) { this.line2 = line2; }
    public void setCity(String city) { this.city = city; }
    public void setState(String state) { this.state = state; }
    public void setPincode(String pincode) { this.pincode = pincode; }
    public void setCountry(String country) { this.country = country; }
    public void setType(String type) { this.type = type; }
}
