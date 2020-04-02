package ua.epam.springapp.service;

import org.junit.Test;
import org.mockito.Mockito;
import ua.epam.springapp.dto.AccountDto;
import ua.epam.springapp.exception.EntityNotFoundException;
import ua.epam.springapp.model.Account;
import ua.epam.springapp.repository.AccountRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static ua.epam.springapp.utils.AccountGenerator.generateAccounts;
import static ua.epam.springapp.utils.AccountGenerator.generateSingleAccount;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class AccountServiceImplTest {

    private final AccountRepository accountRepository = Mockito.mock(AccountRepository.class);

    private final AccountMapper accountMapper = new AccountMapper();

    private final AccountServiceImpl sut = new AccountServiceImpl(accountRepository, accountMapper);

    @Test
    public void shouldGetAllAccounts() {
        int accountsSize = 10;
        List<Account> accounts = generateAccounts(accountsSize);
        doReturn(accounts).when(accountRepository).getAll();

        List<AccountDto> foundAccounts = sut.getAll();

        verify(accountRepository).getAll();
        assertThat(foundAccounts, hasSize(accountsSize));

        accounts.forEach(
                account -> assertThat(foundAccounts, hasItem(allOf(
                        hasProperty("id", is(account.getId())),
                        hasProperty("email", is(account.getEmail())),
                        hasProperty("status", is(account.getStatus().toString()))))
                )
        );
    }

    @Test
    public void shouldThrowWhenAccountsListIsEmpty() {
        doReturn(new ArrayList<Account>()).when(accountRepository).getAll();

        try {
            sut.getAll();
        } catch (Exception e) {
            assertThat(e, instanceOf(EntityNotFoundException.class));
        }

        verify(accountRepository).getAll();
    }

    @Test
    public void shouldGetAccount() {
        Account account = generateSingleAccount();
        UUID id = account.getId();
        doReturn(account).when(accountRepository).get(id);

        AccountDto foundAccount = sut.get(id);

        verify(accountRepository).get(id);

        assertThat(foundAccount, allOf(
                hasProperty("id", is(account.getId())),
                hasProperty("email", is(account.getEmail())),
                hasProperty("status", is(account.getStatus().toString())))
        );
    }

    @Test
    public void shouldThrowWhenAccountNotFound() {
        UUID anyId = UUID.randomUUID();
        doReturn(null).when(accountRepository).get(anyId);

        try {
            sut.get(anyId);
        } catch (Exception e) {
            assertThat(e, instanceOf(EntityNotFoundException.class));
        }

        verify(accountRepository).get(anyId);
    }

    @Test
    public void shouldAddAccount() {
        Account account = generateSingleAccount();
        AccountDto accountDto = accountMapper.toDto(account);
        sut.add(accountDto);

        verify(accountRepository).add(account);
    }

    @Test
    public void shouldUpdateAccount() {
        Account account = generateSingleAccount();
        UUID id = account.getId();
        AccountDto accountDto = accountMapper.toDto(account);
        doReturn(account).when(accountRepository).get(id);

        boolean updated = sut.update(id, accountDto);

        verify(accountRepository).get(id);

        assertThat(updated, is(true));
    }

    @Test
    public void shouldThrowWhenUpdatedAccountNotFound() {
        Account account = generateSingleAccount();
        UUID id = account.getId();
        AccountDto accountDto = accountMapper.toDto(account);
        doReturn(null).when(accountRepository).get(id);

        try {
            sut.update(id, accountDto);
        } catch (Exception e) {
            assertThat(e, instanceOf(EntityNotFoundException.class));
        }

        verify(accountRepository).get(id);
    }

    @Test
    public void shouldDeleteAccount() {
        Account account = generateSingleAccount();
        UUID id = account.getId();
        AccountDto accountDto = accountMapper.toDto(account);
        doReturn(account).when(accountRepository).remove(id);

        AccountDto deletedAccountDto = sut.remove(id);

        verify(accountRepository).remove(id);

        assertThat(deletedAccountDto, allOf(
                hasProperty("id", is(accountDto.getId())),
                hasProperty("email", is(accountDto.getEmail())),
                hasProperty("status", is(accountDto.getStatus())))
        );
    }

    @Test
    public void shouldThrowWhenDeletedAccountNotFound() {
        UUID anyId = UUID.randomUUID();
        doReturn(null).when(accountRepository).get(anyId);

        try {
            sut.remove(anyId);
        } catch (Exception e) {
            assertThat(e, instanceOf(EntityNotFoundException.class));
        }

        verify(accountRepository).remove(anyId);
    }
}
