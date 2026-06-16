package com.resume.paymentsystem.Controllers;

import com.resume.paymentsystem.DAO.Entity.Account;
import com.resume.paymentsystem.DAO.Entity.Payment;
import com.resume.paymentsystem.DAO.Repository.AccountRepository;
import com.resume.paymentsystem.DAO.Repository.PaymentRepository;
import com.resume.paymentsystem.DTO.CheckoutDTO;
import com.resume.paymentsystem.DTO.OrderRequest;
import com.resume.paymentsystem.DTO.PaymentResponse;
import com.resume.paymentsystem.Service.IPaymentService;
import com.resume.paymentsystem.Service.StripePaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@Slf4j
public class PaymentController {

//    private final StripePaymentService stripePayment;
    private final PaymentRepository paymentRepository;
    private final AccountRepository accountRepository;
    private final IPaymentService stripePaymentService;


    public PaymentController(/*StripePaymentService stripePayment,*/ PaymentRepository paymentRepository, AccountRepository accountRepository, IPaymentService stripePaymentService) {
//        this.stripePayment = stripePayment;
        this.paymentRepository = paymentRepository;
        this.accountRepository = accountRepository;
        this.stripePaymentService = stripePaymentService;
    }

    @PostMapping("/create")
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody OrderRequest orderRequest) {

        try {
            PaymentIntent intent = (PaymentIntent) stripePaymentService.createPayment(orderRequest);


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
            String url = stripePaymentService.createSessionLink(checkoutDTO, session);
            return ResponseEntity.ok(url);
        }
        catch (StripeException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }



    }
    @PostMapping("webhook")
    public ResponseEntity<?> webhook(@RequestBody String payload, @RequestHeader HttpHeaders headers, Principal principal) throws Exception {


        String headersHeaderString = headers.getFirst("Stripe-Signature");
        if (headersHeaderString==null || headersHeaderString.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Map<?,?> data = stripePaymentService.webhookEvent(payload,headersHeaderString);



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
