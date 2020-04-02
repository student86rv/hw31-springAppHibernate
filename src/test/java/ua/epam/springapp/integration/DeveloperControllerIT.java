package ua.epam.springapp.integration;

import com.google.gson.Gson;
import org.junit.Test;
import org.springframework.http.MediaType;
import ua.epam.springapp.dto.DeveloperDto;
import ua.epam.springapp.model.Developer;
import ua.epam.springapp.service.AccountMapper;
import ua.epam.springapp.service.DeveloperMapper;
import ua.epam.springapp.service.SkillMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.epam.springapp.utils.DeveloperGenerator.generateDevelopers;
import static ua.epam.springapp.utils.DeveloperGenerator.generateSingleDeveloper;

public class DeveloperControllerIT extends BaseSpringIT {

    private static final String BASE_URL = "/api/v1/developers";
    private static final String NOT_FOUND_MSG = "Entity not found!";

    private final DeveloperMapper developerMapper = new DeveloperMapper(
            new AccountMapper(),
            new SkillMapper()
    );
    private final Gson gson = new Gson();

    @Test
    public void shouldGetAllDevelopers() throws Exception {
        List<Developer> developersList = generateDevelopers(2);
        given(this.developerRepository.getAll()).willReturn(developersList);
        mockMvc.perform(get(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(developersList.get(0).getId().toString())))
                .andExpect(jsonPath("$[1].id", is(developersList.get(1).getId().toString())))
                .andExpect(jsonPath("$[0].name", is(developersList.get(0).getName())))
                .andExpect(jsonPath("$[1].name", is(developersList.get(1).getName())));
    }

    @Test
    public void shouldAllDevelopersNotFound() throws Exception {
        given(this.developerRepository.getAll()).willReturn(new ArrayList<>());
        mockMvc.perform(get(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(content().string(NOT_FOUND_MSG));
    }

    @Test
    public void shouldGetDeveloper() throws Exception{
        Developer developer = generateSingleDeveloper();
        UUID id = developer.getId();
        given(this.developerRepository.get(id)).willReturn(developer);

        mockMvc.perform(get(BASE_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(developer.getId().toString())))
                .andExpect(jsonPath("$.name", is(developer.getName())));
    }

    @Test
    public void shouldDeveloperNotFound() throws Exception {
        UUID anyId = UUID.randomUUID();
        given(this.developerRepository.get(anyId)).willReturn(null);
        mockMvc.perform(get(BASE_URL + "/" + anyId)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(content().string(NOT_FOUND_MSG));
    }

    @Test
    public void shouldAddDeveloper() throws Exception {
        DeveloperDto developerDto = developerMapper.toDto(generateSingleDeveloper());

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(gson.toJson(developerDto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldUpdateDeveloper() throws Exception {
        Developer developer = generateSingleDeveloper();
        DeveloperDto developerDto = developerMapper.toDto(developer);
        UUID id = developer.getId();
        given(this.developerRepository.get(id)).willReturn(developer);

        mockMvc.perform(put(BASE_URL + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(developerDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldUpdatedDeveloperNotFound() throws Exception {
        Developer developer = generateSingleDeveloper();
        DeveloperDto developerDto = developerMapper.toDto(developer);
        UUID id = developer.getId();
        given(this.developerRepository.get(id)).willReturn(null);

        mockMvc.perform(put(BASE_URL + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(developerDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(NOT_FOUND_MSG));
    }

    @Test
    public void shouldDeleteDeveloper() throws Exception {
        Developer developer = generateSingleDeveloper();
        UUID id = developer.getId();
        given(this.developerRepository.remove(id)).willReturn(developer);

        mockMvc.perform(delete(BASE_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldDeleteDeveloperNotFound() throws Exception {
        UUID anyId = UUID.randomUUID();
        given(this.developerRepository.remove(anyId)).willReturn(null);

        mockMvc.perform(delete(BASE_URL + "/" + anyId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(NOT_FOUND_MSG));
    }
}
