package ua.epam.springapp.service;

import org.junit.Test;
import org.mockito.Mockito;
import ua.epam.springapp.dto.SkillDto;
import ua.epam.springapp.exception.EntityNotFoundException;
import ua.epam.springapp.model.Skill;
import ua.epam.springapp.repository.SkillRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static ua.epam.springapp.utils.SkillGenerator.generateSingleSkill;
import static ua.epam.springapp.utils.SkillGenerator.generateSkills;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class SkillServiceImplTest {

    private final SkillRepository skillRepository = Mockito.mock(SkillRepository.class);

    private final SkillMapper skillMapper = new SkillMapper();

    private final SkillServiceImpl sut = new SkillServiceImpl(skillRepository, skillMapper);

    @Test
    public void shouldGetAllSkills() {
        int skillsSize = 10;
        List<Skill> skills = generateSkills(skillsSize);
        doReturn(skills).when(skillRepository).getAll();

        List<SkillDto> foundSkills = sut.getAll();

        verify(skillRepository).getAll();
        assertThat(foundSkills, hasSize(skillsSize));

        skills.forEach(
                skill -> assertThat(foundSkills, hasItem(allOf(
                        hasProperty("id", is(skill.getId())),
                        hasProperty("name", is(skill.getName()))))
                )
        );
    }

    @Test
    public void shouldThrowWhenSkillsListIsEmpty() {
        doReturn(new ArrayList<Skill>()).when(skillRepository).getAll();

        try {
            sut.getAll();
        } catch (Exception e) {
            assertThat(e, instanceOf(EntityNotFoundException.class));
        }

        verify(skillRepository).getAll();
    }

    @Test
    public void shouldGetSkill() {
        Skill skill = generateSingleSkill();
        UUID id = skill.getId();
        doReturn(skill).when(skillRepository).get(id);

        SkillDto foundSkill = sut.get(id);

        verify(skillRepository).get(id);

        assertThat(foundSkill, allOf(
                hasProperty("id", is(skill.getId())),
                hasProperty("name", is(skill.getName())))
        );
    }

    @Test
    public void shouldThrowWhenSkillNotFound() {
        UUID anyId = UUID.randomUUID();
        doReturn(null).when(skillRepository).get(anyId);

        try {
            sut.get(anyId);
        } catch (Exception e) {
            assertThat(e, instanceOf(EntityNotFoundException.class));
        }

        verify(skillRepository).get(anyId);
    }

    @Test
    public void shouldAddSkill() {
        Skill skill = generateSingleSkill();
        SkillDto skillDto = skillMapper.toDto(skill);
        sut.add(skillDto);

        verify(skillRepository).add(skill);
    }

    @Test
    public void shouldUpdateSkill() {
        Skill skill = generateSingleSkill();
        SkillDto skillDto = skillMapper.toDto(skill);
        UUID id = skill.getId();
        doReturn(skill).when(skillRepository).get(id);

        boolean updated = sut.update(id, skillDto);

        verify(skillRepository).get(id);

        assertThat(updated, is(true));
    }

    @Test
    public void shouldThrowWhenUpdatedSkillNotFound() {
        Skill skill = generateSingleSkill();
        SkillDto skillDto = skillMapper.toDto(skill);
        UUID id = skill.getId();
        doReturn(null).when(skillRepository).get(id);

        try {
            sut.update(id, skillDto);
        } catch (Exception e) {
            assertThat(e, instanceOf(EntityNotFoundException.class));
        }

        verify(skillRepository).get(id);
    }

    @Test
    public void shouldDeleteSkill() {
        Skill skill = generateSingleSkill();
        UUID id = skill.getId();
        doReturn(skill).when(skillRepository).remove(id);

        SkillDto deletedAccount = sut.remove(id);

        verify(skillRepository).remove(id);

        assertThat(deletedAccount, allOf(
                hasProperty("id", is(skill.getId())),
                hasProperty("name", is(skill.getName())))
        );
    }

    @Test
    public void shouldThrowWhenDeletedSkillNotFound() {
        UUID anyId = UUID.randomUUID();
        doReturn(null).when(skillRepository).get(anyId);

        try {
            sut.remove(anyId);
        } catch (Exception e) {
            assertThat(e, instanceOf(EntityNotFoundException.class));
        }

        verify(skillRepository).remove(anyId);
    }
}
