package com.resume.paymentsystem.Controllers;

import com.google.gson.Gson;
import com.resume.paymentsystem.DAO.Entity.Account;
import com.resume.paymentsystem.DAO.Entity.Payment;
import com.resume.paymentsystem.DAO.Repository.AccountRepository;
import com.resume.paymentsystem.DAO.Repository.PaymentRepository;
import com.resume.paymentsystem.DTO.CheckoutDTO;
import com.resume.paymentsystem.DTO.OrderRequest;
import com.resume.paymentsystem.DTO.PaymentResponse;
import com.resume.paymentsystem.Service.Dud;
import com.resume.paymentsystem.Service.Pay;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.deelter.yookassa.data.impl.Webhook;
import ru.deelter.yookassa.data.impl.requests.PaymentCaptureData;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@Slf4j
public class PaymentController {

//    private final StripePaymentServiceImpl stripePayment;
    private final PaymentRepository paymentRepository;
    private final AccountRepository accountRepository;
    private final Pay pay;
    private final Dud dud;
    private final ObjectMapper objectMapper;


    public PaymentController(/*StripePaymentServiceImpl stripePayment,*/ PaymentRepository paymentRepository, AccountRepository accountRepository, Pay pay, Dud dud, ObjectMapper objectMapper) {
//        this.stripePayment = stripePayment;
        this.paymentRepository = paymentRepository;
        this.accountRepository = accountRepository;
        this.pay = pay;
        this.dud = dud;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody OrderRequest orderRequest) {

        try {
            PaymentIntent intent = (PaymentIntent) pay.createPayment(orderRequest);


            PaymentResponse response = new PaymentResponse(intent.getId(),
                    intent.getClientSecret(),
                    intent.getDescription(),

                    intent.getAmount(),
                    intent.getCurrency(),
                    intent.getStatus());

            return ResponseEntity.ok(response);

        } catch (StripeException e) {
//            throw new RuntimeException(e);
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
    @PostMapping("checkout")
    public ResponseEntity<?> checkoutPayment(@RequestBody CheckoutDTO checkoutDTO, HttpSession session) throws StripeException, Exception {
        try {
            String url = pay.createSessionLink(checkoutDTO, session);
            return ResponseEntity.ok(url);
        }
        catch (StripeException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }



    }

    @PostMapping("/paymentYookassa")
    public String paymentYookassaPayment() throws IOException {
//       ru.deelter.yookassa.data.impl.Payment payment = pay.createPayment();
//       pay.createWebhookRequest();
       ru.deelter.yookassa.data.impl.Payment data=  dud.createPayment();
       Gson gson = new Gson();
        log.info("Payment Yookassa created");
        return gson.toJson(data);
    }




    @PostMapping("webhook-yokassa")
    public ResponseEntity<?> yookassaWebhook(@RequestBody String payload) throws IOException {
        Webhook webhook = pay.createWebhookRequest();
        return ResponseEntity.ok(payload + "!!!!!!!!!!!!!!!!!!!" + webhook);
    }



    @PostMapping("webhook-stripe")
    public ResponseEntity<?> webhook(@RequestBody String payload, @RequestHeader HttpHeaders headers, Principal principal) throws Exception {


        String headersHeaderString = headers.getFirst("Stripe-Signature");
        if (headersHeaderString==null || headersHeaderString.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Map<?,?> data = pay.webhookEvent(payload,headersHeaderString);



        if (data == null) {
            return new ResponseEntity<>("Event received and skipped", HttpStatus.OK);
        }
//        Account account1 = (Account) httpSession.getAttribute("accountId");
//        Account account =  accountRepository.findById((Integer) data.get("accountId")).orElseThrow(() -> new Exception("Account not is null"));
        Long AccId = (Long) data.get("accountId");
        Account account =  accountRepository.findById(Math.toIntExact(AccId)).orElseThrow(() -> new Exception("Account not is null"));

        log.info("!!! Account received {}",account);

        Payment payment = new Payment();
        payment.setAmount((Long) data.get("amountTotal"));
        payment.setCurrency((String) data.get("currency"));
        payment.setStatus((String) data.get("status"));
        payment.setCheckoutUrl(data.get("metadata").toString());
        payment.setAccount(account);


        paymentRepository.save(payment);
//        accountRepository.save(principal);


        return new ResponseEntity<>("Webhook success",HttpStatus.OK);

    }



}
