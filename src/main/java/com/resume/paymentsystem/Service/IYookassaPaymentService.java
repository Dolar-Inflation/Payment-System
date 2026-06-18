package com.resume.paymentsystem.Service;

import ru.deelter.yookassa.data.impl.Payment;
import ru.deelter.yookassa.data.impl.requests.YooRequest;
import ru.deelter.yookassa.requests.webhooks.WebhookCreateRequest;
import ru.deelter.yookassa.utils.YooRequestUrls;

public interface IYookassaPaymentService {

    public Payment createPayment();
    public WebhookCreateRequest createWebhookRequest();
    public YooRequestUrls createYooRequestUrls();

}
