package com.resume.paymentsystem.DAO.Repository;

import com.resume.paymentsystem.DAO.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
