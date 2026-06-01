package com.resume.paymentsystem.Controllers;

import com.resume.paymentsystem.DTO.CheckoutDTO;
import com.resume.paymentsystem.DTO.OrderRequest;
import com.resume.paymentsystem.DTO.PaymentResponse;
import com.resume.paymentsystem.Service.StripePaymentImpl;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
