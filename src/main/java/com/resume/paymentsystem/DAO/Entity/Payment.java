package com.resume.paymentsystem.DAO.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@BatchSize(size = 100)
public class Payment {
    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @UuidGenerator
    @Column(columnDefinition = "VARCHAR(36)")
    private UUID uuid;
    private Long amount;
    private String currency;
    private String status;
    @Column(length = 2000)
    private String checkoutUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;
}
