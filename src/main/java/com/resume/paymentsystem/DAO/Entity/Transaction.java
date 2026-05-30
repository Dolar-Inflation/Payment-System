package com.resume.paymentsystem.DAO.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uuid;
    private String gatewayTransactionId;
    private String gateway;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Payment payment;
}