package com.resume.paymentsystem.DAO.Repository;

import com.resume.paymentsystem.DAO.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
