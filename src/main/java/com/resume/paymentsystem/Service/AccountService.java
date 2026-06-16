package com.resume.paymentsystem.Service;

import com.resume.paymentsystem.DAO.Entity.Account;
import com.resume.paymentsystem.DAO.Repository.AccountRepository;
import com.resume.paymentsystem.DTO.AccountDTO;
import com.resume.paymentsystem.Mappers.AccountMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AccountService implements AccountServiceI {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository, AccountMapper accountMapper, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Map<Long,String> getMapOfEntityes() {
        try {
            List<Account> data = accountRepository.findAll();
            if (data.isEmpty()) {
                throw new EntityNotFoundException("list of accounts is empty");
            }
            return data.stream().collect(Collectors.toMap(Account::getId, Account::getName));
        }catch (NullPointerException e){
            throw new EntityNotFoundException("list is null");
        }
    }

    @Override
    public String findEntity(Object uniqId) {
        Integer id = (Integer) uniqId;
        AccountDTO accountDTO = accountMapper.accountToAccountDTO(accountRepository.findById(id).
                orElseThrow(()-> new EntityNotFoundException( "по этому " + id.toString() + " сущность не найдена")));

        return accountDTO.name();
    }

    @Override
    public ResponseEntity<String> createEntity(Object dto) {
        AccountDTO accountDTO = (AccountDTO) dto;
        Account account = accountMapper.accountDTOToAccount(accountDTO);
        if (!accountRepository.existsAccountsByName(account.getName())) {
            account.setPassword(passwordEncoder.encode(accountDTO.password()));
            account.setName(accountDTO.name());
            accountRepository.save(account);
            return ResponseEntity.ok("Entity created");
        }
        else {
            return ResponseEntity.badRequest().body("entity already exists");
        }
    }


}
