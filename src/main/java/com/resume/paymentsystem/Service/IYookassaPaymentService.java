package com.resume.paymentsystem.Service;

import ru.deelter.yookassa.data.impl.Payment;
import ru.deelter.yookassa.data.impl.Webhook;
import ru.deelter.yookassa.data.impl.requests.YooRequest;
import ru.deelter.yookassa.requests.webhooks.WebhookCreateRequest;
import ru.deelter.yookassa.utils.YooRequestUrls;

import java.io.IOException;

public interface IYookassaPaymentService {

    public Payment createPayment() throws IOException;
    public Webhook createWebhookRequest() throws IOException;
    public YooRequestUrls createYooRequestUrls();

}
