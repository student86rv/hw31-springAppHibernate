package ua.epam.springapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.epam.springapp.dto.AccountDto;
import ua.epam.springapp.exception.EntityNotFoundException;
import ua.epam.springapp.model.Account;
import ua.epam.springapp.model.AccountStatus;
import ua.epam.springapp.repository.AccountRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepo;

    private final AccountMapper accountMapper;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepo, AccountMapper accountMapper) {
        this.accountRepo = accountRepo;
        this.accountMapper = accountMapper;
    }

    @Override
    public void add(AccountDto accountDto) {
        Account account = accountMapper.fromDto(accountDto);
        accountRepo.add(account);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountDto get(UUID id) {
        Account account = accountRepo.get(id);
        if (account == null) {
            throw new EntityNotFoundException();
        }
        return accountMapper.toDto(account);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDto> getAll() {
        List<Account> list = accountRepo.getAll();
        if (list == null || list.size() == 0) {
            throw new EntityNotFoundException();
        }
        return list.stream().map(accountMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean update(UUID id, AccountDto accountDto) {
        Account account = accountRepo.get(id);
        if (account == null) {
            throw new EntityNotFoundException();
        }

        account.setEmail(accountDto.getEmail());
        account.setStatus(AccountStatus.valueOf(accountDto.getStatus()));

        //return new dto!!!
        return true;
    }

    @Override
    @Transactional
    public AccountDto remove(UUID id) {
        Account account = accountRepo.remove(id);
        if (account == null) {
            throw new EntityNotFoundException();
        }
        return accountMapper.toDto(account);
    }
}
