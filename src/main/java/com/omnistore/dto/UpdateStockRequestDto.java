package com.omnistore.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class UpdateStockRequestDto {

    @NotNull
    @PositiveOrZero
    private Integer stock;

    public UpdateStockRequestDto() {}

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
