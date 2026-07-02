package com.resume.paymentsystem.DTO;

import lombok.Data;

import java.util.List;

@Data
public class InvoiceDTO {
    private String amount;
    private String currency;
    private String description;
    private List<CartItem> cart;
    private String expiresAt;
    private String locale;
}

