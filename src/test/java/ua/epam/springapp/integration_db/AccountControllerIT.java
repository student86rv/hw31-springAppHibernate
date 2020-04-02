package ua.epam.springapp.integration_db;

import com.google.gson.Gson;
import org.junit.Test;
import org.springframework.http.MediaType;
import ua.epam.springapp.dto.AccountDto;
import ua.epam.springapp.service.AccountMapper;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.epam.springapp.utils.AccountGenerator.generateSingleAccount;

public class AccountControllerIT extends BaseSpringIT {

    private static final String BASE_URL = "/api/v1/accounts";
    private static final String NOT_FOUND_MSG = "Entity not found!";

    private final AccountMapper accountMapper = new AccountMapper();
    private final Gson gson = new Gson();

    @Test
    public void shouldAddAndGetAccount() throws Exception {

        AccountDto accountDto = accountMapper.toDto(generateSingleAccount());
        UUID id = accountDto.getId();

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(gson.toJson(accountDto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get(BASE_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(accountDto.getId().toString())))
                .andExpect(jsonPath("$.email", is(accountDto.getEmail())))
                .andExpect(jsonPath("$.status", is(accountDto.getStatus())));

        mockMvc.perform(delete(BASE_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldAccountNotFound() throws Exception {
        UUID anyId = UUID.randomUUID();

        mockMvc.perform(get(BASE_URL + "/" + anyId)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(content().string(NOT_FOUND_MSG));
    }

    @Test
    public void shouldUpdateAccount() throws Exception {
        AccountDto accountDto = accountMapper.toDto(generateSingleAccount());
        UUID id = accountDto.getId();

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(gson.toJson(accountDto)))
                .andExpect(status().isCreated());

        AccountDto updatedAccountDto = new AccountDto(
                accountDto.getId(),
                "new_email@test.com",
                accountDto.getStatus());

        mockMvc.perform(put(BASE_URL + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(updatedAccountDto)))
                .andExpect(status().isOk());

        mockMvc.perform(get(BASE_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedAccountDto.getId().toString())))
                .andExpect(jsonPath("$.email", is(updatedAccountDto.getEmail())))
                .andExpect(jsonPath("$.status", is(updatedAccountDto.getStatus())));

        mockMvc.perform(delete(BASE_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldUpdatedAccountNotFound() throws Exception {
        AccountDto accountDto = accountMapper.toDto(generateSingleAccount());
        UUID id = accountDto.getId();

        mockMvc.perform(put(BASE_URL + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(accountDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(NOT_FOUND_MSG));
    }

    @Test
    public void shouldDeleteAccount() throws Exception {
        AccountDto accountDto = accountMapper.toDto(generateSingleAccount());
        UUID id = accountDto.getId();

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(gson.toJson(accountDto)))
                .andExpect(status().isCreated());

        mockMvc.perform(delete(BASE_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get(BASE_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(content().string(NOT_FOUND_MSG));
    }

    @Test
    public void shouldDeletedAccountNotFound() throws Exception {
        UUID anyId = UUID.randomUUID();

        mockMvc.perform(delete(BASE_URL + "/" + anyId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(NOT_FOUND_MSG));
    }
}
