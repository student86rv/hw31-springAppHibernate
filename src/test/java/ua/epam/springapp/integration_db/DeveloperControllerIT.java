package ua.epam.springapp.integration_db;

import com.google.gson.Gson;
import org.junit.Test;
import org.springframework.http.MediaType;
import ua.epam.springapp.dto.DeveloperDto;
import ua.epam.springapp.service.AccountMapper;
import ua.epam.springapp.service.DeveloperMapper;
import ua.epam.springapp.service.SkillMapper;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.epam.springapp.utils.DeveloperGenerator.generateSingleDeveloper;

public class DeveloperControllerIT extends BaseSpringIT {

    private static final String BASE_URL = "/api/v1/developers";
    private static final String NOT_FOUND_MSG = "Entity not found!";

    private final DeveloperMapper developerMapper = new DeveloperMapper(
            new AccountMapper(),
            new SkillMapper());

    private final Gson gson = new Gson();

    @Test
    public void shouldAddAndGetDeveloper() throws Exception {
        DeveloperDto developerDto = developerMapper.toDto(generateSingleDeveloper());
        UUID id = developerDto.getId();

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(gson.toJson(developerDto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get(BASE_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(developerDto.getId().toString())))
                .andExpect(jsonPath("$.name", is(developerDto.getName())));

        mockMvc.perform(delete(BASE_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldDeveloperNotFound() throws Exception {
        UUID anyId = UUID.randomUUID();

        mockMvc.perform(get(BASE_URL + "/" + anyId)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(content().string(NOT_FOUND_MSG));
    }

    @Test
    public void shouldUpdateDeveloper() throws Exception {
        DeveloperDto developerDto = developerMapper.toDto(generateSingleDeveloper());
        UUID id = developerDto.getId();

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(gson.toJson(developerDto)))
                .andExpect(status().isCreated());

        DeveloperDto updatedDeveloperDto = new DeveloperDto(
                developerDto.getId(),
                "Updated Developer",
                developerDto.getSkills(),
                developerDto.getAccount());

        mockMvc.perform(put(BASE_URL + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(updatedDeveloperDto)))
                .andExpect(status().isOk());

        mockMvc.perform(get(BASE_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedDeveloperDto.getId().toString())))
                .andExpect(jsonPath("$.name", is(updatedDeveloperDto.getName())));

        mockMvc.perform(delete(BASE_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldUpdatedDeveloperNotFound() throws Exception {
        DeveloperDto developerDto = developerMapper.toDto(generateSingleDeveloper());
        UUID id = developerDto.getId();

        mockMvc.perform(put(BASE_URL + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(developerDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(NOT_FOUND_MSG));
    }

    @Test
    public void shouldDeleteDeveloper() throws Exception {
        DeveloperDto developerDto = developerMapper.toDto(generateSingleDeveloper());
        UUID id = developerDto.getId();

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(gson.toJson(developerDto)))
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
    public void shouldDeleteDeveloperNotFound() throws Exception {
        UUID anyId = UUID.randomUUID();

        mockMvc.perform(delete(BASE_URL + "/" + anyId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(NOT_FOUND_MSG));
    }
}
