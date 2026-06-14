package com.resume.paymentsystem.Controllers;

import com.resume.paymentsystem.DAO.Entity.Account;
import com.resume.paymentsystem.DAO.Repository.AccountRepository;
import com.resume.paymentsystem.DTO.AccountDTO;
import com.resume.paymentsystem.Utility.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private final SecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();

    public AccountController(AccountRepository accountRepository, CustomUserDetailsService customUserDetailsService, ObjectMapper objectMapper, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.accountRepository = accountRepository;
        this.customUserDetailsService = customUserDetailsService;
        this.objectMapper = objectMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }
@PostMapping("/register")
    public ResponseEntity<String> createAccount( @RequestBody AccountDTO accountDTO) {





        Account account = new Account();
        account.setName(accountDTO.name());
        account.setPassword(passwordEncoder.encode(accountDTO.password()));

//    Authentication authentication = authenticationManager.authenticate(
//            new UsernamePasswordAuthenticationToken(account.getUsername(), account.getPassword()));
//    SecurityContextHolder.getContext().setAuthentication(authentication);

        accountRepository.save(account);
        return ResponseEntity.ok("Account created" );


    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AccountDTO accountDTO,
                                        HttpServletRequest request,
                                        HttpServletResponse response,HttpSession session) {


        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(accountDTO.name(), accountDTO.password()));

//            SecurityContextHolder.getContext().setAuthentication(authentication);
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            securityContextRepository.saveContext(context, request, response);
            session.setAttribute("account", accountDTO);

            return ResponseEntity.ok("Login successful"+authentication.getDetails());
        }


        catch (AuthenticationException e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("Login failed");
    }
    @GetMapping("/userdata")
    public String getUserData(HttpSession session) {
        AccountDTO account = (AccountDTO) session.getAttribute("account");

        return account.name();
    }
    @GetMapping("/userdata/all")
    public Map<Long,String> getAllUserData() {
        List<Account> data = accountRepository.findAll();

        data.forEach(System.out::println);
        return data.stream().collect(Collectors.toMap(Account::getId,Account::getName));
    }



}
