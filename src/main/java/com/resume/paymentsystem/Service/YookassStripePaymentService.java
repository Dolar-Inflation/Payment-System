package com.resume.paymentsystem.Service;

import com.resume.paymentsystem.DTO.OrderRequest;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.deelter.yookassa.YooKassa;
import ru.deelter.yookassa.data.impl.Amount;
import ru.deelter.yookassa.data.impl.Currency;
import ru.deelter.yookassa.data.impl.Payment;
import ru.deelter.yookassa.data.impl.Webhook;
import ru.deelter.yookassa.data.impl.requests.PaymentCreateData;
import ru.deelter.yookassa.data.impl.requests.WebhookCreateData;
import ru.deelter.yookassa.data.impl.requests.YooRequest;
import ru.deelter.yookassa.events.YooKassaEvent;
import ru.deelter.yookassa.requests.webhooks.WebhookCreateRequest;
import ru.deelter.yookassa.utils.YooRequestUrls;

import java.io.IOException;
import java.util.Map;

@Service
public class YookassStripePaymentService implements IYookassaPaymentService {

    @Value("${UCASSA_API_SECRET_KEY}")
    private String ucassaApiSecretKey;

//    private final YooKassa yooKassa = YooKassa.create(1388972, ucassaApiSecretKey);

    private YooKassa yooKassa;

    @PostConstruct
    public void init() {
        this.yooKassa = YooKassa.create(1388972, ucassaApiSecretKey);
    }
    @Override
    public Payment createPayment() throws IOException {


        return yooKassa.createPayment(PaymentCreateData.builder()
                .amount(Amount.from(100, Currency.RUB))
                .description("Buy a coffee")
                .capture(true)
                .build()
        );
    }

    @Override
    public Webhook createWebhookRequest() throws IOException {
        YooKassaEvent yooKassaEvent = YooKassaEvent.getByName("payment.succeeded");
        WebhookCreateData webhookCreateData = new WebhookCreateData(yooKassaEvent,"192.168.10.87/api/payments/");

        return yooKassa.createWebhook(webhookCreateData);
    }

    @Override
    public YooRequestUrls createYooRequestUrls() {
        return null;
    }

}
