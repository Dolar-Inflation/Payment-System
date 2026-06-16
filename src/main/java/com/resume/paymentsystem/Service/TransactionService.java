package com.resume.paymentsystem.Service;

import com.resume.paymentsystem.DAO.Entity.Payment;
import com.resume.paymentsystem.DAO.Repository.AccountRepository;
import com.resume.paymentsystem.DAO.Repository.PaymentRepository;
import com.resume.paymentsystem.DTO.AccountDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionService implements TransactionServiceI  {

    private final PaymentRepository paymentRepository;
    private final AccountRepository accountRepository;

    public TransactionService(PaymentRepository paymentRepository, AccountRepository accountRepository) {
        this.paymentRepository = paymentRepository;
        this.accountRepository = accountRepository;
    }


    //Хэш таблица транзакций пользователя в виде UUID : CheckOutURL
    @Override
    public Map<UUID,String> getMapOfEntityes(HttpSession session) {

        AccountDTO account = (AccountDTO) session.getAttribute("account");

        if (account.name().isBlank()){
            throw new SessionAuthenticationException ("session expired or invalid");
        }

        List<Payment> transactionsList = paymentRepository.findByAccountId(accountRepository.findByName(account.name()).getId());
        if (transactionsList.isEmpty()){
            throw new IllegalStateException("transaction list is empty");
        }
        return transactionsList.stream().collect(Collectors.toMap(Payment::getUuid, Payment::getCheckoutUrl));
    }


    @Override
    public Payment findEntity(Object uniqId) {

        UUID uuid = (UUID) uniqId;
        return paymentRepository.findByUuid(uuid).orElseThrow(() -> new EntityNotFoundException("transaction not found"));
    }


}
