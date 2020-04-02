package ua.epam.springapp.service;

import org.springframework.stereotype.Service;
import ua.epam.springapp.dto.AccountDto;
import ua.epam.springapp.model.Account;
import ua.epam.springapp.model.AccountStatus;

import java.util.UUID;

@Service
public class AccountMapper {

    public AccountDto toDto(Account account) {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(account.getId());
        accountDto.setEmail(account.getEmail());
        accountDto.setStatus(account.getStatus().toString());
        return accountDto;
    }

    public Account fromDto(AccountDto accountDto) {
        Account account = new Account();
        UUID dtoId = accountDto.getId();
        if (dtoId != null) {
            account.setId(dtoId);
        }
        account.setEmail(accountDto.getEmail());
        account.setStatus(AccountStatus.valueOf(accountDto.getStatus()));
        return account;
    }
}
