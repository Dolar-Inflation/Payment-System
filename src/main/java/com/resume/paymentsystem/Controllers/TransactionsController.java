package com.resume.paymentsystem.Controllers;

import com.resume.paymentsystem.Mappers.PaymentMapper;
import com.resume.paymentsystem.Service.ITransactionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/data")
public class TransactionsController {

    private final ITransactionService transactionService;
    private final PaymentMapper paymentMapper;

    public TransactionsController(ITransactionService transactionService, PaymentMapper paymentMapper) {

        this.transactionService = transactionService;
        this.paymentMapper = paymentMapper;
    }





    @GetMapping("/userdata/transactions")
    public Map<UUID,String> getTransactions(HttpSession session) {

        return transactionService.getMapOfEntityes(session);
        }

//TODO рефакторинг всего по SOLID проверить SecurityFilterChain на дырявость проверка работы сессий redis с несколькими пользователями
//TODO fix n+1
    @GetMapping("/userdata/transactions/uuid/")
    public ResponseEntity<String> getTransactByUUid(@RequestParam UUID uuid) throws ChangeSetPersister.NotFoundException {

        return ResponseEntity.ok("Transaction data: " + transactionService.findEntity(uuid).toString());
    }

    }






