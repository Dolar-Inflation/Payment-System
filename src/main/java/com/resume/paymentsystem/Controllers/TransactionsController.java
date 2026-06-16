package com.resume.paymentsystem.Controllers;

import com.resume.paymentsystem.DAO.Entity.Account;
import com.resume.paymentsystem.DAO.Entity.Payment;
import com.resume.paymentsystem.DAO.Repository.AccountRepository;
import com.resume.paymentsystem.DAO.Repository.PaymentRepository;
import com.resume.paymentsystem.DTO.AccountDTO;
import com.resume.paymentsystem.DTO.PaymentDTO;
import com.resume.paymentsystem.Mappers.PaymentMapper;
import com.resume.paymentsystem.Service.TransactionServiceI;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/data")
public class TransactionsController {

    private final TransactionServiceI transactionService;
    private final PaymentMapper paymentMapper;

    public TransactionsController(TransactionServiceI transactionService, PaymentMapper paymentMapper) {

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

        Payment payment =((Payment) transactionService.findEntity(uuid));
//        PaymentDTO dto = paymentMapper.paymentToPaymentDTO(payment);

        return ResponseEntity.ok("Transaction data: " + payment.toString());
    }

    }






