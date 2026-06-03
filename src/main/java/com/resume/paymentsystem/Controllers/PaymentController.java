package com.resume.paymentsystem.Controllers;

import com.resume.paymentsystem.DTO.CheckoutDTO;
import com.resume.paymentsystem.DTO.OrderRequest;
import com.resume.paymentsystem.DTO.PaymentResponse;
import com.resume.paymentsystem.Service.StripePaymentImpl;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import lombok.extern.slf4j.Slf4j;
import org.junit.platform.engine.support.discovery.SelectorResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.stripe.service.checkout.*;

@RestController
@RequestMapping("/api/payments")
@Slf4j
public class PaymentController {

    private final StripePaymentImpl stripePayment;


    public PaymentController(StripePaymentImpl stripePayment) {
        this.stripePayment = stripePayment;
    }

    @PostMapping("/create")
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody OrderRequest orderRequest) {

        try {
            PaymentIntent intent = stripePayment.createPayment(orderRequest);


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
        }



    }
    @PostMapping("checkout")
    public ResponseEntity<?> checkoutPayment(@RequestBody CheckoutDTO checkoutDTO) throws StripeException {

        String url = stripePayment.createSessionLink(checkoutDTO);
        return ResponseEntity.ok(url);

    }
    @PostMapping("webhook")
    public ResponseEntity<?> webhook(@RequestBody String payload,@RequestHeader HttpHeaders headers) throws Exception {


        String headersHeaderString = headers.getFirst("Stripe-Signature");
        if (headersHeaderString==null || headersHeaderString.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        stripePayment.webhookEvent(payload,headersHeaderString);
        log.info("!!! Webhook event received {}",HttpStatus.OK);



        return new ResponseEntity<>("Webhook success",HttpStatus.OK);

    }

}
