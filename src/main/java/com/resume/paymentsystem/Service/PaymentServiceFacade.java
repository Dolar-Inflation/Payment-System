package com.resume.paymentsystem.Service;

import com.resume.paymentsystem.DTO.OrderRequest;
import com.stripe.model.PaymentIntent;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import ru.deelter.yookassa.data.impl.Payment;
import ru.deelter.yookassa.data.impl.Webhook;
import ru.deelter.yookassa.utils.YooRequestUrls;

import java.io.IOException;
import java.util.Map;

@Service
public class PaymentServiceFacade implements Pay {

    private final StripePaymentServiceImpl stripeService;
    private final YookassStripePaymentService yookassaService;

    public PaymentServiceFacade(StripePaymentServiceImpl stripeService,
                                YookassStripePaymentService yookassaService) {
        this.stripeService = stripeService;
        this.yookassaService = yookassaService;
    }

    @Override
    public PaymentIntent createPayment(OrderRequest orderRequest) throws Exception {
        return stripeService.createPayment(orderRequest);
    }

    @Override
    public String createSessionLink(Object obj, HttpSession session) throws Exception {
        return stripeService.createSessionLink(obj, session);
    }

    @Override
    public Map webhookEvent(String payload, String sigHeader) throws Exception {
        return stripeService.webhookEvent(payload, sigHeader);
    }

    @Override
    public Payment createPayment() throws IOException {
        return yookassaService.createPayment();
    }

    @Override
    public Webhook createWebhookRequest() throws IOException {
        return yookassaService.createWebhookRequest();
    }

    @Override
    public YooRequestUrls createYooRequestUrls() {
        return null;
    }

}
