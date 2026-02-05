package com.omnistore.dto;

import java.util.List;

public class CartResponseDto {

    private Long cartId;
    private Long userId;
    private Double totalAmount;
    private List<CartItemResponseDto> items;

    public CartResponseDto() {}

    public Long getCartId() { return cartId; }
    public void setCartId(Long cartId) { this.cartId = cartId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public List<CartItemResponseDto> getItems() { return items; }
    public void setItems(List<CartItemResponseDto> items) { this.items = items; }
}
