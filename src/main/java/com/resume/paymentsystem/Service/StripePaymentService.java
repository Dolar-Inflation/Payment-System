package com.resume.paymentsystem.Service;


import com.resume.paymentsystem.DAO.Entity.Account;
import com.resume.paymentsystem.DAO.Repository.AccountRepository;
import com.resume.paymentsystem.DAO.Repository.PaymentRepository;
import com.resume.paymentsystem.DTO.AccountDTO;
import com.resume.paymentsystem.DTO.CheckoutDTO;
import com.resume.paymentsystem.DTO.OrderRequest;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;

import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;


import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionUpdateParams;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.Objects;


@Service
public class StripePaymentService implements IPaymentService  {

    Logger logger = LoggerFactory.getLogger(StripePaymentService.class);

    private final PaymentRepository paymentRepository;

    private final AccountRepository accountRepository;

    @Value("${stripe.webhook.secret}")
    private String webhookStripe;
    private final ObjectMapper mapper = new ObjectMapper();



    public StripePaymentService(PaymentRepository paymentRepository, AccountRepository accountRepository) {
        this.paymentRepository = paymentRepository;
        this.accountRepository = accountRepository;
    }




    @Override
    public String createSessionLink(Object obj, HttpSession httpSession) throws StripeException {
        CheckoutDTO checkoutDTO = (CheckoutDTO) obj;
        AccountDTO accountDTO = (AccountDTO) httpSession.getAttribute("account");
        if (accountDTO == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build().toString();
        }
        Account account = accountRepository.findByName(accountDTO.name());
        SessionCreateParams params = SessionCreateParams.builder()
                .setClientReferenceId(String.valueOf(account.getId()))
                .setSuccessUrl("https://example.com/success")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                        .setUnitAmount(Long.parseLong(checkoutDTO.getPrice()))
                                        .setCurrency(checkoutDTO.getCurrency())
                                        .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName("order")
                                                .build()
                                        )
                                        .build())
                                .putMetadata("accountId", String.valueOf(account.getId()))
                                .setQuantity(checkoutDTO.getQuantity())
                                .build()
                )
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .build();
        Session session = Session.create(params);
        SessionUpdateParams updateParams = SessionUpdateParams.builder()
                .putMetadata("checkout_url", session.getUrl())
                .build();
        session.update(updateParams);
        return session.getUrl();

    }

    @Override
    public Map webhookEvent(String payload, String sigHeader) throws Exception {
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookStripe);
        } catch (SignatureVerificationException e) {
            logger.error(e.getMessage());
            throw new Exception("signature verification error");
        }
        if (Objects.equals(event.getType(), "checkout.session.completed")) {
            logger.info("checkout_completed");
            Session session = (Session) event.getDataObjectDeserializer().getObject()
                    .orElseThrow(() -> new Exception("session is null"));
            Map<String, Object> dataMap = mapper.convertValue(session, Map.class);
            Long clientReferenceId = Long.valueOf(session.getClientReferenceId());
            dataMap.entrySet().stream().forEach(entry -> {
                System.out.println("Key - " + entry.getKey());
                System.out.println("Value - " + entry.getValue());
            });
            dataMap.put("accountId", clientReferenceId);
            return dataMap;
        }
        else {
            logger.info("Webhook received, but event type is ignored (not checkout.session.completed).");
            return null;
        }
    }

    @Override
    public PaymentIntent createPayment(OrderRequest orderRequest) throws StripeException {

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(orderRequest.getAmount())
                .setCurrency(orderRequest.getCurrency())
                .setDescription(orderRequest.getDescription())
//                .setPaymentMethod("card")
                .addPaymentMethodType("card")


                .build();



        return PaymentIntent.create(params);

    }
//    //TODO Добавить сериализацию и десериализацию на Account и AccountDTO
//    //TODO исправить n+1 на авторизации
//    public String createSessionLink(CheckoutDTO checkoutDTO, HttpSession httpSession) throws StripeException {
//        AccountDTO accountDTO = (AccountDTO) httpSession.getAttribute("account");
//
////        Map<?,?> accd = (Map<?, ?>) httpSession.getAttribute("account");
////        AccountDTO accountDTO = (AccountDTO) accd.get("account");
//        if (accountDTO == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build().toString();
//        }
//        Account account = accountRepository.findByName(accountDTO.name());
//
//
//
//        SessionCreateParams params = SessionCreateParams.builder()
//                .setClientReferenceId(String.valueOf(account.getId()))
//                .setSuccessUrl("https://example.com/success")
//                .addLineItem(
//                        SessionCreateParams.LineItem.builder()
//                                .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
//                                        .setUnitAmount(Long.parseLong(checkoutDTO.getPrice()))
//                                        .setCurrency(checkoutDTO.getCurrency())
//
//
//
//                                        .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
//                                                .setName("order")
//                                                .build()
//                                                        )
//                                        .build())
//                                .putMetadata("accountId", String.valueOf(account.getId()))
//                                .setQuantity(checkoutDTO.getQuantity())
//                                .build()
//                )
//                .setMode(SessionCreateParams.Mode.PAYMENT)
//                .build();
//        Session session = Session.create(params);
//
//        SessionUpdateParams updateParams = SessionUpdateParams.builder()
//                .putMetadata("checkout_url", session.getUrl())
//                .build();
//
//
//
//
//    session.update(updateParams);
//
//    return session.getUrl();
//
//    }
//
//
//
//
//    public Map<?,?> webhookEvent(String payload,String sigHeader/*,@RequestHeader HttpHeaders headers*/) throws Exception {
//        Event event;
////        AccountDTO accountDTO = (AccountDTO) httpSession.getAttribute("account");
//        try {
//
//            event = Webhook.constructEvent(payload, sigHeader, webhookStripe);
//
//        } catch (SignatureVerificationException e) {
//            logger.error(e.getMessage());
//            throw new Exception("signature verification error");
//        }
////        Map<String,Object> props = mapper.convertValue(event.getData(), Map.class);
////        switch (event.getType()) {
////            case "session_created":{
////                logger.info("session_created");
////            }
////            case "checkout.session.completed":
////
//        if (Objects.equals(event.getType(), "checkout.session.completed")) {
//            logger.info("checkout_completed");
//            Session session = (Session) event.getDataObjectDeserializer().getObject()
//                    .orElseThrow(() -> new Exception("session is null"));
//
//            Map<String, Object> dataMap = mapper.convertValue(session, Map.class);
//            Long clientReferenceId = Long.valueOf(session.getClientReferenceId());
//            dataMap.entrySet().stream().forEach(entry -> {
//                System.out.println("Key - " + entry.getKey());
//                System.out.println("Value - " + entry.getValue());
//            });
//            dataMap.put("accountId", clientReferenceId/*accountRepository.findById(Math.toIntExact(clientReferenceId))*/);
//            return dataMap;
//
//
//        }
////        }
//        else {
//            logger.info("Webhook received, but event type is ignored (not checkout.session.completed).");
//            return null;
//        }
//    }
//
//
//

}
