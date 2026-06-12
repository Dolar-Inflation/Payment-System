package com.resume.paymentsystem.Controllers;

import com.resume.paymentsystem.DAO.Entity.Account;
import com.resume.paymentsystem.DAO.Repository.AccountRepository;
import com.resume.paymentsystem.DAO.Repository.PaymentRepository;
import com.resume.paymentsystem.DTO.AccountDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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


    @GetMapping("/userdata")
    public String getUserData(HttpSession session) {
        AccountDTO account = (AccountDTO) session.getAttribute("account");

        return account.name();
    }
    @GetMapping("/userdata/all")
    public Map<Long,String> getAllUserData() {
        List<Account> data = accountRepository.findAll();

        data.forEach(System.out::println);
        return data.stream().collect(Collectors.toMap(Account::getId,Account::getName));
    }
//    @GetMapping("userdata/transactions")

}
