package com.resume.paymentsystem.DAO.Repository;

import com.resume.paymentsystem.DAO.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    
    Optional<Payment> findByUuid(String uuid);
}
