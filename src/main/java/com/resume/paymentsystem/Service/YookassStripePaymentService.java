package com.resume.paymentsystem.Service;

import com.resume.paymentsystem.DTO.OrderRequest;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import okhttp3.OkHttpClient;
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
import tools.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.util.Map;

@Service
public class YookassStripePaymentService implements IYookassaPaymentService {

    @Value("${UCASSA_API_SECRET_KEY}")
    private String ucassaApiSecretKey;
    private static final YooKassa YOO_KASSA = YooKassa.create(
            1388972, "test__k65ZNU2IPJpETbUZYWJt3sI_XhF2C5nAvVGVUxGRF8"

    );
//    private final YooKassa yooKassa = YooKassa.create(1388972, ucassaApiSecretKey);

//    private YooKassa yooKassa;

//    @PostConstruct
//    public void init() {
//        this.yooKassa = YooKassa.create(1388972, ucassaApiSecretKey);
//        System.out.println("yooKassa initialized: " + (this.yooKassa != null));
//    }
    @Override
    public  Payment createPayment() throws IOException {
        return YOO_KASSA.createPayment(PaymentCreateData.builder()
                .amount(Amount.from(100, Currency.RUB))
                .description("Buy a coffee")
                .redirect("https://github.com/deelter")
                .capture(true)
                .build()
        );
    }

    @Override
    public Webhook createWebhookRequest() throws IOException {
        YooKassaEvent yooKassaEvent = YooKassaEvent.getByName("payment.succeeded");
        WebhookCreateData webhookCreateData = new WebhookCreateData(yooKassaEvent,"https://github.com/deelter");

        return YOO_KASSA.createWebhook(webhookCreateData);
    }

    @Override
    public YooRequestUrls createYooRequestUrls() {
        return null;
    }



}
