package ua.epam.springapp.controller;

import com.google.gson.Gson;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.epam.springapp.dto.SkillDto;
import ua.epam.springapp.service.SkillMapper;
import ua.epam.springapp.service.SkillService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static ua.epam.springapp.utils.SkillGenerator.generateSkills;
import static ua.epam.springapp.utils.SkillGenerator.generateSingleSkill;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SkillControllerTest {

    private static final String BASE_URL = "/api/v1/skills";

    private final SkillService skillService = Mockito.mock(SkillService.class);

    private final SkillMapper skillMapper = new SkillMapper();

    private final SkillController sut = new SkillController(skillService);

    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(sut).build();

    private final Gson gson = new Gson();

    @Test
    public void shouldGetAllSkills() throws Exception {
        List<SkillDto> skillDtoList = generateSkills(2)
                .stream()
                .map(skillMapper::toDto)
                .collect(Collectors.toList());

        given(this.skillService.getAll()).willReturn(skillDtoList);
        mockMvc.perform(get(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(skillDtoList.get(0).getId().toString())))
                .andExpect(jsonPath("$[1].id", is(skillDtoList.get(1).getId().toString())))
                .andExpect(jsonPath("$[0].name", is(skillDtoList.get(0).getName())))
                .andExpect(jsonPath("$[1].name", is(skillDtoList.get(1).getName())));
    }

    @Test
    public void shouldGetSkill() throws Exception{
        SkillDto skillDto = skillMapper.toDto(generateSingleSkill());
        UUID id = skillDto.getId();
        given(this.skillService.get(id)).willReturn(skillDto);

        mockMvc.perform(get(BASE_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(skillDto.getId().toString())))
                .andExpect(jsonPath("$.name", is(skillDto.getName())));
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
        SkillDto skillDto = skillMapper.toDto(generateSingleSkill());
        UUID id = skillDto.getId();
        given(this.skillService.update(id, skillDto)).willReturn(true);

        mockMvc.perform(put(BASE_URL + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(skillDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldDeleteSkill() throws Exception {
        SkillDto skillDto = skillMapper.toDto(generateSingleSkill());
        UUID id = skillDto.getId();
        given(this.skillService.remove(id)).willReturn(skillDto);

        mockMvc.perform(delete(BASE_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }
}
