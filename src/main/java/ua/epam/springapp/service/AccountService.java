package ua.epam.springapp.service;

import ua.epam.springapp.dto.AccountDto;
import ua.epam.springapp.model.Account;

import java.util.UUID;

public interface AccountService extends GenericService<AccountDto, UUID> {
}
