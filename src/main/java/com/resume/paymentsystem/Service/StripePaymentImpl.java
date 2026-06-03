package com.resume.paymentsystem.Service;


import com.resume.paymentsystem.DAO.Repository.PaymentRepository;
import com.resume.paymentsystem.DAO.Repository.TransactionRepository;
import com.resume.paymentsystem.DTO.CheckoutDTO;
import com.resume.paymentsystem.DTO.OrderRequest;

import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;

import com.stripe.model.checkout.Session;
import com.stripe.model.checkout.SessionCollection;
import com.stripe.model.tax.Registration;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;


import com.stripe.param.checkout.SessionCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


@Service
public class StripePaymentImpl  {

    Logger logger = LoggerFactory.getLogger(StripePaymentImpl.class);

    private final PaymentRepository paymentRepository;
    private final TransactionRepository transactionRepository;

    @Value("${stripe.webhook.secret}")
    private String webhookStripe;
    private final ObjectMapper mapper = new ObjectMapper();



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


    public Map<?,?> webhookEvent(String payload,String sigHeader) throws Exception {
        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookStripe);

        }catch (SignatureVerificationException e){
            logger.error(e.getMessage());
            throw new Exception("signature verification error");
        }

//        Map<String,Object> props = mapper.convertValue(event.getData(), Map.class);




        switch (event.getType()) {
//            case "session_created":{
//                logger.info("session_created");
//            }
            case "checkout.session.completed":{
                logger.info("checkout_completed");
                Session session = (Session) event.getDataObjectDeserializer().getObject()
                        .orElseThrow(()->new Exception("session is null"));
                Map<?,?> data =  session.getMetadata();
                if (session!=null){

//                    HashMap<?,?> data = new HashMap<>();



                    String sessionId = session.getId();
                    logger.info("session id is " + session.getId());
                    String customerEmail = session.getCustomerEmail();
                    logger.info("customerEmail is " + customerEmail);
                    Long totalAmount = session.getAmountTotal();
                    logger.info("totalAmount is " + totalAmount);
                    String currency = session.getCurrency();
                    logger.info("currency is " + currency);
                    String userId = session.getMetadata().get("user-id");
                    data.entrySet().stream().forEach(entry -> {
                        System.out.println(entry.getKey());
                        System.out.println(entry.getValue());
                    });
                    return data;
                }

            }
        }
        return null;
        }




}
