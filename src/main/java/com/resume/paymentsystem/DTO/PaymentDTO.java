package com.resume.paymentsystem.DTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.List;
import java.util.UUID;

public record PaymentDTO(UUID uuid,
                         Long amount,
                         String currency,
                         String status,
                         List<String> checkOutUrl) {

    private static final Logger log = LoggerFactory.getLogger(PaymentDTO.class);

//    public PaymentDTO{
//        if (!validate()){
//            throw new IllegalArgumentException();
//        }
//    }
public PaymentDTO {
    if (uuid == null || amount == null || currency == null || currency.isBlank()
            || status == null || status.isBlank() || checkOutUrl == null || checkOutUrl.isEmpty()) {
        throw new IllegalArgumentException("Invalid payment data");
    }

}
//    public boolean validate() {
//        RecordComponent[] components = this.getClass().getRecordComponents();
//
//        log.info("data {} {} {} {} {}", uuid, amount, currency, status, checkOutUrl);
//        for (RecordComponent component : components) {
//            try {
//                Method accessor = component.getAccessor();
//                Object value = accessor.invoke(this);
//
//                if (value == null) {
//
//                    log.info("value is null");
//                    return false;
//                } else if (value.toString().isBlank()) {
//                    log.info("value is blank");
//                    return false;
//
//
//                }
//            } catch (Exception e) {
//                throw new RuntimeException("ошибка валидации аргументов" + e);
//            }
//        }
//
//    return true;
//    }

    }




