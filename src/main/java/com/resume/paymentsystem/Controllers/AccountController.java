package com.resume.paymentsystem.Controllers;

import com.resume.paymentsystem.DTO.AccountDTO;
import com.resume.paymentsystem.Service.AccountServiceI;
import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/payment-system/")
/** Класс который представляет взаимодействие с аккаунтом пользователя <p>Позволяет авторизовать и
 * аутентифицировать пользователя создаёт сессию и записывает её в redis в виде Account
 * также есть эндпоинт для возвращения Account.name</p>
 *
 * @author Siutsou Mikhail
 * @version 0.03
 * @since 0.01
 * */
public class AccountController {


//    private final AccountService accountService;
    private final AccountServiceI accountService;
    @Autowired
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();

    public AccountController(AccountServiceI accountService, AuthenticationManager authenticationManager) {
        this.accountService = accountService;
        this.authenticationManager = authenticationManager;
    }

/** Регистрирует пользователя(записывая его в бд) пароль хешируется
 * @param accountDTO логин и пароль не должны быть isBlank и isEmpty также логин должен быть уникальным
 * @throws  EntityExistsException если сущность существует
 * @throws IllegalArgumentException если логин или пароль isBlank == true*/
@PostMapping("/register")
    public ResponseEntity<String> createAccount( @RequestBody AccountDTO accountDTO) {
        try {
            return accountService.createEntity(accountDTO);}
        catch (EntityExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AccountDTO accountDTO,
                                        HttpServletRequest request,
                                        HttpServletResponse response,HttpSession session) {

        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(accountDTO.name(), accountDTO.password()));

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            securityContextRepository.saveContext(context, request, response);
            session.setAttribute("account", accountDTO);

            return ResponseEntity.ok("Login successful "+accountDTO.name());
        }


        catch (AuthenticationException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Login failed "+accountDTO.name());
        }

    }
    @GetMapping("/userdata")
    public String getUserData(HttpSession session) {
        AccountDTO account = (AccountDTO) session.getAttribute("account");
        return account.name();
    }

    @GetMapping("/userdata/all")
    public Map<Long,String> getAllUserData() {


        return accountService.getMapOfEntityes();
    }



}
