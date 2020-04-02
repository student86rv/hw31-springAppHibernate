package ua.epam.springapp.controller;

import com.google.gson.Gson;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.epam.springapp.dto.DeveloperDto;
import ua.epam.springapp.service.AccountMapper;
import ua.epam.springapp.service.DeveloperMapper;
import ua.epam.springapp.service.DeveloperService;
import ua.epam.springapp.service.SkillMapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static ua.epam.springapp.utils.DeveloperGenerator.generateDevelopers;
import static ua.epam.springapp.utils.DeveloperGenerator.generateSingleDeveloper;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeveloperControllerTest {

    private static final String BASE_URL = "/api/v1/developers";

    private final DeveloperService developerService = Mockito.mock(DeveloperService.class);

    private final DeveloperMapper developerMapper = new DeveloperMapper(
            new AccountMapper(),
            new SkillMapper()
    );

    private final DeveloperController sut = new DeveloperController(developerService);

    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(sut).build();

    private final Gson gson = new Gson();

    @Test
    public void shouldGetAllDevelopers() throws Exception {
        List<DeveloperDto> developerDtoList = generateDevelopers(2)
                .stream()
                .map(developerMapper::toDto)
                .collect(Collectors.toList());

        given(this.developerService.getAll()).willReturn(developerDtoList);
        mockMvc.perform(get(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(developerDtoList.get(0).getId().toString())))
                .andExpect(jsonPath("$[1].id", is(developerDtoList.get(1).getId().toString())))
                .andExpect(jsonPath("$[0].name", is(developerDtoList.get(0).getName())))
                .andExpect(jsonPath("$[1].name", is(developerDtoList.get(1).getName())));
    }

    @Test
    public void shouldGetDeveloper() throws Exception {
        DeveloperDto developerDto = developerMapper.toDto(generateSingleDeveloper());
        UUID id = developerDto.getId();
        given(this.developerService.get(id)).willReturn(developerDto);

        mockMvc.perform(get(BASE_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(developerDto.getId().toString())))
                .andExpect(jsonPath("$.name", is(developerDto.getName())));
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
        DeveloperDto developerDto = developerMapper.toDto(generateSingleDeveloper());
        UUID id = developerDto.getId();

        given(this.developerService.update(id, developerDto)).willReturn(true);

        mockMvc.perform(put(BASE_URL + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(developerDto)))
                .andExpect(status().isOk());
    }
    @Test
    public void shouldDeleteDeveloper() throws Exception {
        DeveloperDto developerDto = developerMapper.toDto(generateSingleDeveloper());
        UUID id = developerDto.getId();

        given(this.developerService.remove(id)).willReturn(developerDto);

        mockMvc.perform(delete(BASE_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }
}
