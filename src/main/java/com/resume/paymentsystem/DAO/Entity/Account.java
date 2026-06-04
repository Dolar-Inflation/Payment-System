//package com.resume.paymentsystem.DAO.Entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import org.hibernate.annotations.BatchSize;
//import org.hibernate.annotations.UuidGenerator;
////import org.springframework.security.core.GrantedAuthority;
////import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//
//@Entity
//@Data
//@BatchSize(size = 100)
//public class Account /*implements UserDetails */{
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private String name;
//    private String password;
//
//    @OneToMany
//    private Map<Payment,Transaction> transactions;
//
////    @Override
////    public Collection<? extends GrantedAuthority> getAuthorities() {
////        return List.of();
////    }
////
////    @Override
////    public String getUsername() {
////        return name;
////    }
//}
