package com.resume.paymentsystem.DTO;

import lombok.Data;

@Data
public class CartItem {
    private String description;
    private AmountDTO price;
    private Integer quantity;

}


