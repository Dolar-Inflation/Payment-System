package com.resume.paymentsystem.Configuration;

import com.resume.paymentsystem.DAO.Repository.AccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {



    @Bean
    public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(10);
}
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    http.csrf(csrf -> csrf.disable());
//
//        http.securityContext(context -> context
//                .securityContextRepository(new HttpSessionSecurityContextRepository())
//        );
//
//        http.authorizeHttpRequests(authorizeRequests ->
//                authorizeRequests.requestMatchers(
//
//
//                                "/api/payments/webhook",
//                                "/payment-system/login",
//                                "/payment-system/register",
//                                "/swagger-ui/**",
//                                "/v3/api-docs/**",
//                                "/swagger-ui.html",
//                                "/api-docs",
//                                "/docs/**",
//                                "/api-docs/**").permitAll()
//
//                        .requestMatchers(
//                        "/api/payments/checkout",
//                        "/api/payments/webhook-stripe",
//                        "/payment-system/checkout",
//                        "/api/payments/webhook-yokassa",
//                        "/api/payments/webhook-yokassa/**",
//                        "/api/payments/paymentYookassa",
//                        "/api/data/**",
//                        "/api/data/userdata/",
//                        "/api/data/userdata/transactions",
//                        "/api/data/userdata/transactions/uuid/**").authenticated()
//                        .anyRequest().authenticated());
//
//
//
//
//
//
//
//
//
//        return http.build();
//}
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // 1. Полностью отключаем CSRF защиту
    http.csrf(csrf -> csrf.disable());

    // 2. Отключаем обработку заголовков базовой авторизации (Basic Auth) и стандартной формы входа
    http.httpBasic(basic -> basic.disable());
    http.formLogin(form -> form.disable());

    // 3. РАЗРЕШАЕМ ВСЁ: меняем .anyRequest().authenticated() на .permitAll()
    http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
            .anyRequest().permitAll()
    );

    return http.build();
}


}
