package com.resume.paymentsystem.Controllers;

import com.resume.paymentsystem.DAO.Entity.Account;
import com.resume.paymentsystem.DAO.Repository.AccountRepository;
import com.resume.paymentsystem.DTO.AccountDTO;
import com.resume.paymentsystem.Utility.CustomUserDetailsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/payment-system/")
public class AccountController {

    private final AccountRepository accountRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final ObjectMapper objectMapper;

    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final AuthenticationManager authenticationManager;

    public AccountController(AccountRepository accountRepository, CustomUserDetailsService customUserDetailsService, ObjectMapper objectMapper, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.accountRepository = accountRepository;
        this.customUserDetailsService = customUserDetailsService;
        this.objectMapper = objectMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }
@PostMapping("/register")
    public ResponseEntity<String> createAccount(HttpSession httpSession, @RequestBody AccountDTO accountDTO) {





        Account account = new Account();
        account.setName(accountDTO.name());
        account.setPassword(passwordEncoder.encode(accountDTO.password()));
        accountRepository.save(account);
        return ResponseEntity.ok("Account created");


    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AccountDTO accountDTO) {


        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(accountDTO.name(), accountDTO.password()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return ResponseEntity.ok("Login successful");
        }


        catch (AuthenticationException e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("Login failed");
    }



}
