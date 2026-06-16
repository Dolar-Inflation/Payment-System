package com.resume.paymentsystem.DTO;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

public record PaymentDTO(UUID uuid,
                         Long amount,
                         String currency,
                         String status,
                         List<String> checkOutUrl) {
    private static final Logger log = LoggerFactory.getLogger(PaymentDTO.class);

    public PaymentDTO{
        log.info("data {} {} {} {} {}", uuid, amount, currency, status, checkOutUrl );
        if (amount == null && currency == null && status == null && checkOutUrl == null ) {
            throw new NullPointerException("argument or arguments null");
        } else if (amount < 0 && currency.isBlank() && status.isBlank() && checkOutUrl.isEmpty()) {
            throw new IllegalArgumentException("Illegal argument or arguments");
            
        }
    }


}
