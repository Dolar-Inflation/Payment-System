package com.resume.paymentsystem.Service;


import com.resume.paymentsystem.DAO.Repository.PaymentRepository;
import com.resume.paymentsystem.DAO.Repository.TransactionRepository;
import com.resume.paymentsystem.DTO.CheckoutDTO;
import com.resume.paymentsystem.DTO.OrderRequest;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCreateParams;


import com.stripe.param.checkout.SessionCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;



@Service
public class StripePaymentImpl  {

    Logger logger = LoggerFactory.getLogger(StripePaymentImpl.class);

    private final PaymentRepository paymentRepository;
    private final TransactionRepository transactionRepository;




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
    public String createSessionLink(CheckoutDTO checkoutDTO) throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
                .setSuccessUrl("https://example.com/success")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                        .setUnitAmount(Long.parseLong(checkoutDTO.getPrice()))
                                        .setCurrency("USD")
                                        .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName("order")
                                                .build()
                                                        )
                                        .build())
                                .setQuantity(checkoutDTO.getQuantity())
                                .build()
                )
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .build();
        Session session = Session.create(params);






    return session.getUrl();
    }




}
