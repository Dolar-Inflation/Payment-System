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
@RequiredArgsConstructor
public class StripePaymentImpl implements IPaymentService {

    Logger logger = LoggerFactory.getLogger(StripePaymentImpl.class);

    private final PaymentRepository paymentRepository;
    private final TransactionRepository transactionRepository;


    @Value("${stripe.api-key}")
    private String stripeApiKey;

    @Value("${stripe.webhook.secret}")
    private String stripeWebhookSecret;


    @Override
    public PaymentResponse createPayment(OrderRequest orderRequest, String idempotencyKey) {
        com.stripe.Stripe.apiKey = stripeApiKey;

        String intentKey = (idempotencyKey != null && !idempotencyKey.isBlank()) ? idempotencyKey + "-intent" : UUID.randomUUID().toString() + "-intent";

        Optional<Payment> existingOpt = paymentRepository.findByUuid(idempotencyKey);
        if (existingOpt.isPresent()) {
            Payment existingPayment = existingOpt.get();
            return PaymentResponse.builder()
                    .success(true)
                    .paymentId(String.valueOf(existingPayment.getId()))
                    .clientSecret(existingOpt.get().getClientSecret())
                    .message("Payment already exists for idempotency key: " + idempotencyKey)
                    .build();
        }

        Payment payment = Payment.builder()
                .uuid(intentKey)
                .amount(orderRequest.getAmount())
                .currency(orderRequest.getCurrency())
                .description(orderRequest.getDescription())
                .status("CREATED")
                .build();

        try {
            payment = paymentRepository.save(payment);
        }
        catch (DataIntegrityViolationException e) {
            payment = paymentRepository.findByUuid(intentKey).orElseThrow(() -> new RuntimeException("Failed to create or find payment with intentKey: " + intentKey));
        }


        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(orderRequest.getAmount())
                .setCurrency(orderRequest.getCurrency())
                .addPaymentMethodType("card")
                .setDescription(orderRequest.getDescription())
                .build();


        RequestOptions requestOptions = RequestOptions.builder()
                .setIdempotencyKey(intentKey)
                .build();

        try{
            PaymentIntent intent = PaymentIntent.create(params,requestOptions);

            Transaction tx = Transaction.builder()
                    .payment(payment)
                    .uuid(UUID.randomUUID().toString())
                    .gatewayTransactionId(intent.getId())
                    .gateway("Stripe")
                    .status(intent.getStatus())
                    .build();
            transactionRepository.save(tx);


            payment.setStatus(intent.getStatus());
            paymentRepository.save(payment);

            return PaymentResponse.builder()
                    .success(true)
                    .paymentId(String.valueOf(payment.getId()))
                    .clientSecret(intent.getClientSecret())
                    .message("Payment successfully created")
                    .build();
        }catch (StripeException e){
            payment.setStatus("FAILED");
            paymentRepository.save(payment);
            return PaymentResponse.builder()
                    .success(false)
                    .paymentId(String.valueOf(payment.getId()))
                    .message( e.getMessage())
                    .build();
        }


    }

    @Override
    public String createCheckoutSession(OrderRequest orderRequest, String idempotencyKey, String baseUrl) {
//        com.stripe.Stripe.apiKey = stripeApiKey;
//
//        String checkoutKey = (idempotencyKey != null && !idempotencyKey.isBlank())
//                ? idempotencyKey + "-checkout"
//                : UUID.randomUUID().toString() + "-checkout";
//
//
//        Optional<Payment> existingOpt = paymentRepository.findByUuid(checkoutKey);
//        if (existingOpt.isPresent()) {
//            Payment existingPayment = existingOpt.get();
//            if (existingPayment.getCheckoutUrl() != null && !existingPayment.getCheckoutUrl().isBlank()) {
//                return existingPayment.getCheckoutUrl();
//            }
//        }
//
//        Payment payment = Payment.builder()
//                .uuid(checkoutKey)
//                .amount(orderRequest.getAmount())
//                .currency(orderRequest.getCurrency())
//                .description(orderRequest.getDescription())
//                .status("CHECKOUT CREATED")
//                .build();
//
//        try {
//            paymentRepository.save(payment);
//        } catch (DataIntegrityViolationException e) {
//            payment = paymentRepository.findByUuid(checkoutKey).orElseThrow(()-> new RuntimeException("Failed to create or find payment with checkoutKey: " + checkoutKey));
//        }
//
return null;
    }

    @Override
    public String handleWebhook(String signatureHead, String payload) {
        return "";
    }

    @Override
    public PaymentResponse getPaymentStatus(String uuId) {
        return null;
    }

    @Override
    public PaymentResponse getPaymentStatus(Long id) {
        return null;
    }

    @Override
    public List<PaymentSummary> listAllPayments() {
        return List.of();
    }
}
