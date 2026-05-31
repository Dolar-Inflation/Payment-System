package com.resume.paymentsystem.Service;

import com.resume.paymentsystem.DAO.Entity.Payment;
import com.resume.paymentsystem.DAO.Entity.Transaction;
import com.resume.paymentsystem.DAO.Repository.PaymentRepository;
import com.resume.paymentsystem.DAO.Repository.TransactionRepository;
import com.resume.paymentsystem.DTO.OrderRequest;
import com.resume.paymentsystem.DTO.PaymentResponse;
import com.resume.paymentsystem.DTO.PaymentSummary;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.billingportal.SessionCreateParams;
import lombok.RequiredArgsConstructor;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StripePaymentImpl  {

    Logger logger = LoggerFactory.getLogger(StripePaymentImpl.class);

    private final PaymentRepository paymentRepository;
    private final TransactionRepository transactionRepository;


    @Value("${stripe.api-key}")
    private String stripeApiKey;

    @Value("${stripe.webhook.secret}")
    private String stripeWebhookSecret;

    public StripePaymentImpl(PaymentRepository paymentRepository, TransactionRepository transactionRepository) {
        this.paymentRepository = paymentRepository;
        this.transactionRepository = transactionRepository;
    }


    public PaymentIntent createPayment(OrderRequest orderRequest) throws StripeException {

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(orderRequest.getAmount())
                .setCurrency(orderRequest.getCurrency())
                .setDescription(orderRequest.getDescription())
//                .setPaymentMethod("card")
                .addPaymentMethodType("card")
//                .setPaymentMethod("card")

                .build();


        return PaymentIntent.create(params);

    }




}
