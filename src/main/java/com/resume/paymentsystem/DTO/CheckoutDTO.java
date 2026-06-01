package com.resume.paymentsystem.DTO;

import lombok.Data;

@Data
public class CheckoutDTO {
    String price;
    String currency;
    Long quantity;
}
