package ua.epam.springapp.integration_db;

import com.google.gson.Gson;
import org.junit.Test;
import org.springframework.http.MediaType;
import ua.epam.springapp.dto.SkillDto;
import ua.epam.springapp.service.SkillMapper;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.epam.springapp.utils.SkillGenerator.generateSingleSkill;
public class SkillControllerIT extends BaseSpringIT {

    private static final String BASE_URL = "/api/v1/skills";
    private static final String NOT_FOUND_MSG = "Entity not found!";

    private final SkillMapper skillMapper = new SkillMapper();
    private final Gson gson = new Gson();

    @Test
    public void shouldAddAndGetSkill() throws Exception{
        SkillDto skillDto = skillMapper.toDto(generateSingleSkill());
        UUID id = skillDto.getId();

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(gson.toJson(skillDto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get(BASE_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(skillDto.getId().toString())))
                .andExpect(jsonPath("$.name", is(skillDto.getName())));

        mockMvc.perform(delete(BASE_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldSkillNotFound() throws Exception {
        UUID anyId = UUID.randomUUID();

        mockMvc.perform(get(BASE_URL + "/" + anyId)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(content().string(NOT_FOUND_MSG));
    }

    @Test
    public void shouldUpdateSkill() throws Exception {
        SkillDto skillDto = skillMapper.toDto(generateSingleSkill());
        UUID id = skillDto.getId();

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(gson.toJson(skillDto)))
                .andExpect(status().isCreated());

        SkillDto updatedSkillDto = new SkillDto(skillDto.getId(), "Update skill");

        mockMvc.perform(put(BASE_URL + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(updatedSkillDto)))
                .andExpect(status().isOk());

        mockMvc.perform(get(BASE_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedSkillDto.getId().toString())))
                .andExpect(jsonPath("$.name", is(updatedSkillDto.getName())));

        mockMvc.perform(delete(BASE_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldUpdatedSkillNotFound() throws Exception {
        SkillDto skillDto = skillMapper.toDto(generateSingleSkill());
        UUID id = skillDto.getId();

        mockMvc.perform(put(BASE_URL + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(skillDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(NOT_FOUND_MSG));
    }

    @Test
    public void shouldDeleteSkill() throws Exception {
        SkillDto skillDto = skillMapper.toDto(generateSingleSkill());
        UUID id = skillDto.getId();

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(gson.toJson(skillDto)))
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
    public void shouldDeletedSkillNotFound() throws Exception {
        UUID anyId = UUID.randomUUID();

        mockMvc.perform(delete(BASE_URL + "/" + anyId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(NOT_FOUND_MSG));
    }
}
