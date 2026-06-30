package com.resume.paymentsystem.Service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.deelter.yookassa.YooKassa;
import ru.deelter.yookassa.data.impl.Amount;
import ru.deelter.yookassa.data.impl.Currency;
import ru.deelter.yookassa.data.impl.Payment;
import ru.deelter.yookassa.data.impl.requests.PaymentCreateData;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;
import java.util.UUID;

@Service
public class Dud {

    @Value("${UCASSA_API_SECRET_KEY}")
    private String ucassaApiSecretKey;

    private static final YooKassa YOO_KASSA = YooKassa.create(
            1388972,"test__k65ZNU2IPJpETbUZYWJt3sI_XhF2C5nAvVGVUxGRF8"

    );
//    @PostConstruct
//    public void init() {
//        final YooKassa YOO_KASSA = YooKassa.create(
//                1388972,ucassaApiSecretKey
//
//        );
//    }

@JsonDeserialize
    public Payment createPayment() throws IOException {
        return YOO_KASSA.createPayment(PaymentCreateData.builder()
                .amount(Amount.from(100, Currency.RUB))
                .description("Buy a coffee")
                .redirect("https://github.com/deelter")
                .capture(true)
                .build()
        );
    }
    public Payment getPayment(UUID paymentId) throws IOException {
        return YOO_KASSA.getPayment(paymentId);
    }

    // Is payment status success
    public boolean isSuccess(UUID paymentId) throws IOException {
        return getPayment(paymentId).getStatus().isSuccess();
    }


}
