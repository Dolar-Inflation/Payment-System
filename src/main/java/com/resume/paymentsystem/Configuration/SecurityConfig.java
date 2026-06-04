//package com.resume.paymentsystem.Configuration;
//
//import com.resume.paymentsystem.DAO.Repository.AccountRepository;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//    return new BCryptPasswordEncoder(10);
//}
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    http.csrf(csrf -> csrf.disable());
//        http.authorizeHttpRequests(authorizeRequests ->
//                authorizeRequests.requestMatchers(HttpMethod.POST,
//                        "/payment-system/**",
//                        "/api/payments/checkout/**").permitAll()
//                .anyRequest().authenticated()
//
//
//
//
//        );
//        return http.build();
//}
//
//
//}
