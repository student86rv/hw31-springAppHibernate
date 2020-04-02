package ua.epam.springapp.controller;

import com.google.gson.Gson;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.epam.springapp.dto.AccountDto;
import ua.epam.springapp.model.Account;
import ua.epam.springapp.service.AccountMapper;
import ua.epam.springapp.service.AccountService;

import static ua.epam.springapp.utils.AccountGenerator.generateAccounts;
import static ua.epam.springapp.utils.AccountGenerator.generateSingleAccount;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AccountControllerTest {

    private static final String BASE_URL = "/api/v1/accounts";

    private final AccountService accountService = Mockito.mock(AccountService.class);

    private final AccountMapper accountMapper = new AccountMapper();

    private final AccountController sut = new AccountController(accountService);

    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(sut).build();

    private final Gson gson = new Gson();

    @Test
    public void shouldGetAllAccounts() throws Exception {
        List<AccountDto> accountDtoList = generateAccounts(2)
                .stream()
                .map(accountMapper::toDto)
                .collect(Collectors.toList());

        given(this.accountService.getAll()).willReturn(accountDtoList);
        mockMvc.perform(get(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(accountDtoList.get(0).getId().toString())))
                .andExpect(jsonPath("$[1].id", is(accountDtoList.get(1).getId().toString())))
                .andExpect(jsonPath("$[0].email", is(accountDtoList.get(0).getEmail())))
                .andExpect(jsonPath("$[1].email", is(accountDtoList.get(1).getEmail())))
                .andExpect(jsonPath("$[0].status", is(accountDtoList.get(0).getStatus())))
                .andExpect(jsonPath("$[1].status", is(accountDtoList.get(1).getStatus())));
    }

    @Test
    public void shouldGetAccount() throws Exception{
        AccountDto accountDto = accountMapper.toDto(generateSingleAccount());
        UUID id = accountDto.getId();
        given(this.accountService.get(id)).willReturn(accountDto);

        mockMvc.perform(get(BASE_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(accountDto.getId().toString())))
                .andExpect(jsonPath("$.email", is(accountDto.getEmail())))
                .andExpect(jsonPath("$.status", is(accountDto.getStatus().toString())));
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
        AccountDto accountDto = accountMapper.toDto(generateSingleAccount());
        UUID id = accountDto.getId();
        given(this.accountService.update(id, accountDto)).willReturn(true);

        mockMvc.perform(put(BASE_URL + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(accountDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldDeleteAccount() throws Exception {
        AccountDto accountDto = accountMapper.toDto(generateSingleAccount());
        UUID id = accountDto.getId();
        given(this.accountService.remove(id)).willReturn(accountDto);

        mockMvc.perform(delete(BASE_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }
}
