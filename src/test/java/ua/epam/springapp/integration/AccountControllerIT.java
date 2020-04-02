package ua.epam.springapp.integration;

import com.google.gson.Gson;
import org.junit.Test;
import org.springframework.http.MediaType;
import ua.epam.springapp.dto.AccountDto;
import ua.epam.springapp.model.Account;
import ua.epam.springapp.service.AccountMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.epam.springapp.utils.AccountGenerator.generateAccounts;
import static ua.epam.springapp.utils.AccountGenerator.generateSingleAccount;

public class AccountControllerIT extends BaseSpringIT {

    private static final String BASE_URL = "/api/v1/accounts";
    private static final String NOT_FOUND_MSG = "Entity not found!";

    private final AccountMapper accountMapper = new AccountMapper();
    private final Gson gson = new Gson();

    @Test
    public void shouldGetAllAccounts() throws Exception {
        List<Account> accountList = generateAccounts(2);
        given(this.accountRepository.getAll()).willReturn(accountList);
        mockMvc.perform(get(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(accountList.get(0).getId().toString())))
                .andExpect(jsonPath("$[1].id", is(accountList.get(1).getId().toString())))
                .andExpect(jsonPath("$[0].email", is(accountList.get(0).getEmail())))
                .andExpect(jsonPath("$[1].email", is(accountList.get(1).getEmail())))
                .andExpect(jsonPath("$[0].status", is(accountList.get(0).getStatus().toString())))
                .andExpect(jsonPath("$[1].status", is(accountList.get(1).getStatus().toString())));
    }

    @Test
    public void shouldAllAccountsNotFound() throws Exception {
        given(this.accountRepository.getAll()).willReturn(new ArrayList<>());
        mockMvc.perform(get(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(content().string(NOT_FOUND_MSG));
    }

    @Test
    public void shouldGetAccount() throws Exception {
        Account account = generateSingleAccount();
        UUID id = account.getId();
        given(this.accountRepository.get(id)).willReturn(account);

        mockMvc.perform(get(BASE_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(account.getId().toString())))
                .andExpect(jsonPath("$.email", is(account.getEmail())))
                .andExpect(jsonPath("$.status", is(account.getStatus().toString())));
    }

    @Test
    public void shouldAccountNotFound() throws Exception {
        UUID anyId = UUID.randomUUID();
        given(this.accountRepository.get(anyId)).willReturn(null);
        mockMvc.perform(get(BASE_URL + "/" + anyId)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(content().string(NOT_FOUND_MSG));
    }

    @Test
    public void shouldAddAccount() throws Exception {
        AccountDto accountDto = accountMapper.toDto(generateSingleAccount());

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(gson.toJson(accountDto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldUpdateAccount() throws Exception {
        Account account = generateSingleAccount();
        AccountDto accountDto = accountMapper.toDto(account);
        UUID id = account.getId();
        given(this.accountRepository.get(id)).willReturn(account);

        mockMvc.perform(put(BASE_URL + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(accountDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldUpdatedAccountNotFound() throws Exception {
        Account account = generateSingleAccount();
        AccountDto accountDto = accountMapper.toDto(account);
        UUID id = account.getId();
        given(this.accountRepository.get(id)).willReturn(null);

        mockMvc.perform(put(BASE_URL + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(accountDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(NOT_FOUND_MSG));
    }

    @Test
    public void shouldDeleteAccount() throws Exception {
        Account account = generateSingleAccount();
        UUID id = account.getId();
        given(this.accountRepository.remove(id)).willReturn(account);

        mockMvc.perform(delete(BASE_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldDeletedAccountNotFound() throws Exception {
        UUID anyId = UUID.randomUUID();
        given(this.accountRepository.remove(anyId)).willReturn(null);

        mockMvc.perform(delete(BASE_URL + "/" + anyId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(NOT_FOUND_MSG));
    }
}
