package com.resume.paymentsystem.DAO.Repository;

import com.resume.paymentsystem.DAO.Entity.Account;
import com.resume.paymentsystem.DAO.Entity.Payment;
import com.resume.paymentsystem.DTO.AccountDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    
    Optional<Payment> findByUuid(UUID uuid);

    List<Payment> findByAccount(Account account);

    List<Payment> findByAccountId(Long accountId);
}
