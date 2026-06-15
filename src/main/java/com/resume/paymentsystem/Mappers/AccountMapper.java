package com.resume.paymentsystem.Mappers;

import com.resume.paymentsystem.DAO.Entity.Account;
import com.resume.paymentsystem.DTO.AccountDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountMapper {

    AccountDTO accountToAccountDTO(Account account);

    Account accountDTOToAccount(AccountDTO accountDTO);

    List<AccountDTO> accountsToAccountDTOs(List<Account> accounts);

    List<Account> accountDTOsToAccounts(List<AccountDTO> accountDTOs);

}
