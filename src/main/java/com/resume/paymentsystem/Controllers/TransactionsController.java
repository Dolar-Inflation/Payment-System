package com.resume.paymentsystem.Controllers;

import com.resume.paymentsystem.DAO.Entity.Account;
import com.resume.paymentsystem.DAO.Entity.Payment;
import com.resume.paymentsystem.DAO.Repository.AccountRepository;
import com.resume.paymentsystem.DAO.Repository.PaymentRepository;
import com.resume.paymentsystem.DTO.AccountDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.crossstore.ChangeSetPersister;
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

    private final PaymentRepository paymentRepository;
    private final AccountRepository accountRepository;

    public TransactionsController(PaymentRepository paymentRepository, AccountRepository accountRepository) {
        this.paymentRepository = paymentRepository;
        this.accountRepository = accountRepository;
    }





    @GetMapping("/userdata/transactions")
    public Map<UUID,String> getTransactions(HttpSession session) {
        AccountDTO account = (AccountDTO) session.getAttribute("account");
        List<Payment> transactionsList = paymentRepository.findByAccountId(accountRepository.findByName(account.name()).getId());

        Map<UUID,String> map = transactionsList.stream().collect(Collectors.toMap(
                Payment::getUuid,
                Payment::getCheckoutUrl
        ));

        map.entrySet().forEach(System.out::println);
        return map;
        }

//TODO рефакторинг всего по SOLID проверить SecurityFilterChain на дырявость проверка работы сессий redis с несколькими пользователями
//TODO fix n+1
    @GetMapping("/userdata/transactions/")
    public Payment getTransactByUUid(@RequestParam UUID uuid) throws ChangeSetPersister.NotFoundException {


        return paymentRepository.findByUuid(uuid).orElseThrow(() -> new EntityNotFoundException("transaction not found"));

    }

    }






