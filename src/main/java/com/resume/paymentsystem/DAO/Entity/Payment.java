package com.resume.paymentsystem.DAO.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

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
    private String uuid;
    private Long amount;
    private String currency;
    private String description;
    private String status;
    @Column(length = 2000)
    private String clientSecret;
    @Column(length = 2000)
    private String checkoutUrl;
}
