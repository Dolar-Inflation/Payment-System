package com.resume.paymentsystem.Configuration;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import ru.deelter.yookassa.YooKassa;

@Configuration
@Slf4j
public class PaymentConfig {

    @Value("${stripe.api-key}")
    private String stripeSecretKey;



    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;


    }
}
