package ua.epam.springapp.integration;

import com.google.gson.Gson;
import org.junit.Test;
import org.springframework.http.MediaType;
import ua.epam.springapp.dto.SkillDto;
import ua.epam.springapp.model.Skill;
import ua.epam.springapp.service.SkillMapper;


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
import static ua.epam.springapp.utils.SkillGenerator.generateSkills;
import static ua.epam.springapp.utils.SkillGenerator.generateSingleSkill;

public class SkillControllerIT extends BaseSpringIT {

    private static final String BASE_URL = "/api/v1/skills";
    private static final String NOT_FOUND_MSG = "Entity not found!";

    private final SkillMapper skillMapper = new SkillMapper();
    private final Gson gson = new Gson();

    @Test
    public void shouldGetAllSkills() throws Exception {
        List<Skill> skillsList = generateSkills(2);
        given(this.skillRepository.getAll()).willReturn(skillsList);
        mockMvc.perform(get(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(skillsList.get(0).getId().toString())))
                .andExpect(jsonPath("$[1].id", is(skillsList.get(1).getId().toString())))
                .andExpect(jsonPath("$[0].name", is(skillsList.get(0).getName())))
                .andExpect(jsonPath("$[1].name", is(skillsList.get(1).getName())));
    }

    @Test
    public void shouldAllSkillsNotFound() throws Exception {
        given(this.skillRepository.getAll()).willReturn(new ArrayList<>());
        mockMvc.perform(get(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(content().string(NOT_FOUND_MSG));
    }

    @Test
    public void shouldGetSkill() throws Exception{
        Skill skill = generateSingleSkill();
        UUID id = skill.getId();
        given(this.skillRepository.get(id)).willReturn(skill);

        mockMvc.perform(get(BASE_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(skill.getId().toString())))
                .andExpect(jsonPath("$.name", is(skill.getName())));
    }

    @Test
    public void shouldSkillNotFound() throws Exception {
        UUID anyId = UUID.randomUUID();
        given(this.skillRepository.get(anyId)).willReturn(null);
        mockMvc.perform(get(BASE_URL + "/" + anyId)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(content().string(NOT_FOUND_MSG));
    }

    @Test
    public void shouldAddSkill() throws Exception {
        SkillDto skillDto = skillMapper.toDto(generateSingleSkill());

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(gson.toJson(skillDto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldUpdateSkill() throws Exception {
        Skill skill = generateSingleSkill();
        SkillDto skillDto = skillMapper.toDto(skill);
        UUID id = skill.getId();
        given(this.skillRepository.get(id)).willReturn(skill);

        mockMvc.perform(put(BASE_URL + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(skillDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldUpdatedSkillNotFound() throws Exception {
        Skill skill = generateSingleSkill();
        SkillDto skillDto = skillMapper.toDto(skill);
        UUID id = skill.getId();
        given(this.skillRepository.get(id)).willReturn(null);

        mockMvc.perform(put(BASE_URL + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(skillDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(NOT_FOUND_MSG));
    }

    @Test
    public void shouldDeleteSkill() throws Exception {
        Skill skill = generateSingleSkill();
        UUID id = skill.getId();
        given(this.skillRepository.remove(id)).willReturn(skill);

        mockMvc.perform(delete(BASE_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldDeletedSkillNotFound() throws Exception {
        UUID anyId = UUID.randomUUID();
        given(this.skillRepository.remove(anyId)).willReturn(null);

        mockMvc.perform(delete(BASE_URL + "/" + anyId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(NOT_FOUND_MSG));
    }
}
